package asywalul.minang.wisatasumbar.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CommentListAdapter;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.Comment;
import asywalul.minang.wisatasumbar.model.CommentWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Wisata;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;

/**
 * Created by Toshiba on 4/6/2016.
 */
public class WisataDetail extends BaseActivity {

    private int mPosition = 0;
    private Wisata mWisataList;
    private int selectedQuestion;
    private TextView  namawisata, lokasi, kategori, commentt;
    private ImageView ivWisata;
    private SelectableRoundedImageView ivFoto;
    private String etcomment, mIdquestion;
    private ExpandableHeightListView mCommentLv;
    private CommentListAdapter mCommentAdapter;
    public final static String SP = "sharedAt";
    Bitmap bitmappp = null;
    private Comment mResponse;
    private LinearLayout mCommentsView;
    private TextView tvLike, tvComment;
    private boolean mIsLoading = false;
    public static final String sendImage = "imageFull";
    String a, time;
    private ProgressDialog mDialog;
    private ArrayList<Comment> mResponseList = new ArrayList<Comment>();
    private boolean loadPost;
    private TextView tvLoading;
    String iduser;
    private  int page =1;
    private TextView komentar;
    User user;
    private String TAG =WisataDetail.class.getSimpleName();
    private FloatingActionButton fab;
    private Intent intent;
    private AdView mAdView;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setTitle(getString(R.string.text_resep));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //or whatever you used to do on your onOptionItemSelected's android.R.id.home callback
            }
        });
        ApplyFontToolbar();

        Bundle bundle = getIntent().getExtras();
        mWisataList= bundle.getParcelable(Util.getIntentName("wisata"));
        mPosition = bundle.getInt(Util.getIntentName("position"));


        user = getUser();
        iduser = user.userId;


        WebView content        = (WebView) findViewById(R.id.tv_content);
        lokasi         = (TextView) findViewById(R.id.tv_date);
        fab            = (FloatingActionButton)findViewById(R.id.fab);
        ivWisata       = (ImageView) findViewById(R.id.iv_location);

        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        String custom =
                "<html>" +
                        head +
                        "<body style='color:#555555;font-size:14px'>" + mWisataList.content +
                        "</body>" +
                        "</html>";
        content.loadData(custom, "text/html", "UTF-8");
        lokasi.setText(mWisataList.title);
        Picasso.with(this) //
                .load((mWisataList.attachment.equals("")) ? null : mWisataList.attachment) //
                .placeholder(R.drawable.no_image) //
                .error(R.drawable.no_image)
                .into(ivWisata);


        CollapsingToolbarLayout mCTollbar   = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar.setTitle(mWisataList.title);
        mCTollbar.setTitle(mWisataList.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mWisataList.title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        commentt = (TextView) findViewById(R.id.et_comment);
        mCommentsView = (LinearLayout) findViewById(R.id.rl_comments);
        komentar = (TextView)findViewById(R.id.tv_loading);

        mCommentAdapter = new CommentListAdapter(this);

        if(mWisataList.latitude.equals("")||mWisataList.longitude.equals("")){
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WisataDetail.this,LocationShown.class);
                intent.putExtra("latitude",mWisataList.latitude);
                intent.putExtra("longitude",mWisataList.longitude);
                intent.putExtra("origin",mWisataList.title);
                startActivity(intent);
            }
        });





        Time today = new Time();


        today.setToNow();
        String dd = (today.monthDay + "");             // Day of the month (1-31)
        String mm = (today.month + "");              // Month (0-11)
        String yy = (today.year + "");                // Year
        String hh = (today.format("%k:%M:%S"));  // Current time

        if (mm.equals("1")) {
            a = "02";
        }
        if (mm.equals("2")) {
            a = "03";
        }
        if (mm.equals("3")) {
            a = "04";
        }
        if (mm.equals("4")) {
            a = "05";
        }
        if (mm.equals("5")) {
            a = "06";
        }
        if (mm.equals("6")) {
            a = "7";
        }
        if (mm.equals("7")) {
            a = "8";
        }

        if (mm.equals("8")) {
            a = "9";
        }
        if (mm.equals("9")) {
            a = "10";
        }

        if (mm.equals("10")) {
            a = "11";
        }
        if (mm.equals("11")) {
            a = "12";
        }
        if (mm.equals("0")) {
            a = "1";
        }

        time = yy + "-" + a + "-" + dd + " " + hh;




      //  getListComment();

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

    public void volley(View view) {
        String content = commentt.getText().toString().replaceAll("\\s+", " ").trim();
        String commentId = "";
        if(commentId.equals("")||commentId.equals("null")){
            commentId = UUID.randomUUID().toString().trim();
        }
        Log.i("ISI", content);

        if (content.equals("")) {
            Toast.makeText(WisataDetail.this, "Kommntar Belum Di tulis", Toast.LENGTH_SHORT).show();
            return;
        }
        postComment(content,commentId);

    }



    private void postComment(final String comment, final String commentId) {
        // showDialog();

        Log.d("content1",comment);
        Log.d("content2",commentId);
        Log.d("content3",iduser);
        Log.d("content4",mIdquestion);
        Log.d("content5",time);

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair(Cons.RESPONSE_ID,commentId));
        params.add(new BasicNameValuePair(Cons.CONV_CONTENT, comment));
        params.add(new BasicNameValuePair(Cons.KEY_ID,iduser));
        params.add(new BasicNameValuePair(Cons.CONV_ID,mIdquestion));
        params.add(new BasicNameValuePair(Cons.CONV_DATE_SUBMITTED,time));

        String rawUrl = Cons.CONVERSATION_URL + "/postComent.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                rawUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Comment comment = null;
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            comment = parsing.postCommentParsing(response);
                            commentt.setText("");
                            hideKeyboard();
                            updateComments(comment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                hideKeyboard();
                tvLoading.setVisibility(View.GONE);
                Toast.makeText(WisataDetail.this, "Kommentar Gagal dikirim", Toast.LENGTH_SHORT).show();
                VolleyLog.e("PostComment", "Error" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //   params.put("Authorization", headerStore.authorization);
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
                mparams.put(Cons.RESPONSE_ID,commentId);
                mparams.put(Cons.CONV_CONTENT, comment);
                mparams.put(Cons.KEY_ID, iduser);
                mparams.put(Cons.CONV_ID, mIdquestion);
                mparams.put(Cons.CONV_DATE_SUBMITTED,time);
                return mparams;
            }
        };

        Volley.newRequestQueue(this).add(strReq);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }



    private void showDialog() {
        mDialog = new ProgressDialog(WisataDetail.this);
        mDialog.setMessage(getString(R.string.text_loading));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    private void updateComments(Comment comment) {
        mWisataList.totalResponses++;

        if (mResponseList == null) {
            mResponseList = new ArrayList<Comment>();
            mResponseList.add(0, comment);
        } else {
            mResponseList.add(comment);
        }

        commentt.setText("");
        //mNumCommentsTv.setText(String.valueOf(mQuestion.totalResponses));

        hideKeyboard();
        showCommentList();
    }
    private void updateComments(ArrayList<Comment> comments) {
        if (mResponseList == null) {
            mResponseList = new ArrayList<Comment>();
        }

        int size = comments.size();
        for (int i = 0; i < size; i++) {
            mResponseList.add(comments.get(i));
        }

        showCommentList();
    }

    private void showCommentList() {
        if (mCommentLv == null) {
            mCommentLv = new ExpandableHeightListView(this);
            mCommentAdapter = new CommentListAdapter(this);
            mCommentAdapter.setData(mResponseList);
            mCommentAdapter.setContentOwner(mWisataList.user.userId);

            mCommentLv.setAdapter(mCommentAdapter);
            mCommentLv.setExpanded(true);
            mCommentLv.setSelector(R.drawable.list_selector);

            mCommentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    mResponse = mResponseList.get(position);
                    Intent intent = new Intent(WisataDetail.this, Profile_activity_Other_User2.class);
                    intent.putExtra(Util.getIntentName("position"), position);
                    intent.putExtra(Util.getIntentName("comment"), mResponseList);
                    startActivity(intent);
                }
            });



            mCommentAdapter.option(new CommentListAdapter.Option() {

                @Override
                public void onOptionClick(View view, int position) {
                    Log.e("FFF", mResponseList.get(position).responseId);
                    showOptionDialog(mResponseList.get(position).responseId, position);

                }
            });

            mCommentsView.removeAllViews();
            mCommentsView.addView(mCommentLv);
        } else {
            mCommentAdapter.notifyDataSetChanged();
        }
    }

    private void showOptionDialog(final String id, final int position) {
        String[] options = {getString(R.string.text_confirm_delete_comment)};

        CustomArrayAdapter adapter  = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                    //deleteComment(id, position);

                } else if (arg1 == 1) {
                }
            }
        });

        builder.create().show();
    }

    public void getListComment() {
        startCountTime();
        page = 1;
        String url = Cons.CONVERSATION_URL + "/list_comment_question.php?conversationId=" + mIdquestion;
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("response of resend code is:" + response);
                        mTimerPage.cancel();
                        CommentWrapper wrapper = null;
                        Debug.i("Response " + response);
                        try {
                            VolleyParsing parsing = new VolleyParsing();
                            wrapper = parsing.commentParsing(response);

                            if (wrapper != null) {
                                updateComments(wrapper.list);
                                komentar.setText(getString(R.string.text_response));
                            } else {
                                komentar.setText(getString(R.string.text_reponse_null));

                                //textView.setTextAppearance(getActivity(), R.style.TextCommon_Small);
                                //textView.setText(getString(R.string.text_no_comment));

                                //mCommentsView.addView(textView);
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
                        VolleyLog.d("Response Error", "Error" + error.getMessage());
                       // showEmpty(getString(R.string.text_download_failed));
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



    private static final Drawable getDrawableModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

  /*  public void deleteComment(final String commentId, final int position) {
        String url = Cons.CONVERSATION_URL + "/deleteComment.php?id_comment="+commentId;
        Log.d("hapus", url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getActivity(), "Komentar berhasil di hapus", Toast.LENGTH_SHORT).show();
                mResponseList.remove(position);
                mCommentAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Gagal menghapus Komentar", Toast.LENGTH_SHORT).show();
                VolleyLog.e("PostComment", "Error" + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //   params.put("Authorization", headerStore.authorization);
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

                mparams.put(Cons.RESPONSE_ID, commentId);
                return mparams;
            }
        };

        Volley.newRequestQueue(getActivity()).add(jsonRequest);
    }*/

}



