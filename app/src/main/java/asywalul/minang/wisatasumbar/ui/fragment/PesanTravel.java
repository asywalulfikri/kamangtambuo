package asywalul.minang.wisatasumbar.ui.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.adapter.PesanTravelAdapter;
import asywalul.minang.wisatasumbar.adapter.QuestionAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.database.ConversationDb;
import asywalul.minang.wisatasumbar.database.QuestionDb;
import asywalul.minang.wisatasumbar.http.QuestionConnection;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.ConversationWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.PostQuestionAcivity;
import asywalul.minang.wisatasumbar.ui.activity.Profile_Activity_Other_User;
import asywalul.minang.wisatasumbar.ui.activity.QuestionDetailActivity;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.NetworkUtils;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.FloatingActionButton1;
import asywalul.minang.wisatasumbar.widget.FloatingActionsMenu;
import asywalul.minang.wisatasumbar.widget.ItemClickSupport;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.MyGridLayoutManager;
import asywalul.minang.wisatasumbar.widget.SimpleDividerItemDecoration;

;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class PesanTravel extends BaseListFragment implements QuestionAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    // inisial buat ui
    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private RelativeLayout rlLoadMore;
    private ProgressBar progressBar;
    private TextView tvLoadMore;
    private View footerView;
    private FloatingActionsMenu menuMultipleActions;
    private View viewFloatingBt;

    // inisial database
    private ConversationDb mConvDb;
    private QuestionDb mQuestionDb;
    private CacheDb mCacheDb;

    //inisial adapter
    private PesanTravelAdapter mAdapter;
    private ConversationWrapper wrapper;
    private ArrayList<Conversation> mConversationList = new ArrayList<Conversation>();
    private int mPage = 0;

    private static final int REQ_CODE = 1000;

    private boolean mGetCache = false;
    private LoadCacheTask mCacheTask;
    String idLikeQuestion;
    private int offSet = 0;
    RequestQueue requestQueue;
    private LoadTask mLoadTask;
    private String iduser;
    private UpdateCacheTask mUpdCacheTask;
    public final static String SP = "sharedAt";
    private boolean loading = true;
    protected boolean mIsLoading = false;
    private int vote = 0;
    private Conversation conversation;
    public static PesanTravel newInstance() {
        return new PesanTravel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_question, container, false);

        mCacheDb    = new CacheDb(getDatabase());
        mConvDb     = new ConversationDb(getDatabase());
        mQuestionDb = new QuestionDb(getDatabase());

        mRecyclerView  = (RecyclerView)root.findViewById(R.id.lv_data);
        mLoadingLayout = (LoadingLayout)root.findViewById(R.id.layout_loading);
        swipContainer  = (SwipeRefreshLayout)root.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.fab_ripple_color));

        viewFloatingBt = (View) root.findViewById(R.id.view);
        menuMultipleActions = (FloatingActionsMenu) root.findViewById(R.id.multiple_actions);
        final FloatingActionButton1 actionA = (FloatingActionButton1) root.findViewById(R.id.action_a);
        final FloatingActionButton1 actionB = (FloatingActionButton1) root.findViewById(R.id.action_b);

        swipContainer.setOnRefreshListener(this);

        viewFloatingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuMultipleActions.isExpanded()) {
                    menuMultipleActions.toggle();
                    viewFloatingBt.setVisibility(View.GONE);
                    viewFloatingBt.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        menuMultipleActions.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuMultipleActions.toggle();

                if (menuMultipleActions.isExpanded()) {
                    viewFloatingBt.setBackgroundColor(getResources().getColor(R.color.transparent_dark));
                    viewFloatingBt.setVisibility(View.VISIBLE);
                } else {
                    viewFloatingBt.setBackgroundColor(getResources().getColor(R.color.transparent));
                    viewFloatingBt.setVisibility(View.GONE);
                }
            }
        });

        actionA.setIcon(R.drawable.ic_face_black_24dp);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostQuestionAcivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        actionB.setIcon(R.drawable.ic_face_black_24dp);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostQuestionAcivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        setupRecyclerView();
        return root;
    }

    @Override
    public void onPause() {
        if (menuMultipleActions.isExpanded()) {
            menuMultipleActions.toggle();
            viewFloatingBt.setVisibility(View.GONE);
            viewFloatingBt.setBackgroundColor(getResources().getColor(R.color.transparent));
        }

        if (mIsLoading && mUpdCacheTask != null) {
            mUpdCacheTask.cancel(true);
        }

        if (mIsLoading && mCacheTask != null) {
            mCacheTask.cancel(true);
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        mConvDb.reload(getDatabase());
        mCacheDb.reload(getDatabase());
        mQuestionDb.reload(getDatabase());

        if (NetworkUtils.isNetworkAvailable(getActivity())) {

            (mLoadTask = new LoadTask()).execute();
        }

        if (mGetCache == false) {
            Log.d("gak ada", "network");
// refresh();
        }

        if (!isListVisible()) {
            (mCacheTask = new LoadCacheTask()).execute();
        }
    }

    @Override
    public void loadData() {
        (mLoadTask = new LoadTask()).execute();
    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.search) {
            showSearchView();
            return true;
        }if (id == R.id.chat){
            startActivity(new Intent(getActivity(), InboxActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
*/

    private void setupRecyclerView() {

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PesanTravelAdapter(getActivity(),iduser);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager.setSpanSizeLookup(new MyGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((mAdapter.hasHeader() && mAdapter.isHeader(position)) ||
                        mAdapter.hasFooter() && mAdapter.isFooter(position))
                    return mLayoutManager.getSpanCount();

                return 1;
            }
        });

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                conversation = mConversationList.get(position);

                Intent intent = new Intent(getActivity(),QuestionDetailActivity.class);
                intent.putExtra(Util.getIntentName("position"), position);
                intent.putExtra(Util.getIntentName("question"),conversation);
                startActivity(intent);
            }
        });

        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);

        rlLoadMore = (RelativeLayout) footerView.findViewById(R.id.rl_load_more);
        progressBar = (ProgressBar) footerView.findViewById(R.id.pb_progress);
        tvLoadMore = (TextView) footerView.findViewById(R.id.tv_loading);

        rlLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                tvLoadMore.setText(getString(R.string.text_loading));

                offSet = mPage+1;

                //loadTask(String.valueOf(offSet));
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.i("On Result");

        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || mConversationList == null || mConversationList.size() == 0) return;

            int position = data.getIntExtra(Util.getIntentName("position"), 0);
            Conversation rconv = data.getParcelableExtra(Util.getIntentName("conversation"));
            Conversation conv = mConversationList.get(position);

            mConversationList.get(position).totalResponses = rconv.totalResponses;
            mConversationList.get(position).totalVotes = rconv.totalVotes;
            mConversationList.get(position).totalShares = rconv.totalShares;
            mConversationList.get(position).isVoted = rconv.isVoted;

            conv.totalResponses = rconv.totalResponses;
            conv.totalVotes = rconv.totalVotes;
            conv.totalShares = rconv.totalShares;
            conv.isVoted = rconv.isVoted;
            SharedPreferences getId = getActivity()
                    .getSharedPreferences(SP, 0);
            String iduser = getId.getString(Cons.KEY_ID, "Nothing");
           // wrapper = mCacheDb.getListQuestion("question-" + iduser);

            mAdapter.notifyDataSetChanged();
            mCacheDb.update(conv, "question-" +iduser);
            Debug.i("On Result");

        }
    }
    private  void unLikeQuestionHome (View v,final  int pos){
        final Conversation conversation = mConversationList.get(pos);
        idLikeQuestion = conversation.conversationId;

        new unLikeQuestionHome(pos).execute();
    }

    private void likeQuestionHome(View v, final int pos) {
        final Conversation conversation = mConversationList.get(pos);
        idLikeQuestion = conversation.conversationId;

        new LikeQuestionHome(pos).execute();
    }

    private  void profileUser(View v, final  int pos){
        final Conversation conversation = mConversationList.get(pos);
        Intent intent = new Intent(getActivity(), Profile_Activity_Other_User.class);
        intent.putExtra(Util.getIntentName("position"), pos);
        intent.putExtra(Util.getIntentName("question"),conversation);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void moreQuestionHome(View v, final int pos) {
        final Conversation conversation= mConversationList.get(pos);
        idLikeQuestion = conversation.conversationId;

    }

    protected boolean isListVisible() {
        return (mRecyclerView.getVisibility() == View.VISIBLE) ? true : false;
    }

    private void updateView(ConversationWrapper wrapper) {
        showList();

        progressBar.setVisibility(View.GONE);
        tvLoadMore.setText(getString(R.string.text_next));

        mAdapter = new PesanTravelAdapter(getActivity(),iduser);
        mAdapter.setData(wrapper.list);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setFooterView(footerView);

      //  mAdapter.setClickListener(this);
        mAdapter.notifyDataSetChanged();
/*
        mAdapter.likeQuestion(new QuestionAdapter.LikeQuestion() {

            @Override
            public void OnLikeClickQuestion(View view, int position) {
                // TODO Auto-generated method stub
                if (mConversationList.get(position).isVoted == 1) {
                    // Toast.makeText(getActivity(), "Sudah Disukai", Toast.LENGTH_SHORT).show();

                    mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes - 1;
                    vote = mConversationList.get(position).isVoted = 0;
                    unLikeQuestionHome(view, position);

                } else if (mConversationList.get(position).isVoted == 0) {
                    mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes + 1;
                    vote = mConversationList.get(position).isVoted = 1;
                    likeQuestionHome(view, position);
                }
            }
        });

        mAdapter.likeArticle(new QuestionAdapter.LikeArticle() {

            @Override
            public void OnLikeClickArticle(View view, int position) {
                // TODO Auto-generated method stub
                if (mConversationList.get(position).isVoted == 1) {
                    // Toast.makeText(getActivity(), "Sudah Disukai", Toast.LENGTH_SHORT).show();

                    mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes - 1;
                    vote = mConversationList.get(position).isVoted = 0;
                    unLikeQuestionHome(view, position);

                } else if (mConversationList.get(position).isVoted == 0) {
                    mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes + 1;
                    vote = mConversationList.get(position).isVoted = 1;
                    likeQuestionHome(view, position);
                }
            }
        });

        mAdapter.profileUser(new QuestionAdapter.ProfileUser() {
            public void OnClickProfile(View view, int position) {

                mConversationList.get(position);
                profileUser(view, position);
            }
        });

        mAdapter.moreQuestion(new QuestionAdapter.MoreQuestion() {
            public void OnMoreClickQuestion(View view, int position) {
                if (mConversationList.get(position).user.userId == iduser && mConversationList.get(position).attachment.equals(mConversationList.get(position).attachment)) {
                    showOptionDialog(view, position);
                } else if (mConversationList.get(position).attachment.equals("") && mConversationList.get(position).user.userId.equals(iduser)) {
                    showOptionDialogjustDelete(view, position);
                } else {
                    showOptionDialogOtherUser(view, position);
                }
            }
        });*/

    }

    private void loadMoreView (ConversationWrapper wrapper) {
        if (wrapper.hasNext = false) {
            mAdapter.removeFooter();
        }

        progressBar.setVisibility(View.GONE);
        tvLoadMore.setText(getString(R.string.text_next));

        mAdapter.addItem(wrapper.list);
        mAdapter.notifyDataSetChanged();
    }

    protected void showClearEmpty(String message) {
        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        mRecyclerView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.hideAndEmpty(message, false);
    }

    protected void showList() {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Conversation conversation = mConversationList.get(position);

        if (buildVersion()) {
            Intent intent     = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra(Util.getIntentName("position"), position);
            intent.putExtra(Util.getIntentName("question"), conversation);

            // this for animation sharedElements
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    new Pair<View, String>(view.findViewById(R.id.layout_questions), getString(R.string.shared_element_image_cover)));
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        } else {
            Intent intent     = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra(Util.getIntentName("position"), position);
            intent.putExtra(Util.getIntentName("question"), conversation);
            startActivity(intent);
        }

    }




    @Override
    public void onRefresh() {

        mIsRefresh = true;
        mPage = 1;
        (mLoadTask = new LoadTask()).execute();

    }

    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        ConversationWrapper wrapper;

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
//             Debug.i("Checking cache " + "home-" + mConvObject);
                SharedPreferences getId = getActivity()
                        .getSharedPreferences(SP, 0);
                String iduser = getId.getString(Cons.KEY_ID, "Nothing");
               // wrapper = mCacheDb.getListQuestion("question-" + iduser);
                result  = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            if (wrapper != null) {
                if (wrapper.list.size() > 0) {
                    // doneLoading();

                    mConversationList = new ArrayList<Conversation>();

                    updateView(wrapper);

                    //showToast("From db");
                } else {
                    //Debug.i("Cache not found");
                }
            } else {
                // Debug.i("Cache not found");
            }

            refresh();
        }
    }

    public class UpdateCacheTask extends AsyncTask<URL, Integer, Long> {
        ConversationWrapper wrapper;

        public UpdateCacheTask(ConversationWrapper wrapper) {
            this.wrapper = wrapper;
        }

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            Debug.i("Update Cache");
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            SharedPreferences getId = getActivity()
                    .getSharedPreferences(SP, 0);
            String iduser = getId.getString(Cons.KEY_ID, "Nothing");
            try {
                mCacheDb.updateQuestionList(wrapper.list,"question-"+iduser);
                Debug.i("update cache " + "question-"+iduser);
                result = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    public class LikeQuestionHome extends AsyncTask<URL, Integer, Long> {
        String error = "";
        int position;

        protected LikeQuestionHome(int position) {
            this.position = position;
        }

        protected void onCancelled() {
            mConversationList.get(position).isVoted = 0;
            mIsLoading = false;
        }

        protected void onPreExecute() {
            mIsLoading = true;

            //  Toast.makeText(getActivity(),"Disukai",Toast.LENGTH_SHORT).show();

        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            SharedPreferences getId = getActivity()
                    .getSharedPreferences(SP, 0);
            String iduser = getId.getString(Cons.KEY_ID, "Nothing");

            try {
                QuestionConnection conn = new QuestionConnection();

                conn.like(idLikeQuestion,iduser);

                result  = 1;
            } catch (WisataException e) {
                e.printStackTrace();
                error  = e.getError();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            mIsLoading = false;

            if (result == 1) {
                //showToastNew(getString(R.string.text_question_liked));
                //  mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes + 1;
                // mCacheDb.updateLike(mConversationList.get(position));
                mAdapter.notifyDataSetChanged();
            } else {
                error = (error.equals("")) ? getString(R.string.text_gagal_like) : error;
                mConversationList.get(position).isVoted = 0;

                showError(error);

            }
        }
    }

    public class unLikeQuestionHome extends AsyncTask<URL, Integer, Long> {
        String error = "";
        int position;

        protected unLikeQuestionHome(int position) {
            this.position = position;
        }

        protected void onCancelled() {
            mConversationList.get(position).isVoted = 1;
            mIsLoading = false;
        }

        protected void onPreExecute() {
            mIsLoading = true;

            // Toast.makeText(getActivity(),"Batal Menyukai",Toast.LENGTH_SHORT).show();

        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            SharedPreferences getId = getActivity()
                    .getSharedPreferences(SP, 0);
            String iduser = getId.getString(Cons.KEY_ID, "Nothing");

            try {
                QuestionConnection conn = new QuestionConnection();

                conn.unlike(idLikeQuestion, iduser);

                result  = 1;
            } catch (WisataException e) {
                e.printStackTrace();
                error  = e.getError();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            mIsLoading = false;

            if (result == 1) {
                //showToastNew(getString(R.string.text_question_liked));
                // mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes - 1;
                // mCacheDb.updateLike(mConversationList.get(position));
                mAdapter.notifyDataSetChanged();
            } else {
                error = (error.equals("")) ? getString(R.string.text_gagal_like) : error;
                mConversationList.get(position).isVoted = 1;

                showError(error);

            }
        }
    }
    // Ini untuk menyimpan image ke di direktori / galeri hp
    public void getBitmapFromURL(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/wisatasumbar");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/wisatasumbar", "wisatasumbar.jpg");
        Toast.makeText(getActivity(),getString(R.string.text_save_image_succses),Toast.LENGTH_SHORT).show();

        mgr.enqueue(request);

    }
    // ini untuk menyimpan link image
    public void copyLinkImage(String path) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(path);
            Toast.makeText(getActivity(), getString(R.string.text_copy_url_clipboard), Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", path);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), getString(R.string.text_copy_url_clipboard), Toast.LENGTH_SHORT).show();
        }
    }

   /* public void loadTask(String page) {

        requestQueue = Volley.newRequestQueue(getActivity());
        SharedPreferences getId = getActivity()
                .getSharedPreferences(SP, 0);
        String iduser = getId.getString(Cons.KEY_ID, "Nothing");

        String url = Cons.CONVERSATION_URL+ "/listQuestion.php?userId="+iduser+"&offset="+page+"&limit=20";
        Log.d("url",url);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());

                        try {

                            JSONArray dataJson = response.getJSONArray("data");
                            parsingData(dataJson);
                            mAdapter.notifyDataSetChanged();
                            showList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        //  hidepDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                // hidepDialog();
            }
        });
        // Adds the JSON object request "obreq" to the request queue
        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }*/

    private void parsingData(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            Conversation conversation = new Conversation();
            JSONObject itemJson = jsonArray.getJSONObject(i);
            conversation.conversationId     = itemJson.getString(Cons.CONV_ID);
            conversation.content     = itemJson.getString(Cons.CONV_CONTENT);
            conversation.tags             = itemJson.getString(Cons.CONV_TAGS);
            conversation.dateSubmitted     = itemJson.getString(Cons.CONV_DATE_SUBMITTED);
            conversation.totalResponses     = itemJson.getInt(Cons.CONV_TOTAL_RESPONSES);
            conversation.totalVotes         = itemJson.getInt(Cons.CONV_TOTAL_VOTE);
            conversation.attachment         = itemJson.getString(Cons.CONV_ATTACHMENT);
            conversation.isVoted  = itemJson.getInt(Cons.CONV_ISVOTED);
            conversation.type               = itemJson.getString(Cons.CONV_TYPE);
            conversation.title              = itemJson.getString(Cons.CONV_TITLE);
            conversation.summary            = itemJson.getString(Cons.CONV_SUMMARY);
            conversation.latitude           = itemJson.getString(Cons.KEY_LATITUDE);
            conversation.longitude          = itemJson.getString(Cons.KEY_LONGITUDE);

            conversation.user = new User();

            conversation.user.userId     = itemJson.getString(Cons.KEY_ID);
            conversation.user.fullName      = itemJson.getString(Cons.KEY_NAME);
            conversation.user.avatar        = itemJson.getString(Cons.KEY_AVATAR);
            conversation.user.gender        = itemJson.getString(Cons.KEY_GENDER);
            conversation.user.status        = itemJson.getString(Cons.KEY_STATUS);
            conversation.user.msisdn        = itemJson.getString(Cons.KEY_MSISDN);
            conversation.user.email         = itemJson.getString(Cons.KEY_EMAIL);
            conversation.user.birthDate     = itemJson.getString(Cons.KEY_BIRTH);

            mConversationList.add(conversation);
        }
    }

    public class LoadTask extends AsyncTask<URL, Integer, Long> {
        ConversationWrapper wrapper;

        protected void onCancelled() {
            // doneLoading();
        }

        protected void onPreExecute() {
            //  showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {

                QuestionConnection conn = new QuestionConnection();
                wrapper = conn.getList(iduser, offSet);

                result  = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            // doneLoading();
            swipContainer.setRefreshing(false);

            //mLoadReadLater = false;

            if (result == 1) {
                if (wrapper != null) {
                    if (wrapper.list.size() == 0) {
                        showClearEmpty(getString(R.string.text_no_data));
                    } else {
                        if (mIsRefresh) {
                            mConversationList = new ArrayList<Conversation>();
                        }

                        updateView(wrapper);

                        (mUpdCacheTask = new UpdateCacheTask(wrapper)).execute();
                    }
                } else {
                    showClearEmpty(getString(R.string.text_no_data));
                }
            } else {
                showClearEmpty(getString(R.string.text_download_failed));
            }
        }
    }

}