package asywalul.minang.wisatasumbar.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.URL;
import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.ArticlesAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.database.ConversationDb;
import asywalul.minang.wisatasumbar.database.QuestionDb;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.Articles;
import asywalul.minang.wisatasumbar.model.ArticlesWrapper;
import asywalul.minang.wisatasumbar.model.ConversationWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.ArticleDetailActivity;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class ArticlesFragment extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener {
    // inisial buat user interface
    private ExpandableHeightListView mQuestionLv;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private View viewFloatingBt;
    private boolean mIsLoading = false;

    // inisial database dan cache
    private ConversationDb mConvDb;
    private QuestionDb mQuestionDb;
    private CacheDb mCacheDb;
    private UpdateCacheTask mUpdateCacheTask;
    private LoadCacheTask mCacheTask;

    //inisial adapter
    private ArticlesAdapter mAdapter;
    private ConversationWrapper wrapper;
    private ArrayList<Articles> mArticlesList = new ArrayList<Articles>();
    private int mPage = 1;

    //variabel digunakan
    private String idLikeQuestion;
    private String iduser;
    private int page = 1;
    private int vote = 0;
    private static final int REQ_CODE = 1000;
    private Intent intent;
    private String API =  "/listArticles.php?page=" + page + "&count=20";
    private String urutan = "&urutan=DESC";

    User user;

    //kategori
    private int[] groupChoice = {0, 0, 0};
    private int mCurrentGrouping = 0;


    //shared Preference
    public final static String SP = "sharedAt";

    private AdView mAdView;

  //  TextView mKategori, mContent;
    private LinearLayout llkategori;
    private LinearLayout lltime;


    public static ArticlesFragment newInstance() {
        return new ArticlesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_articles, container, false);
        mCacheDb = new CacheDb(getDatabase());

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        user = getUser();
        mAdView             = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        page = 1;
        mLoadingLayout      = (LoadingLayout) root.findViewById(R.id.layout_loading);
        swipContainer       = (SwipeRefreshLayout) root.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

     //   mKategori           = (TextView) root.findViewById(R.id.tv_kategori);
     //   mKategori.setText(getString(R.string.text_kategori_semua));
     //   mContent            = (TextView) root.findViewById(R.id.tv_konten);
        llkategori          = (LinearLayout)root.findViewById(R.id.ll_kategori);
        lltime              = (LinearLayout)root.findViewById(R.id.lltime);
     //   mContent            = (TextView)root.findViewById(R.id.tv_konten);

        llkategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGroupOptions();
            }
        });

        lltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeOptions();

            }
        });

        mLoadingLayout = (LoadingLayout) root.findViewById(R.id.layout_loading);

        mQuestionLv = (ExpandableHeightListView) root.findViewById(R.id.lv_data);
        mQuestionLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING
                        && (mQuestionLv.getLastVisiblePosition() - mQuestionLv.getHeaderViewsCount() -
                        mQuestionLv.getFooterViewsCount()) >= (mAdapter.getCount() - 1)) {
                    loadTask2(API);

                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


      /*  mQuestionLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) { // TODO Auto-generated method stub
                int threshold = 1;
                int count = mQuestionLv.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (mQuestionLv.getLastVisiblePosition() >= count
                            - threshold) {
                        // Execute LoadMoreDataTask AsyncTask
                        ++ page;
                        loadTask2(API);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }

        });*/

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
        loadTask2(API);
    }


    private void showTimeOptions() {
        String[] options = {getString(R.string.text_artikel_baru), getString(R.string.text_artikel_lama)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.text_urutkan));
        builder.setSingleChoiceItems(options, mCurrentGrouping, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String optionMenu = String.valueOf(which);
                if (optionMenu.equals("0")) {
                    urutan =  "&urutanDESC";
                 //   mContent.setText(getString(R.string.text_artikel_baru));
                    mCurrentGrouping = 0;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("1")) {
                    urutan = "&urutan=ASC";
                  //  mContent.setText(getString(R.string.text_artikel_lama));
                    mCurrentGrouping = 1;
                    onRefresh();
                    dialog.dismiss();

                }
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

    private void showGroupOptions() {
        String[] options = {getString(R.string.text_kategori_semua), getString(R.string.text_kategori_wisata),
                           getString(R.string.text_kategori_budaya),getString(R.string.text_kategori_kuliner)};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.text_kategori));
        builder.setSingleChoiceItems(options, mCurrentGrouping, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String optionMenu = String.valueOf(which);
                if (optionMenu.equals("0")) {
                    API =  "/listArticles.php?page=" + page + "&count=20"+urutan;
                  //  mKategori.setText(getString(R.string.text_kategori_semua));
                    mCurrentGrouping = 0;
                    onRefresh();
                    dialog.dismiss();


                } else if (optionMenu.equals("1")) {
                    API =  "/listArticlesKategori.php?category=wisata&&page="+page +"&count=20"+urutan;
                   // mKategori.setText(getString(R.string.text_kategori_wisata));
                    mCurrentGrouping = 1;
                    onRefresh();
                    dialog.dismiss();

                } else if (optionMenu.equals("2")) {
                    API =  "/listArticlesKategori.php?category=budaya&&page="+page+  "&count=20"+urutan;
                  //  mKategori.setText(getString(R.string.text_kategori_budaya));
                    mCurrentGrouping = 2;
                    onRefresh();
                    dialog.dismiss();

                } else {
                    API =  "/listArticlesKategori.php?category=kuliner&&page=" + page + "&count=20"+urutan;
                  //  mKategori.setText(getString(R.string.text_kategori_kuliner));
                    mCurrentGrouping = 3;
                    onRefresh();
                    dialog.dismiss();

                }
                dialog.dismiss();

            }
        });
        builder.create().show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.i("On Result");

        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || mArticlesList == null || mArticlesList.size() == 0) return;

            int position = data.getIntExtra(Util.getIntentName("position"), 0);
            Articles articless = data.getParcelableExtra(Util.getIntentName("articles"));
            Articles articles = mArticlesList.get(position);
            mAdapter.notifyDataSetChanged();
            mCacheDb.update(articles, "articles-" + iduser);
            Debug.i("On Result");

        }
    }



    private void updateList(ArticlesWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new ArticlesAdapter(getActivity(), iduser);

                mAdapter.pindahHalaman(new ArticlesAdapter.PindahHalaman() {
                    public void OnLikeClickPindahHalaman(View view, int position) {
                        final Articles articles = mArticlesList.get(position);
                        if (user.isLogin.equals("") || user.isLogin.equals("null")) {
                            showDialogLogin(getString(R.string.text_login_first));
                        } else {
                            Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                            intent.putExtra(Util.getIntentName("position"), position);
                            intent.putExtra(Util.getIntentName("articles"), articles);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }
                });
                mAdapter.pindahHalaman2(new ArticlesAdapter.PindahHalaman2() {
                    public void OnLikeClickPindahHalaman2(View view, int position) {
                        final Articles articles = mArticlesList.get(position);
                        if (user.isLogin.equals("") || user.isLogin.equals("null")) {
                            showDialogLogin(getString(R.string.text_login_first));
                        } else {
                            Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                            intent.putExtra(Util.getIntentName("position"), position);
                            intent.putExtra(Util.getIntentName("articles"), articles);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    }
                });


                mAdapter.setData(mArticlesList);
                mAdapter.notifyDataSetChanged();
                mQuestionLv.setAdapter(mAdapter);
                if (mListSize != 0) {
                    scrollMyListViewToBottom(1 + (mArticlesList.size() - mListSize));
                }
                mIsRefresh = false;
                return;
            }
            mArticlesList.add((Articles) wrapper.list.get(j));
            Debug.i((new StringBuilder("ID ")).append(((Articles) mArticlesList.get(j)).articlesId).append(" pos ").append(String.valueOf(j)).toString());
            j++;
        } while (true);
    }


    @Override
    public void onListItemClick(final int position) {
        final Articles articles = mArticlesList.get(position);
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra(Util.getIntentName("position"), position);
        intent.putExtra(Util.getIntentName("articles"), articles);
        startActivity(intent);
    }




    @Override
    public void onRefresh() {

        mIsRefresh = true;
        page = 1;
        loadTask2(API);

    }

    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        ArticlesWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {


                wrapper = mCacheDb.getListArticles("articles");
                Debug.i("Checking cache " + "articles");
                Log.d("isicache",String.valueOf(wrapper));
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
                    doneLoading();
                    mArticlesList = new ArrayList<Articles>();
                    if(isAdded()) {
                        updateList(wrapper);
                    }
                } else {
//                    showFormField();
                    Debug.i("Cache artikel not found");
                }
            } else {
//                showFormField();
                Debug.i("Cache artikel2 not found");
            }

            refresh();
        }
    }

    public class UpdateCacheTask extends AsyncTask<URL, Integer, Long> {
        ArticlesWrapper wrapper;

        public UpdateCacheTask(ArticlesWrapper wrapper) {
            this.wrapper = wrapper;
        }

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            Debug.i("Update Cache");
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                mCacheDb.updateArticlesList(wrapper.list, "articles");
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

    public void loadTask2(String API) {
        startCountTime();

        String url = Cons.CONVERSATION_URL + API;
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        ArticlesWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.articlesParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mArticlesList = new ArrayList<Articles>();
                                    }
                                    updateList(wrapper);
                                    (mUpdateCacheTask = new UpdateCacheTask(wrapper)).execute();
                                }
                            } else {
                                showEmpty(getString(R.string.text_no_data));
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mRequest);
    }


    /*public void loadTask3(String API) {
        startCountTime();
        page += 1;
        String url = Cons.CONVERSATION_URL + "/listArticles.php?page=" + page + "&count=20"+urutan;
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        ArticlesWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.articlesParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mArticlesList = new ArrayList<Articles>();
                                    }
                                    updateList(wrapper);
                                    (mUpdateCacheTask = new UpdateCacheTask(wrapper)).execute();
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mRequest);
    }*/
    }
