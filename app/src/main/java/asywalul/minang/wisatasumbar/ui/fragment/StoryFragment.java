package asywalul.minang.wisatasumbar.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.StoryAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.database.ConversationDb;
import asywalul.minang.wisatasumbar.database.QuestionDb;
import asywalul.minang.wisatasumbar.http.StoryConnection;
import asywalul.minang.wisatasumbar.model.Story;
import asywalul.minang.wisatasumbar.model.StoryWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.FloatingActionButton1;
import asywalul.minang.wisatasumbar.widget.FloatingActionsMenu;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.MyGridLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.URL;
import java.util.ArrayList;

import asywalul.minang.wisatasumbar.widget.SimpleDividerItemDecoration;


/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class StoryFragment extends BaseListFragment implements StoryAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    // inisial buat user interface
    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private RelativeLayout rlLoadMore;
    private ProgressBar progressBar;
    private TextView tvLoadMore;
    private View footerView;
    private FloatingActionsMenu menuMultipleActions;
    private FloatingActionButton1 actionA,actionB;
    private View viewFloatingBt;
    private boolean mIsLoading = false;

    // inisial database dan cache
    private ConversationDb mConvDb;
    private QuestionDb mQuestionDb;
    private CacheDb mCacheDb;
    private UpdateCacheTask mUpdateCacheTask;
    private LoadCacheTask mCacheTask;

    //ekseskusi pertama
    private LoadTask mLoadTask;

    //inisial adapter
    private StoryAdapter mAdapter;
    private StoryWrapper wrapper;
    private ArrayList<Story> mStoryList = new ArrayList<Story>();
    private int mPage = 0;

    //variabel digunakan
    private String idLikeQuestion;
    private String iduser;
    private int offSet = 0;
    private int vote = 0;
    private static final int REQ_CODE = 1000;

    private AdView mAdView;
    //shared Preference
    public final static String SP = "sharedAt";


    public static StoryFragment newInstance() {
        return new StoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root   = inflater.inflate(R.layout.fragment_story, container, false);
        mCacheDb	= new CacheDb(getDatabase());
        mConvDb		= new ConversationDb(getDatabase());
        mQuestionDb	= new QuestionDb(getDatabase());

        mAdView = (AdView)root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        SharedPreferences getId = getActivity()
                .getSharedPreferences(SP, 0);
        iduser = getId.getString(Cons.KEY_ID, "Nothing");

        mLoadingLayout = (LoadingLayout)root.findViewById(R.id.layout_loading);
        swipContainer  = (SwipeRefreshLayout)root.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

        RecyClearView(root);      // UNTUK MEMANGGIL FUNGSI RECYCLERVIEW

        return root;
    }


    @Override
    public void onPause() {
        if (mIsLoading && mUpdateCacheTask != null) {
            mUpdateCacheTask.cancel(true);
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

        if (!isListVisible()) {
//			loadData();
            (mCacheTask = new LoadCacheTask()).execute();
        }
    }

    @Override
    public void loadData() {
        (mLoadTask = new LoadTask()).execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.i("On Result");

        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || mStoryList == null || mStoryList.size() == 0) return;

            int position = data.getIntExtra(Util.getIntentName("position"), 0);
            Story rconv = data.getParcelableExtra(Util.getIntentName("video"));
            Story conv = mStoryList.get(position);

           // mVideoList.get(position).totalView = rconv.totalView;
            // mVideoList.get(position).totalVotes = rconv.totalVotes;
            // mVideoList.get(position).isVoted = rconv.isVoted;

            //conv.totalView = rconv.totalView;
            // conv.totalVotes = rconv.totalVotes;
            // conv.isVoted = rconv.isVoted;

            mAdapter.notifyDataSetChanged();
            mCacheDb.update(conv, "story-" +iduser);
            Debug.i("On Result");

        }
    }


    public void RecyClearView(View root){
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView  = (RecyclerView)root.findViewById(R.id.lv_data);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager.setSpanSizeLookup(new MyGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((mAdapter.hasHeader() && mAdapter.isHeader(position)) ||
                        mAdapter.hasFooter() && mAdapter.isFooter(position))
                    return mLayoutManager.getSpanCount();

                return 1;
            }
        });


        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);
        footerView.setVisibility(View.GONE);
        rlLoadMore = (RelativeLayout) footerView.findViewById(R.id.rl_load_more);
        progressBar = (ProgressBar) footerView.findViewById(R.id.pb_progress);
        tvLoadMore = (TextView) footerView.findViewById(R.id.tv_loading);
        rlLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                tvLoadMore.setText(getString(R.string.text_loading));
                if(mPage == offSet ){
                    offSet = mPage+1;
                }
                // (mLoadTask2 = new LoadTask2(true)).execute();
            }
        });
        //  (mLoadTask = new LoadTask(false)).execute();
    }





    protected boolean isListVisible() {
        return (mRecyclerView.getVisibility() == View.VISIBLE) ? true : false;
    }


    private void updateView( StoryWrapper wrapper) {
//
        progressBar.setVisibility(View.GONE);
        tvLoadMore.setText(getString(R.string.text_next));

        mAdapter = new StoryAdapter(getActivity(),iduser);
        mAdapter.setData(wrapper.list);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setFooterView(footerView);

        mAdapter.setClickListener(this);
        mAdapter.notifyDataSetChanged();


    }

    private void loadMoreView ( StoryWrapper wrapper) {
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

   /* @Override
    public void onItemClick(View view, int position) {
        Video video = mVideoList.get(position);

        if (buildVersion()) {
            Intent intent     = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra(Util.getIntentName("position"), position);
            intent.putExtra(Util.getIntentName("question"), video);

            // this for animation sharedElements
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    new Pair<View, String>(view.findViewById(R.id.layout_questions), getString(R.string.shared_element_image_cover)));
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        } else {
            Intent intent     = new Intent(getActivity(), QuestionDetailActivity.class);
            intent.putExtra(Util.getIntentName("position"), position);
            intent.putExtra(Util.getIntentName("question"), video);
            startActivity(intent);
        }
*/

//    }



    @Override
    public void onRefresh() {

        mIsRefresh = true;
        mPage = 1;
        (mLoadTask = new LoadTask()).execute();

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    //    protected void showLoading() {
//        mRecyclerView.setVisibility(View.GONE);
//        mLoadingLayout.setVisibility(View.VISIBLE);
//    }
//
//    protected void doneLoading() {
//        mRecyclerView.setVisibility(View.VISIBLE);
//        mLoadingLayout.setVisibility(View.GONE);
//    }
//


    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        StoryWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {


                wrapper = mCacheDb.getListStory("question-"+iduser);
                Debug.i("Checking cache " + "video-" + iduser);
                result = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            //doneLoading();
            if (wrapper != null) {
                if (wrapper.list.size() > 0) {
                    showList();

                    mStoryList = new ArrayList<Story>();

                    updateView(wrapper);
                } else {
//                    showFormField();
                    Debug.i("Cache not found");
                }
            } else {
//                showFormField();
                Debug.i("Cache not found");
            }

            refresh();
        }
    }

    public class UpdateCacheTask extends AsyncTask<URL, Integer, Long> {
        StoryWrapper wrapper;

        public UpdateCacheTask( StoryWrapper wrapper) {
            this.wrapper = wrapper;
        }

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            //Debug.i("Update Cache");
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                mCacheDb.updateStoryList(wrapper.list, "home-" + iduser);
                result 	= 1;
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

    public class LoadTask extends AsyncTask<URL, Integer, Long> {
        boolean loadMore;

        protected void onCancelled() {
            //  doneLoading();
        }

        protected void onPreExecute() {
            //  showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;
            SharedPreferences getId = getActivity()
                    .getSharedPreferences(SP, 0);
            String iduser = getId.getString(Cons.KEY_ID, "Nothing");


            try {

                StoryConnection conn = new StoryConnection();
                wrapper = conn.getList();
                result = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            if (isAdded()) {
//            doneLoading();
                swipContainer.setRefreshing(false);

                if (result == 1) {
                    Log.e("DDD", "DISINI1");
                    if (wrapper != null) {
                        if (wrapper.list.size() == 0) {
                            Log.e("DDD", "DISINI2");
                            showClearEmpty(getActivity().getString(R.string.text_no_data));
                        } else {
                            if (loadMore) {
                                Log.e("DDD", "DISINI3");
                                loadMoreView(wrapper);
                            } else {
                                Log.e("DDD", "DISINI4");
                                mStoryList = new ArrayList<Story>();
                                updateView(wrapper);

                            }
                            for (int i = 0; i < wrapper.list.size(); i++) {
                                mStoryList.add(wrapper.list.get(i));
                            }
                            mPage = mPage + 1;
                            showList();
                            updateView(wrapper);
                            showList();
                            (mUpdateCacheTask= new UpdateCacheTask(wrapper)).execute();
                        }
                    } else {
                        showClearEmpty(getActivity().getString(R.string.text_no_data));
                    }
                } else {
                    showClearEmpty(getActivity().getString(R.string.text_no_data));
                }
            }
        }
    }




    // ini untuk menyimpan link image
    public void copyLinkImage(String path){
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
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


}