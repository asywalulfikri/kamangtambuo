package asywalul.minang.wisatasumbar.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.DaerahAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.http.DaerahConnection;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.Daerah;
import asywalul.minang.wisatasumbar.model.DaerahWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.SimpleDividerItemDecoration;

/**
 * Created by asywalulfikri on 1/20/17.
 */

public class KabubatenKotaListActivity extends BaseActivity implements DaerahAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener  {

    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadingLayout;
    private ArrayList<Daerah> mDaerahList = new ArrayList<Daerah>();
    protected SwipeRefreshLayout swipContainer;
    private static final int REQ_CODE = 1000;
    private LoadTask mLoadTask;
    private CacheDb cacheDb;
    private LoadCacheTask mCacheTask;
    private boolean mIsLoading = false;
    protected boolean mIsRefresh = false;
    DaerahWrapper wrapper;
    private Toolbar toolbar;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_store);
        cacheDb = new CacheDb(getDatabase());
        initDatabase();
        mIsRefresh = true;

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Kabupaten/Kota");
        ApplyFontToolbar();

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //or whatever you used to do on your onOptionItemSelected's android.R.id.home callback
            }
        });

        mRecyclerView  = (RecyclerView)findViewById(R.id.rv_browse_store);
        mLoadingLayout = (LoadingLayout)findViewById(R.id.layout_loading);
        swipContainer  = (SwipeRefreshLayout)findViewById(R.id.ptr_layout);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //(mLoadTask = new LoadTask(false)).execute();
        loadTask2();
        parsingData();

    }

    public void ApplyFontToolbar() {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/wwf-webfont.ttf");
                if (tv.getText().equals(toolbar.getTitle())) {
                    tv.setTypeface(custom_font);
                    tv.setTextSize(25);
                    tv.setTextColor(Color.parseColor("#FFFFFF"));
                    break;
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {

        if (mIsLoading && mCacheTask != null) {
            mCacheTask.cancel(true);
        }

        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();

        cacheDb.reload(getDatabase());

        if (!isListVisible()) {
            (mCacheTask = new LoadCacheTask()).execute();
        }
    }


    protected boolean isListVisible() {
        return (mRecyclerView.getVisibility() == View.VISIBLE) ? true : false;
    }

    protected void showList() {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    protected void showEmpty(String message) {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mLoadingLayout.hideAndEmpty(message, false);
        }
    }

    private void updateView(final DaerahWrapper wrapper) {
        showList();

        DaerahAdapter mAdapter = new DaerahAdapter(wrapper.list,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(wrapper.list);
        mAdapter.moreCerita(new DaerahAdapter.MoreCerita() {
            @Override
            public void OnMoreClickCerita(final  View view, final  int position) {
                Daerah daerah = wrapper.list.get(position);

                Intent intent = new Intent(getActivity(), ListWisataPerKabupaten.class);
                intent.putExtra("kabupaten",daerah.idk);
                startActivityForResult(intent, REQ_CODE);
            }
        });
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onItemClick(View view, int position) {
        Daerah daerah = mDaerahList.get(position);
        Intent intent = new Intent(getActivity(), ListWisataPerKabupaten.class);
        intent.putExtra("kabupaten",daerah.idk);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    public void onRefresh() {
        (mLoadTask = new LoadTask(false)).execute();
    }

    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        DaerahWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {


                wrapper = cacheDb.getListKabupaten("kabupaten");
                Debug.i("wrappernya",String.valueOf(wrapper));
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
                    updateView(wrapper);

                    mDaerahList = new ArrayList<Daerah>();

                    updateView(wrapper);
                } else {
//                    showFormField();
                    Debug.i("Cache not found");
                }
            } else {
//                showFormField();
                Debug.i("Cache not found");
            }


        }
    }

    public class LoadTask extends AsyncTask<URL, Integer, Long> {
        boolean loadMore;

        protected LoadTask(boolean loadMore) {
            this.loadMore = loadMore;
        }

        @Override
        protected void onPreExecute() {
//            showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {

                DaerahConnection conn = new DaerahConnection();
                DaerahWrapper wrapper = conn.getList();
                cacheDb.updateDaerahList(wrapper.list,"kabupaten");
                result = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
//            if (isAdded()) {
//            doneLoading();
            swipContainer.setRefreshing(false);

            if (result == 1) {
                Log.e("DDD", "DISINI1");
                if (wrapper != null) {
                    if (wrapper.list.size() == 0) {
                        Log.e("DDD", "DISINI2");
                        showEmpty(getActivity().getString(R.string.text_no_data));
                    } else {
                        if (loadMore) {
                            Log.e("DDD", "DISINI3");
//                                loadMoreView(wrapper);
                        } else {
                            Log.e("DDD", "DISINI4");
                            mDaerahList = new ArrayList<Daerah>();
                            updateView(wrapper);

                        }
                        for (int i = 0; i < wrapper.list.size(); i++) {
                            mDaerahList.add(wrapper.list.get(i));
                        }
//                            mPage = mPage + 1;
                        showList();

//                            (mUpdateCacheTask = new UpdateCacheTask(wrapper)).execute();
                    }
                } else {
                    showEmpty(getActivity().getString(R.string.text_no_data));
                }
            } else {
                showEmpty(getActivity().getString(R.string.text_no_data));
            }
        }
    }


    public void loadTask2() {
        startCountTime();
        String url = Cons.CONVERSATION_URL+ "ListKabupaten/kabupaten";
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        DaerahWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.daerahParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mDaerahList = new ArrayList<Daerah>();
                                    }
                                    updateView(wrapper);
                                    cacheDb.updateDaerahList(wrapper.list,"kabupaten");
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

    public void parsingData(){
        String url = "http://183.91.67.198:7079/inquiryData?AgentID=1234567890&AgentPIN=1234567890&AgentTrxID=151028210390&AgentStoreID=idopay1&ProductID=4000&CustomerID=8888801261782314&DateTimeRequest=20150224163407&Signature=3d920c48b329c3b9802ed29fa7407e8941f1fab9";
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] data = response.split("|",11);
                        List<String> splitList = new ArrayList<String>(Arrays.asList(data));



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Response Error", "Error" + error.getMessage());

                    }
                });

        mRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(KabubatenKotaListActivity.this);
        queue.add(mRequest);
    }


}