package asywalul.minang.wisatasumbar.ui.fragment.kuliner;

/**
 * Created by asywalulfikri on 10/5/16.
 */

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.WisataAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Wisata;
import asywalul.minang.wisatasumbar.model.WisataWrapper;
import asywalul.minang.wisatasumbar.ui.activity.WisataDetail;
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
public class FragmentMinuman extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener {
    // inisial buat user interface
    private ExpandableHeightListView mQuestionLv;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private View viewFloatingBt;
    private boolean mIsLoading = false;

    // inisial database dan cache
    private CacheDb mCacheDb;
    private LoadCacheTask mCacheTask;

    //inisial adapter
    private WisataAdapter mAdapter;
    private WisataWrapper wrapper;
    private ArrayList<Wisata> mWisataList = new ArrayList<Wisata>();
    private int mPage = 1;


    private int page = 1;
    private int vote = 0;
    private static final int REQ_CODE = 1000;
    protected int mListSize = 0;
    protected boolean mIsRefresh = false;

    //shared Preference
    private AdView mAdView;
    private String object = "mylistwisatasumbar";
    public final static String SP = "sharedAt";
    User user;

    private String kategori ="";
    private Toolbar toolbar;
    private String category= "minuman";
    private AppBarLayout appBarLayout;

    public static FragmentMinuman newInstance() {
        return new FragmentMinuman();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_list_wisata, container, false);
        mCacheDb = new CacheDb(getDatabase());

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        user = getUser();

        mAdView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        appBarLayout = (AppBarLayout)root.findViewById(R.id.appbar);
        appBarLayout.setVisibility(View.GONE);
        toolbar = (Toolbar)root.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        mLoadingLayout = (LoadingLayout)root. findViewById(R.id.layout_loading);
        swipContainer = (SwipeRefreshLayout) root.findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

        mLoadingLayout = (LoadingLayout) root.findViewById(R.id.layout_loading);

        mQuestionLv = (ExpandableHeightListView)root. findViewById(R.id.lv_data);
        mQuestionLv.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onClick(View view, MotionEvent e) {
                int position = mQuestionLv.pointToPosition((int) e.getX(), (int) e.getY());
                final Wisata wisata = mWisataList.get(position);
                if (user.isLogin.equals("") || user.typeLogin.equals("null")) {
                    showDialogLogin(getString(R.string.text_login_first));
                } else {
                    Intent intent = new Intent(getActivity(), WisataDetail.class);
                    intent.putExtra(Util.getIntentName("position"), position);
                    intent.putExtra(Util.getIntentName("wisata"), wisata);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }

                super.onClick(view, e);
            }

            // your on onDoubleClick here
            @Override
            public void onDoubleClick(View view, MotionEvent e) {


                int position = mQuestionLv.pointToPosition((int) e.getX(), (int) e.getY());
                final Wisata wisata = mWisataList.get(position);

               /* if (mWisataList.get(position).isVoted == 1) {
                    //showToast(getResources().getString(R.string.text_like_already));
                } else {
                    mWisataList.get(position).isVoted = 1;
                    //likeTask(conversation.id, position);

                }*/
                super.onDoubleClick(view, e);
            }

        });
       /* mQuestionLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (mQuestionLv.getLastVisiblePosition() - mQuestionLv.getHeaderViewsCount() -
                        mQuestionLv.getFooterViewsCount()) >= (mAdapter.getCount() - 1)) {

                    //loadTask3();
                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });*/

        return  root;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateList(WisataWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new WisataAdapter(getActivity(),"");

                mAdapter.setData(mWisataList);
                mAdapter.notifyDataSetChanged();
                mQuestionLv.setAdapter(mAdapter);
                if (mListSize != 0) {
                    scrollMyListViewToBottom(-1 + (mWisataList.size() - mListSize));
                }
                mIsRefresh = false;
                return;
            }
            mWisataList.add((Wisata) wrapper.list.get(j));
            Debug.i((new StringBuilder("ID ")).append(((Wisata) mWisataList.get(j)).wisataId).append(" pos ").append(String.valueOf(j)).toString());
            j++;
        } while (true);
    }

/*    @Override
    public void onListItemClick(final int position) {
        final Wisata wisata = mWisataList.get(position);
        Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
        intent.putExtra(Util.getIntentName("position"), position);
        intent.putExtra(Util.getIntentName("question"), wisata);
        startActivity(intent);
    }*/

    @Override
    public void onRefresh() {

        mIsRefresh = true;
        page = 1;
        loadTask2();

    }

    public void onLoadmore() {
        loadTask2();
    }

    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        WisataWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {


                wrapper = mCacheDb.getListWisata("wisata-"+category);
                Debug.i("Checking cache " + "wisata-" + category);
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

                    mWisataList = new ArrayList<Wisata>();

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

    protected void refresh() {
        mIsRefresh = true;
        mPage = 1;

        loadData();
    }


    public void loadTask2() {
        startCountTime();
        page = 1;
       // String url = Cons.CONVERSATION_URL + "/listKuliner.php?&category=="+category+"&&page=" + page + "&count=20";
        String url = Cons.CONVERSATION_URL + "ListKuliner?category="+category;
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        WisataWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.wisataParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mWisataList = new ArrayList<Wisata>();
                                    }
                                    updateList(wrapper);
                                    mCacheDb.updateWisataList(wrapper.list,"wisata-"+category);
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


   /* public void loadTask3() {
        startCountTime();
        page += 1;
       // String url = Cons.CONVERSATION_URL + "/list_kuliner.php?"+kategori+"&category="+category+"&&page=" + page + "&count=20";
        String url = Cons.CONVERSATION_URL + "ListKuliner?id_kategori="+category;
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        WisataWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.wisataParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mWisataList = new ArrayList<Wisata>();
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mRequest);
    }


*/
}