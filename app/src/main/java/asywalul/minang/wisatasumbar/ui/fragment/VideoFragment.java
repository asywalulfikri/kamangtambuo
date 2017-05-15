package asywalul.minang.wisatasumbar.ui.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.VIdeoAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Video;
import asywalul.minang.wisatasumbar.model.VideoWrapper;
import asywalul.minang.wisatasumbar.ui.activity.ArticleDetailActivity;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.OnSwipeTouchListener;


/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class VideoFragment extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener {
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
    private VIdeoAdapter mAdapter;
    private ArrayList<Video> mVideoList = new ArrayList<Video>();
    private int mPage = 1;

    //variabel digunakan
    private String idLikeQuestion;
    private String iduser;
    private int page = 1;
    private int vote = 0;
    private static final int REQ_CODE = 1000;
    private Intent intent;


    //shared Preference
    public final static String SP = "sharedAt";

    private AdView mAdView;
    private User user;


    public static ArticlesFragment newInstance() {
        return new  ArticlesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_video, container, false);
        mCacheDb = new CacheDb(getDatabase());

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        user = getUser();
        iduser = getUser().userId;


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
                final Video video = mVideoList.get(position);

                if (intent != null) {
                    startActivityForResult(intent, REQ_CODE);
                }
                super.onClick(view, e);
            }

            // your on onDoubleClick here
            @Override
            public void onDoubleClick(View view, MotionEvent e) {


                int position = mQuestionLv.pointToPosition((int) e.getX(), (int) e.getY());
                final Video video = mVideoList.get(position);

              /*  if (mVideoList.get(position).isVoted == 1) {
                    showToast(getResources().getString(R.string.text_like_already));
                } else {
                    mVideoList.get(position).isVoted = 1;
                    //likeTask(conversation.id, position);

                }*/
                super.onDoubleClick(view, e);
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

        return root;
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
//			loadData();
            (mCacheTask = new LoadCacheTask()).execute();
        }
    }

    protected boolean isListVisible() {
        return (mQuestionLv.getVisibility() == View.VISIBLE) ? true : false;
    }

    @Override
    public void loadMore() {
        super.loadMore();

        loadTask3();
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
        Debug.i("On Result");

        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null || mVideoList == null || mVideoList.size() == 0) return;

            int position = data.getIntExtra(Util.getIntentName("position"), 0);
            Video articless = data.getParcelableExtra(Util.getIntentName("articles"));
            Video articles = mVideoList.get(position);

            //  mVideoList.get(position).totalResponses = rconv.totalResponses;
            //  mVideoList.get(position).totalVotes = rconv.totalVotes;
            //  mVideoList.get(position).totalViews = articless.totalViews;
            //  mVideoList.get(position).isVoted = rconv.isVoted;

            //  articles.totalResponses = rconv.totalResponses;
            //  articles.totalVotes = rconv.totalVotes;
            //  articles.totalShares = rconv.totalShares;
            //   articles.totalViews = articles.totalViews;

            mAdapter.notifyDataSetChanged();
            mCacheDb.update(articles, "articles-" + iduser);
            Debug.i("On Result");

        }
    }



    private void updateList(VideoWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new VIdeoAdapter(getActivity(), iduser);
                mAdapter.likeQuestion(new VIdeoAdapter.LikeQuestion() {

                    @Override
                    public void OnLikeClickQuestion(View view, int position) {

                        // TODO Auto-generated method stub
                        if (mVideoList.get(position).isWatch == 1) {
                            mVideoList.get(position).totalView = mVideoList.get(position).totalView;
                            vote = mVideoList.get(position).isWatch = 0;
                            //likeVideo(view, position);

                        } else if (mVideoList.get(position).isWatch == 0) {
                            mVideoList.get(position).totalView = mVideoList.get(position).totalView;
                            vote = mVideoList.get(position).isWatch = 1;
                            likeVideo(view, position);
                        }
                    }
                });


                mAdapter.setData(mVideoList);
                mAdapter.notifyDataSetChanged();
                mQuestionLv.setAdapter(mAdapter);
                if (mListSize != 0) {
                    scrollMyListViewToBottom(1 + (mVideoList.size() - mListSize));
                }
                mIsRefresh = false;
                return;
            }
            mVideoList.add((Video) wrapper.list.get(j));
            Debug.i((new StringBuilder("ID ")).append(((Video) mVideoList.get(j)).videoId).append(" pos ").append(String.valueOf(j)).toString());
            j++;
        } while (true);
    }


    @Override
    public void onListItemClick(final int position) {
        final Video articles = mVideoList.get(position);
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

    public void onLoadmore() {
        loadTask2();
    }

    public class LoadCacheTask extends AsyncTask<URL, Integer, Long> {
        VideoWrapper wrapper;

        protected void onCancelled() {
        }

        protected void onPreExecute() {
            // showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {


                wrapper = mCacheDb.getListVideo("video");
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

                    mVideoList = new ArrayList<Video>();

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


    public void loadTask2() {
        startCountTime();
        page = 1;
        String url = Cons.CONVERSATION_URL + "/listVideo.php?userId="+iduser+"&page="+page+"&count=20";
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        VideoWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.videoParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mVideoList = new ArrayList<Video>();
                                    }
                                    updateList(wrapper);
                                    mCacheDb.updateVideoList(wrapper.list,"video");
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


    public void loadTask3() {
        startCountTime();
        page += 1;
        String url = Cons.CONVERSATION_URL + "/listVideo.php?&&userId="+iduser+"&page="+page+"&count=20";
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        swipContainer.setRefreshing(false);
                        VideoWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.videoParsing(response);

                            if (wrapper != null) {
                                if (wrapper.list.size() == 0) {
                                    showEmpty(getString(R.string.text_no_data));
                                } else {
                                    if (mIsRefresh) {
                                        mVideoList = new ArrayList<Video>();
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


    private void likeVideo(View v, int pos) {
        final Video video = mVideoList.get(pos);

        postWatch(video.videoId, pos);
    }
    private void postWatch (final String videoId, final int position) {
        String url = Cons.CONVERSATION_URL+"/postWatch.php";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);

        nameValuePairs.add(new BasicNameValuePair(Cons.KEY_ID, user.userId));
        nameValuePairs.add(new BasicNameValuePair(Cons.VIDEO_ID, videoId));

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), getString(R.string.text_edit_profile_failed), Toast.LENGTH_SHORT).show();
                        VolleyLog.e("Edit profile", "Error" + error.getMessage());
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
                Map<String, String> obj = new HashMap<String, String>();
                obj.put(Cons.KEY_ID, user.userId);
                obj.put(Cons.VIDEO_ID, videoId);
                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(strReq);
    }
    }
