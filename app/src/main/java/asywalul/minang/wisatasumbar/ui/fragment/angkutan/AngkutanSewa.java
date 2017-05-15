package asywalul.minang.wisatasumbar.ui.fragment.angkutan;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import asywalul.minang.wisatasumbar.adapter.AngkutanAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.Angkutan;
import asywalul.minang.wisatasumbar.model.AngkutanWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.ArticleDetailActivity;
import asywalul.minang.wisatasumbar.ui.fragment.BaseListFragment;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.OnSwipeTouchListener;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class AngkutanSewa extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener {
    // inisial buat user interface
    private ExpandableHeightListView mQuestionLv;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private View viewFloatingBt;
    private boolean mIsLoading = false;

    // inisial database dan cache
    private CacheDb mCacheDb;
    private UpdateCacheTask mUpdateCacheTask;
    private LoadCacheTask mCacheTask;

    //inisial adapter
    private AngkutanAdapter mAdapter;
    private AngkutanWrapper wrapper;
    private ArrayList<Angkutan> mAngkutanList = new ArrayList<Angkutan>();
    private int mPage = 1;

    //variabel digunakan
    private String idLikeQuestion;
    private String iduser;
    private int page = 1;
    private int vote = 0;
    private static final int REQ_CODE = 1000;
    private Intent intent;
    User user;

    //kategori
    private int[] groupChoice = {0, 0, 0};
    private int mCurrentGrouping = 0;


    //shared Preference
    public final static String SP = "sharedAt";

    private AdView mAdView;

    TextView mKategori, mContent;
    private LinearLayout llkategori;
    private String type = "AS";


    public static AngkutanSewa newInstance() {
        return new AngkutanSewa();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_list_angkutan, container, false);
        mCacheDb = new CacheDb(getDatabase());

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        user = getUser();
        mAdView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        mLoadingLayout = (LoadingLayout) root.findViewById(R.id.layout_loading);
        swipContainer = (SwipeRefreshLayout) root.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

        mLoadingLayout = (LoadingLayout) root.findViewById(R.id.layout_loading);

        mQuestionLv = (ExpandableHeightListView) root.findViewById(R.id.lv_data);
        mQuestionLv.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onClick(View view, MotionEvent e) {
                int position = mQuestionLv.pointToPosition((int) e.getX(), (int) e.getY());
                final Angkutan articles = mAngkutanList.get(position);
                if (user.isLogin.equals("") || user.typeLogin.equals("null")) {
                    showDialogLogin(getString(R.string.text_login_first));
                } else {
                    Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                    intent.putExtra(Util.getIntentName("position"), position);
                    intent.putExtra(Util.getIntentName("articles"), articles);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

                super.onClick(view, e);
            }

            // your on onDoubleClick here
            @Override
            public void onDoubleClick(View view, MotionEvent e) {


                int position = mQuestionLv.pointToPosition((int) e.getX(), (int) e.getY());
                final Angkutan articles = mAngkutanList.get(position);


                super.onDoubleClick(view, e);
            }

        });


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
//			loadData();
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



    private void updateList(AngkutanWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new AngkutanAdapter(getActivity(), iduser);

                mAdapter.setData(mAngkutanList);
                mAdapter.notifyDataSetChanged();
                mQuestionLv.setAdapter(mAdapter);
                if (mListSize != 0) {
                    scrollMyListViewToBottom(1 + (mAngkutanList.size() - mListSize));
                }
                mIsRefresh = false;
                return;
            }
            mAngkutanList.add((Angkutan) wrapper.list.get(j));
            j++;
        } while (true);
    }


    @Override
    public void onListItemClick(final int position) {
        final Angkutan articles = mAngkutanList.get(position);
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra(Util.getIntentName("position"), position);
        intent.putExtra(Util.getIntentName("question"), articles);
        startActivity(intent);
    }




    @Override
    public void onRefresh() {

        mIsRefresh = true;
        page = 1;
        loadTask2();

    }

    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        AngkutanWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {


                wrapper = mCacheDb.getListAngkutan("angkutan-" + type);
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
                    updateList(wrapper);

                    mAngkutanList = new ArrayList<Angkutan>();

                    updateList(wrapper);
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
        AngkutanWrapper wrapper;

        public UpdateCacheTask(AngkutanWrapper wrapper) {
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
                mCacheDb.updateAngkutanList(wrapper.list, "angkutan-" + type);
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

    public void loadTask2() {
        startCountTime();
        String url = Cons.CONVERSATION_URL + "/listAngkutan.php?kategori="+type;
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        AngkutanWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.angkutanParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mAngkutanList = new ArrayList<Angkutan>();
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
    }

    }
