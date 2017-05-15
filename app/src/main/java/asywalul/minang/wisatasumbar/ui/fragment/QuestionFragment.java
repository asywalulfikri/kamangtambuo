package asywalul.minang.wisatasumbar.ui.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wx.goodview.GoodView;

import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.adapter.QuestionAdapterr;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.http.QuestionConnection;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.ConversationWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.PostQuestionAcivity;
import asywalul.minang.wisatasumbar.ui.activity.Profile_Activity_Other_User;
import asywalul.minang.wisatasumbar.ui.activity.QuestionDetailActivity;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.TLSSocketFactory;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;
import asywalul.minang.wisatasumbar.widget.FloatingActionButton1;
import asywalul.minang.wisatasumbar.widget.FloatingActionsMenu;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.OnSwipeTouchListener;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class QuestionFragment extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener {
   /* inisial buat user interface*/

    private ExpandableHeightListView mQuestionLv;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton1 actionA, actionB;
    private View viewFloatingBt;
    private boolean mIsLoading = false;

    // inisial database dan cache

    private CacheDb mCacheDb;
    private LoadCacheTask mCacheTask;

    //inisial adapter

    private QuestionAdapterr mAdapter;
    private ArrayList<Conversation> mConversationList = new ArrayList<Conversation>();
    private int mPage = 1;

    //variabel digunakan

    private String idLikeQuestion;
    private String iduser;
    private int page = 1;
    private int vote = 0;
    private static final int REQ_CODE = 1000;
    private Intent intent;
    private User user;
    private GoodView goodView;
    private RequestQueue requestQueue;

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question, container, false);
        mCacheDb = new CacheDb(getDatabase());
        user = getUser();
        RequestQueueVolley();

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>

        iduser = user.userId;
        goodView = new GoodView(getActivity());
        mLoadingLayout = (LoadingLayout) root.findViewById(R.id.layout_loading);
        swipContainer  = (SwipeRefreshLayout) root.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);


        mQuestionLv = (ExpandableHeightListView) root.findViewById(R.id.lv_data);
        mQuestionLv.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onClick(View view, MotionEvent e) {
                int position = mQuestionLv.pointToPosition((int) e.getX(), (int) e.getY());
                final Conversation articles = mConversationList.get(position);

                if(user.isLogin.equals("")||user.isLogin.equals("null")){
                    showDialogLogin(getString(R.string.text_login_first));
                }else {
                   /* if(buildVersion()&& (!articles.attachment.equals(""))) {
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                new Pair<View, String>(view.findViewById(R.id.iv_item_grid), getString(R.string.shared_element_image_cover)));
                        ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
                    }else {*/
                        Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                        intent.putExtra(Util.getIntentName("position"), position);
                        intent.putExtra(Util.getIntentName("question"), articles);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(intent,122);
                  //  }

                }

                super.onClick(view, e);
            }

        });

        mQuestionLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (mQuestionLv.getLastVisiblePosition() - mQuestionLv.getHeaderViewsCount() -
                        mQuestionLv.getFooterViewsCount()) >= (mAdapter.getCount() - 1)) {

                    loadTask3();
                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        ButtonActionMenu(root);   // UNTUK MEMANGGIL FUNGSI FLOATING BUTTON


        return root;
    }

    private void RequestQueueVolley() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            HttpStack stack = null;
            try {
                stack = new HurlStack(null, new TLSSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.d("Your Wrapper Class", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            }
            requestQueue = Volley.newRequestQueue(getActivity(), stack);
        } else {
            requestQueue = Volley.newRequestQueue(getActivity());
        }
    }


    @Override
    public void onPause() {
        if (menuMultipleActions.isExpanded()) {
            menuMultipleActions.toggle();
            viewFloatingBt.setVisibility(View.GONE);
            viewFloatingBt.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
        if (mIsLoading && mCacheTask != null) {
            mCacheTask.cancel(true);
        }

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCacheDb.reload(getDatabase());
        if (!isListVisible()) {
            (mCacheTask = new LoadCacheTask()).execute();
        }
    }

    protected boolean isListVisible() {
        return (mQuestionLv.getVisibility() == View.VISIBLE) ? true : false;
    }

    protected void showList() {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mQuestionLv.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    protected void showEmpty(String message) {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mLoadingLayout.hideAndEmpty(message, false);
        }
    }

    protected void scrollMyListViewToBottom(final int sel) {
        mQuestionLv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mQuestionLv.setSelection(sel);
            }
        });
    }

    @Override
    public void loadData() {
        loadTask2();
    }

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
        Toast.makeText(getActivity(), getString(R.string.text_save_image_succses), Toast.LENGTH_SHORT).show();

        mgr.enqueue(request);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == 122 && resultCode == Activity.RESULT_OK) {
            if (data == null || mConversationList == null || mConversationList.size() == 0) return;

            int position = data.getIntExtra(Util.getIntentName("position"), 0);
            Conversation rconv = data.getParcelableExtra(Util.getIntentName("question"));
            Conversation conv = mConversationList.get(position);

            mConversationList.get(position).totalResponses = rconv.totalResponses;
            mConversationList.get(position).totalVotes = rconv.totalVotes;
            mConversationList.get(position).isVoted = rconv.isVoted;

            conv.totalResponses = rconv.totalResponses;
            conv.totalVotes = rconv.totalVotes;
            conv.isVoted = rconv.isVoted;

            mAdapter.notifyDataSetChanged();
            mCacheDb.update(conv,"questions");
            Debug.i("On Result");

        }else if (requestCode == 121 && resultCode == Activity.RESULT_OK) {
           onRefresh();
        }
    }
    public void ButtonActionMenu(View root) {

        //------------------FLOATING ACTION BUTTON-------------------<<
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.fab_ripple_color));

        viewFloatingBt = (View) root.findViewById(R.id.view);
        menuMultipleActions = (FloatingActionsMenu) root.findViewById(R.id.multiple_actions);
        actionA = (FloatingActionButton1) root.findViewById(R.id.action_a);
        actionA.setVisibility(View.GONE);
        actionB = (FloatingActionButton1) root.findViewById(R.id.action_b);

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

        actionA.setIcon(R.drawable.ic_share);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isLogin.equals("")||user.isLogin.equals("null")){
                    Toast.makeText(getActivity(),getString(R.string.text_please_login_first),Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), PostQuestionAcivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,121);
                }
            }
        });

        actionB.setIcon(R.drawable.ic_tanya);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isLogin.equals("")||user.isLogin.equals("null")){
                    Toast.makeText(getActivity(),getString(R.string.text_please_login_first),Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), PostQuestionAcivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,121);
                }
            }
        });


    }

    @Override
    public void loadMore() {
        super.loadMore();

        loadTask3();
    }



    private void profileUser(View v, final int pos) {
        final Conversation conversation = mConversationList.get(pos);
        Intent intent = new Intent(getActivity(), Profile_Activity_Other_User.class);
        intent.putExtra(Util.getIntentName("position"), pos);
        intent.putExtra(Util.getIntentName("conversation"), conversation);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void likeQuestionHome(View v, final int pos) {
        final Conversation conversation = mConversationList.get(pos);
        idLikeQuestion = conversation.conversationId;

        if(user.isLogin.equals("")||user.isLogin.equals("null")){
            Toast.makeText(getActivity(),getString(R.string.text_login_before),Toast.LENGTH_SHORT).show();
        }else {
            LikeQuestionHome(conversation,v);
        }


    }

    private void moreQuestionHome(View v, final int pos) {
        final Conversation conversation = mConversationList.get(pos);
        idLikeQuestion = conversation.conversationId;


    }

    private void unLikeQuestionHome(View v, final int pos) {
        final Conversation conversation = mConversationList.get(pos);
        idLikeQuestion = conversation.conversationId;
        if(user.isLogin.equals("")||user.isLogin.equals("null")){
            Toast.makeText(getActivity(),getString(R.string.text_login_before),Toast.LENGTH_SHORT).show();
        }else {
            new unLikeQuestionHome(pos).execute();
            goodView.setText("-1");
            goodView.show(v);
        }
    }

    private void updateList(ConversationWrapper wrapper) {
        showList();
        //showFooterView(true);
        final int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new QuestionAdapterr(getActivity(), iduser);

                mAdapter.likeQuestion(new QuestionAdapterr.LikeQuestion() {

                    @Override
                    public void OnLikeClickQuestion(View view, int position) {

                        // TODO Auto-generated method stub
                        if (mConversationList.get(position).isVoted == 1) {
                            mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes;
                            vote = mConversationList.get(position).isVoted = 0;
                            unLikeQuestionHome(view, position);

                        } else if (mConversationList.get(position).isVoted == 0) {
                            mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes;
                            vote = mConversationList.get(position).isVoted = 1;
                            likeQuestionHome(view, position);
                        }
                    }
                });

                mAdapter.likeArticle(new QuestionAdapterr.LikeArticle() {

                    @Override
                    public void OnLikeClickArticle(View view, int position) {
                        // TODO Auto-generated method stub
                        if (mConversationList.get(position).isVoted == 1) {
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


                mAdapter.profileUser(new QuestionAdapterr.ProfileUser() {
                    public void OnClickProfile(View view, int position) {

                        mConversationList.get(position);
                        profileUser(view, position);
                    }
                });

                mAdapter.moreQuestion(new QuestionAdapterr.MoreQuestion() {
                    public void OnMoreClickQuestion(View view, int position) {
                        if (mConversationList.get(position).user.userId.equals(iduser)||user.email.equals("fikri@fupei.com") && mConversationList.get(position).attachment.equals("")) {
                            showOptionDialogjustDelete(view, position);
                        } else if (mConversationList.get(position).user.userId.equals(iduser) && mConversationList.get(position).attachment.equals(mConversationList.get(position).attachment)) {
                            showOptionDialog(view, position);
                        } else if(mConversationList.get(position).user.userId != iduser && mConversationList.get(position).attachment.equals("")){
                            copyContent(view,position);
                        } else if(mConversationList.get(position).user.userId != iduser &&mConversationList.get(position).attachment.equals("")){
                            copyContent(view, position);
                        }
                    }
                });

                mAdapter.pindahHalaman(new QuestionAdapterr.PindahHalaman() {
                    public void OnLikeClickPindahHalaman(View view, int position) {
                        final Conversation articles = mConversationList.get(position);
                        if (user.isLogin.equals("") || user.isLogin.equals("null")) {
                            showDialogLogin(getString(R.string.text_login_first));
                        } else {
                            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                            intent.putExtra(Util.getIntentName("position"), position);
                            intent.putExtra(Util.getIntentName("question"), articles);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivityForResult(intent,122);
                        }
                    }
                });

                mAdapter.setData(mConversationList);
                mAdapter.notifyDataSetChanged();
                mQuestionLv.setAdapter(mAdapter);
                if (mListSize != 0) {
                    scrollMyListViewToBottom(0 + (mConversationList.size() - mListSize));
                }
                mIsRefresh = false;
                return;
            }
            mConversationList.add((Conversation) wrapper.list.get(j));
            Debug.i((new StringBuilder("ID ")).append(((Conversation) mConversationList.get(j)).conversationId).append(" pos ").append(String.valueOf(j)).toString());
            j++;
        } while (true);
    }


    @Override
    public void onListItemClick(final int position) {
        final Conversation conversation = mConversationList.get(position);
        Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
        intent.putExtra(Util.getIntentName("position"), position);
        intent.putExtra(Util.getIntentName("question"), conversation);
        startActivity(intent);
    }

    private void showOptionDialog(final View view, final int position) {
        String[] options;
        final Conversation conversation = mConversationList.get(position);

        options = new String[]{getString(R.string.text_save_image), getString(R.string.text_delete)};

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                    getBitmapFromURL(conversation.attachment);
                } else if (arg1 == 1) {
                    showDialogDelete(conversation);
                }
            }
        });

        builder.create().show();
    }


    private void copyContent(final View view, final int position) {
        String[] options;
        final Conversation conversation = mConversationList.get(position);

        options = new String[]{getString(R.string.text_copy_url)};

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                    copyLinkImage(conversation.content);
                }
            }
        });

        builder.create().show();
    }

    private void showOptionDialogjustDelete(final View view, final int position) {
        String[] options;
        final Conversation conversation = mConversationList.get(position);

        options = new String[]{ getString(R.string.text_delete)};

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                    showDialogDelete(conversation);
                }
            }
        });

        builder.create().show();
    }



    private void showOptionDialogOtherUser(final View view, final int position) {
        String[] options;
        final Conversation conversation = mConversationList.get(position);

        options = new String[]{getString(R.string.text_save_image)};

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                    getBitmapFromURL(conversation.attachment);
                }
            }
        });

        builder.create().show();
    }



    private void showDialogDelete(final Conversation conversation) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_info, null);

        TextView messageTv = (TextView) view.findViewById(R.id.tv_message);

        messageTv.setText(getActivity()
                .getString(R.string.text_confirm_delete));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

        builder.setView(view)
                .setNegativeButton(getString(R.string.text_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.text_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(conversation);
                            }
                        });

        builder.create().show();
    }

    public void delete(final Conversation conversation) {
        String url = Cons.CONVERSATION_URL + "/deleteQuestion.php?conversationId=" + conversation.conversationId;
        StringRequest jsonRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), getString(R.string.text_question_deleted_success), Toast.LENGTH_SHORT).show();
                if (mCacheDb.exist(conversation.conversationId)) {
                    mCacheDb.delete(conversation.conversationId);

                }
                (mCacheTask = new LoadCacheTask()).execute();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),getString(R.string.text_question_deleted_failed), Toast.LENGTH_SHORT).show();
                VolleyLog.e("PostComment", "Error" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> mparams = new HashMap<String, String>();

                mparams.put(Cons.CONV_ID, conversation.conversationId);
                return mparams;
            }
        };

        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }

    @Override
    public void onRefresh() {

        mIsRefresh = true;
        page = 0;
        loadTask2();

    }



    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        ConversationWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
           // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                wrapper = mCacheDb.getListQuestion("questions");
                result 	= 1;
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
                    doneLoading();

                    mConversationList = new ArrayList<Conversation>();

                    updateList(wrapper);

                    //showToast("From db");
                } else {
                    Debug.i("Cache not found");
                }
            } else {
                   Debug.i("Cache not found");
            }

            refresh();
        }
    }


    public void loadTask2() {
        startCountTime();
        page = 1;
        String url = Cons.CONVERSATION_URL + "/listQuestion.php?userId=" + iduser + "&page=" + page + "&count=20";
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        ConversationWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.conversationParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mConversationList = new ArrayList<Conversation>();
                                    }
                                    updateList(wrapper);
                                    mCacheDb.updateQuestionList(wrapper.list,"questions");
                                }
                            } else {
                                showEmpty(getString(R.string.text_download_failed));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimerPage.cancel();
                        try {
                            // actionTracking("getHomeList", "homeList", "failed", String.valueOf(totalTimeSec), user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        swipContainer.setRefreshing(false);
                        VolleyLog.d("Response Error", "Error" + error.getMessage());
                        showEmpty(getString(R.string.text_download_failed));
                        errorHandle(error);
                    }
                });

        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(mRequest);
    }


    public void loadTask3() {
        startCountTime();
        page += 1;
        String url = Cons.CONVERSATION_URL + "/listQuestion.php?userId=" + iduser + "&page=" + page + "&count=20";
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        ConversationWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.conversationParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mConversationList = new ArrayList<Conversation>();
                                    }
                                    updateList(wrapper);
                                }
                            } else {
                                showEmpty(getString(R.string.text_download_failed));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimerPage.cancel();
                        try {
                            // actionTracking("getHomeList", "homeList", "failed", String.valueOf(totalTimeSec), user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        swipContainer.setRefreshing(false);
                        VolleyLog.d("Response Error", "Error" + error.getMessage());
                        showEmpty(getString(R.string.text_download_failed));
                        errorHandle(error);
                    }
                });

        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(mRequest);
    }


    public void LikeQuestionHome (final Conversation conversation,final View v) {
        String url = Cons.CONVERSATION_URL+"/postVote.php";
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        conversation.totalVotes = conversation.totalVotes + 1;
                        Toast.makeText(getActivity(), getString(R.string.like), Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                        goodView.setText("+1");
                        goodView.show(v);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),getString(R.string.text_question_deleted_failed), Toast.LENGTH_SHORT).show();
                VolleyLog.e("PostComment", "Error" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> mparams = new HashMap<String, String>();

                mparams.put(Cons.CONV_ID, idLikeQuestion);
                mparams.put(Cons.KEY_ID,user.userId);
                return mparams;
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonRequest);
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

            Toast.makeText(getActivity(), "Batal Menyukai", Toast.LENGTH_SHORT).show();

        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            try {
                QuestionConnection conn = new QuestionConnection();

                conn.unlike(idLikeQuestion, user.userId);

                result = 1;
            } catch (WisataException e) {
                e.printStackTrace();
                error = e.getError();
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
                mConversationList.get(position).totalVotes = mConversationList.get(position).totalVotes - 1;
                // mCacheDb.updateLike(mConversationList.get(position));
                mAdapter.notifyDataSetChanged();
            } else {
                error = (error.equals("")) ? getString(R.string.text_gagal_like) : error;
                mConversationList.get(position).isVoted = 1;

                showError(error);

            }
        }



    }
}