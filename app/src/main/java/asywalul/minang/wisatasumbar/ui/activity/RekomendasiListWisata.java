package asywalul.minang.wisatasumbar.ui.activity;

/**
 * Created by asywalulfikri on 10/5/16.
 */

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.WisataAdapter;
import asywalul.minang.wisatasumbar.database.CacheDb;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Wisata;
import asywalul.minang.wisatasumbar.model.WisataWrapper;
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
public class RekomendasiListWisata extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener {
    // inisial buat user interface
    private ExpandableHeightListView mQuestionLv;
    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout swipContainer;
    private View viewFloatingBt;
    private boolean mIsLoading = false;

    // inisial database dan cache
    private CacheDb mCacheDb;

    //inisial adapter
    private WisataAdapter mAdapter;
    private WisataWrapper wrapper;
    private ArrayList<Wisata> mWisataList = new ArrayList<Wisata>();
    private int mPage = 1;

    //variabel digunakan
    private String idLikeQuestion;
    private String iduser;
    private int page = 1;
    private int vote = 0;
    private static final int REQ_CODE = 1000;
    protected int mListSize = 0;
    protected boolean mIsRefresh = false;

    //shared Preference
    private AdView mAdView;
    private Intent intent;
    private String object = "mylistwisatasumbar";
    public final static String SP = "sharedAt";
    User user;

    //Dialog list
    private ExpandableHeightListView simpleList;
    String kategoriList[];
    ArrayAdapter<String> adapter;
    private EditText autoCompleteTextView;
    private String kategori ="";
    private CardView llkategori;
    private String kabupaten;
    private Toolbar toolbar;
    private String keyword;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_articles, container, false);
        mCacheDb = new CacheDb(getDatabase());

        //<<<-------------------> INISIAL USERID <----------------------->>>>>>
        user = getUser();



/*
        mAdView = (AdView)root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);*/

        mLoadingLayout = (LoadingLayout)root. findViewById(R.id.layout_loading);
        swipContainer = (SwipeRefreshLayout)root. findViewById(R.id.ptr_layout);
        swipContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipContainer.setOnRefreshListener(this);

        mLoadingLayout = (LoadingLayout)root. findViewById(R.id.layout_loading);
        llkategori     = (CardView)root.findViewById(R.id.layout_kategori);
        llkategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialogWithListview();
            }
        });

        mQuestionLv = (ExpandableHeightListView) root.findViewById(R.id.lv_data);
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


       loadTask2();


        return root;

    }



    public void ShowAlertDialogWithListview()
    {

        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setTitle(getString(R.string.text_category));

        View view = getActivity().getLayoutInflater().inflate(R.layout.listviewkategori, null);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        kategoriList = getResources().getStringArray(R.array.array_kabupatendankota);

        simpleList = (ExpandableHeightListView)view.findViewById(R.id.simpleListView);
        autoCompleteTextView = (EditText)view.findViewById(R.id.search_auto_complete);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_adapter, R.id.tv_title_resep, kategoriList);
        simpleList.setAdapter(adapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        simpleList.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onClick(View view, MotionEvent e) {
                int position = simpleList.pointToPosition((int) e.getX(), (int) e.getY());
                Object obj = simpleList.getAdapter().getItem(position);

                Log.d("posisi",String.valueOf(position));
                String value = obj.toString();
                if(position ==0) {
                    kategori = "";
                }else if(position ==1){
                    kategori = "&location=kota_bukittinggi";
                }else if(position ==2){
                    kategori = "&location=kota_padang";
                }else if(position ==3){
                    kategori = "&location=kota_padang_panjang";
                }else if(position ==4){
                    kategori = "&location=kota_pariaman";
                }else if(position ==5){
                    kategori = "&location=kota_payakumbuh";
                }else if(position ==6){
                    kategori = "&location=kota_sawah_lunto";
                }else if(position ==7){
                    kategori = "&location=kab_solok";
                }else if(position ==8){
                    kategori = "&location=kab_agam";
                }else if(position ==9){
                    kategori = "&location=kab_dharmasraya";
                }else if(position ==10){
                    kategori = "&location=kab_kepulauan_mentawai";
                }else if(position ==11){
                    kategori = "&location=kab_lima_puluh_kota";
                }else if(position ==12){
                    kategori = "&location=kab_padang_pariaman";
                }else if(position ==13){
                    kategori = "&location=kab_pasaman";
                }else if(position ==14){
                    kategori = "&location=kab_pasaman_barat";
                }else if(position ==15){
                    kategori = "&location=kab_pesisir_selatan";
                }else if(position ==16){
                    kategori = "&location=kab_sijunjung";
                }else if(position ==17){
                    kategori = "&location=kab_solok";
                }else if(position ==18){
                    kategori = "&location=kab_solok_selatan";
                }else if(position ==19){
                    kategori = "&location=kab_tanah_datar";
                }

                refresh();
                hideKeyboard();
                autoCompleteTextView.setText("");
                dialog.dismiss();

                super.onClick(view, e);
            }

            // your on onDoubleClick here
            @Override
            public void onDoubleClick(View view, MotionEvent e) {
                int position = simpleList.pointToPosition((int) e.getX(), (int) e.getY());
                Object obj = simpleList.getAdapter().getItem(position);
                String value = obj.toString();
                kategori = "&location="+value.toString();
                refresh();
                hideKeyboard();
                autoCompleteTextView.setText("");
                super.onDoubleClick(view, e);
                dialog.dismiss();

            }

        });


        dialog.setContentView(view);

        dialog.show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        mCacheDb.reload(getDatabase());

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



    private void updateList(WisataWrapper wrapper) {
        showList();

        int i = wrapper.list.size();
        int j = 0;
        do {
            if (j >= i) {
                mPage = 1 + mPage;
                mListSize = i;
                mAdapter = new WisataAdapter(getActivity(), iduser);

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


    protected void refresh() {
        mIsRefresh = true;
        mPage = 1;

        loadData();
    }


    public void loadTask2() {
        startCountTime();
        page = 1;

        String url = "http://dhiva.16mb.com/rest_server/index.php/api/rekomendasi?rekomendasi=true";
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

    public void loadTask4() {

        String url = "http://dhiva.16mb.com/rest_server/index.php/api/rekomendasi?rekomendasi=true";
        Log.d("urlnya",url);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        WisataWrapper wrapper = null;
                        Log.d("Responsenya ","yogs"+response);
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
                                    mCacheDb.updateWisataList(wrapper.list,"wisata-rekomendasi"+kabupaten);
                                }
                            } else {
                                showEmpty(getString(R.string.text_download_failed));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("PostQuestion", "Error" + error.getMessage());
                Log.d("erorpasti","erorpasti");
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
                obj.put("keyword",keyword);

                return obj;


            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(strReq);
    }

    public void loadTask22() {
        startCountTime();
        page = 1;
        String url = "http://dhiva.16mb.com/restful/Search.php";
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.POST, url,
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
                                    mCacheDb.updateWisataList(wrapper.list,"wisata-"+kabupaten);
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
       // String url = Cons.CONVERSATION_URL + "/listWisata.php?&category=1&&location="+kabupaten+"&&page=" + page + "&count=20";
        String url = Cons.CONVERSATION_URL + "WisataAlam?id_kategori=1&idk="+kabupaten;
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

    @Override
    public void loadMore() {
        super.loadMore();

       // loadTask3();
    }



}