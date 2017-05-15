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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.joooonho.SelectableRoundedImageView;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.CommentListAdapter;
import asywalul.minang.wisatasumbar.adapter.CustomArrayAdapter;
import asywalul.minang.wisatasumbar.database.QuestionDb;
import asywalul.minang.wisatasumbar.http.QuestionConnection;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.http.exeption.WisataException;
import asywalul.minang.wisatasumbar.model.Comment;
import asywalul.minang.wisatasumbar.model.CommentWrapper;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.TimeUtil;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;
import asywalul.minang.wisatasumbar.widget.FullImageActivity;

/**
 * Created by Toshiba on 4/6/2016.
 */
public class QuestionDetailActivity extends BaseActivity {

    private int mPosition;
    private Conversation mConversation;
    private int selectedQuestion;
    private TextView nama, waktu, kategori, commentt;
    private ImageView ivsend, ivLike, mCloseIv, mPhotoIv;
    private ImageView avatar;
    private SelectableRoundedImageView ivFoto;
    private String etcomment, mIdquestion;
    private ExpandableHeightListView mCommentLv;
    private CommentListAdapter mCommentAdapter;
    public final static String SP = "sharedAt";
    Bitmap bitmappp = null;
    private Comment mResponse;
    private QuestionDb mQuestionDb;
    private LinearLayout mCommentsView;
    private TextView tvLike, tvComment;
    private boolean mIsLoading = false;
    public static final String sendImage = "imageFull";
    String a, time;
    private Toolbar toolbar;
    private ProgressDialog mDialog;
    private ArrayList<Comment> mResponseList = new ArrayList<Comment>();
    private boolean loadPost;
    private TextView tvLoading;
    String iduser;
    private int page = 1;
    private ImageView ivpoto;
    private TextView komentar;
    User user;
    private AutoLinkTextView pertanyaan;
    private TextView tvTotalVote, tvTotalResponses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        toolbar();
        user = getUser();
        nama        = (TextView) findViewById(R.id.tv_author);
        pertanyaan  = (AutoLinkTextView) findViewById(R.id.tv_title);
        avatar      = (ImageView) findViewById(R.id.iv_author);
        commentt    = (TextView) findViewById(R.id.et_comment);
        ivsend      = (ImageView) findViewById(R.id.iv_write);
        ivpoto      = (ImageView) findViewById(R.id.iv_image);
        tvTotalVote = (TextView) findViewById(R.id.tvLikeQuestionDetail);
        ivLike      = (ImageView) findViewById(R.id.iv_like);
        tvTotalResponses = (TextView) findViewById(R.id.tvCommentQuestionDetail);
        commentt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int charCount = commentt.getText().toString().trim().length();
                if (charCount != 0) {
                    ivsend.setColorFilter(getResources().getColor(R.color.instagram));
                    Log.e("DDD", commentt.getText().toString().trim().length() + "");
                } else {
                    ivsend.setColorFilter(getResources().getColor(R.color.bg_icon));
                    Log.e("EEE", commentt.getText().toString().trim().length() + "");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        mCommentsView = (LinearLayout) findViewById(R.id.rl_comments);
        tvLoading = (TextView) findViewById(R.id.tv_loading);
        komentar = (TextView) findViewById(R.id.tv_loading);
        tvLoading.setVisibility(mCommentsView.GONE);
        user = getUser();
        iduser = user.userId;
        mCommentAdapter = new CommentListAdapter(this);


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


        Bundle bundle = getIntent().getExtras();
        mConversation = bundle.getParcelable(Util.getIntentName("question"));
        mPosition = bundle.getInt(Util.getIntentName("position"));
        mIdquestion = mConversation.conversationId;

        nama.setText(mConversation.user.fullName);
        pertanyaan.addAutoLinkMode(
                AutoLinkMode.MODE_HASHTAG,
                AutoLinkMode.MODE_PHONE,
                AutoLinkMode.MODE_URL,
                AutoLinkMode.MODE_MENTION,
                AutoLinkMode.MODE_CUSTOM);
        pertanyaan.setCustomRegex("\\sAllo\\b");
        pertanyaan.setHashtagModeColor(ContextCompat.getColor(this,R.color.instagram));
        pertanyaan.setPhoneModeColor(ContextCompat.getColor(this, R.color.instagram));
        pertanyaan.setCustomModeColor(ContextCompat.getColor(this, R.color.instagram));
        pertanyaan.setUrlModeColor(ContextCompat.getColor(this, R.color.instagram));
        pertanyaan.setMentionModeColor(ContextCompat.getColor(this, R.color.instagram));
        pertanyaan.setEmailModeColor(ContextCompat.getColor(this, R.color.instagram));
        pertanyaan.setSelectedStateColor(ContextCompat.getColor(this, R.color.instagram));
        pertanyaan.setAutoLinkText(mConversation.content);

        tvTotalVote.setText(String.valueOf(mConversation.totalVotes));
        tvTotalResponses.setText(String.valueOf(mConversation.totalResponses));
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mConversation.isVoted == 0) {
                    new LikeQuestionHome().execute();
                } else {
                   new unLikeQuestionHome().execute();
                }
            }
        });


        if (mConversation.user.avatar == null || mConversation.user.avatar.equals("")) {
        } else {
            avatar.setVisibility(View.VISIBLE);

            Picasso.with(this) //
                    .load((mConversation.user.avatar.equals("")) ? null : mConversation.user.avatar) //
                    .placeholder(R.drawable.ic_profile_blank) //
                    .error(R.drawable.ic_profile_blank)
                    .into(avatar);
        }

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionDetailActivity.this,Profile_Activity_Other_User.class);
                intent.putExtra(Util.getIntentName("position"), mPosition);
                intent.putExtra(Util.getIntentName("conversation"), mConversation);
                startActivity(intent);
            }
        });

        if (mConversation.attachment == null || mConversation.attachment.equals("")) {
            ivpoto.setVisibility(View.GONE);
        } else {
            ivpoto.setVisibility(View.VISIBLE);
           Picasso.with(this) //
                    .load((mConversation.attachment.equals("")) ? null : mConversation.attachment) //
                    .placeholder(R.drawable.no_image) //
                    .error(R.drawable.no_image)
                    .into(ivpoto);
          //  Uri uri = Uri.parse(mConversation.attachment);
          //  ivpoto.setImageURI(uri);
        }
        ivpoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(QuestionDetailActivity.this, FullImageActivity.class);
                intent.putExtra(sendImage,mConversation.attachment);
                startActivity(intent);
            }
        });

        if (user.isLogin.equals("1")) {
            if (mConversation.isVoted == 1) {
                ivLike.setColorFilter(Color.parseColor("#D50000"));
            }else  {
                ivLike.setColorFilter(Color.parseColor("#bdbdbd"));
            }
        }

        getListComment();

    }

    private void toolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.instagram_2), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //or whatever you used to do on your onOptionItemSelected's android.R.id.home callback
            }
        });
    }


    public void volley(View view) {
        String content = commentt.getText().toString().replaceAll("\\s+", " ").trim();
        String commentId = "";
        String timeunix = (String.valueOf(TimeUtil.getUnix()));
        if (commentId.equals("") || commentId.equals("null")) {
            commentId = UUID.randomUUID().toString().trim();
        }
        Log.i("ISI", content);

        if (content.equals("")) {
            Toast.makeText(QuestionDetailActivity.this, getString(R.string.text_commentar_blank), Toast.LENGTH_SHORT).show();
            return;
        }
        postComment(content, commentId, timeunix);

    }


    private void postComment(final String comment, final String commentId, final String timeunix) {

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair(Cons.RESPONSE_ID, commentId));
        params.add(new BasicNameValuePair(Cons.CONV_CONTENT, comment));
        params.add(new BasicNameValuePair(Cons.KEY_ID, iduser));
        params.add(new BasicNameValuePair(Cons.CONV_ID, mIdquestion));
        params.add(new BasicNameValuePair(Cons.CONV_DATE_SUBMITTED, time));
        params.add(new BasicNameValuePair(Cons.TIME_UNIX, timeunix));

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
                Toast.makeText(QuestionDetailActivity.this, "Kommentar Gagal dikirim", Toast.LENGTH_SHORT).show();
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
                mparams.put(Cons.CONV_CONTENT, comment);
                mparams.put(Cons.KEY_ID, iduser);
                mparams.put(Cons.CONV_ID, mIdquestion);
                mparams.put(Cons.CONV_DATE_SUBMITTED, time);
                mparams.put(Cons.TIME_UNIX, timeunix);
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
        mDialog = new ProgressDialog(QuestionDetailActivity.this);
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

        intent.putExtra(Util.getIntentName("position"), mPosition);
        intent.putExtra(Util.getIntentName("question"), mConversation);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void updateComments(Comment comment) {
        mConversation.totalResponses++;

        if (mResponseList == null) {
            mResponseList = new ArrayList<Comment>();
            mResponseList.add(0, comment);
        } else {
            mResponseList.add(comment);
        }

        commentt.setText("");
        tvTotalResponses.setText(String.valueOf(mConversation.totalResponses));

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
            mCommentLv.setDivider(null);
            mCommentLv.setDividerHeight(0);
            mCommentAdapter = new CommentListAdapter(this);
            mCommentAdapter.setData(mResponseList);
            mCommentAdapter.setContentOwner(mConversation.user.userId);

            mCommentLv.setAdapter(mCommentAdapter);
            mCommentLv.setExpanded(true);
            mCommentLv.setSelector(R.drawable.list_selector);

            mCommentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    mResponse = mResponseList.get(position);
                    Intent intent = new Intent(QuestionDetailActivity.this, Profile_activity_Other_User2.class);
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

        CustomArrayAdapter adapter = new CustomArrayAdapter(getActivity(), R.layout.list_item_adapter, options);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                   // deleteComment(id, position);

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


   /* public void deleteComment(final String commentId, final int position) {
        String url = Cons.CONVERSATION_URL + "/deleteComment.php?id_comment=" + commentId;
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

    public class LikeQuestionHome extends AsyncTask<URL, Integer, Long> {
        String error = "";
        int position;

        protected LikeQuestionHome() {

        }

        protected void onCancelled() {
            mConversation.isVoted = 0;
            mIsLoading = false;
        }

        protected void onPreExecute() {
            mIsLoading = true;

            Toast.makeText(getActivity(), "Disukai", Toast.LENGTH_SHORT).show();

        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                QuestionConnection conn = new QuestionConnection();
                conn.like(mConversation.conversationId, user.userId);

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
                mConversation.totalVotes = mConversation.totalVotes + 1;
                ivLike.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            } else {
                error = (error.equals("")) ? getString(R.string.text_gagal_like) : error;
                mConversation.isVoted = 0;

                showError(error);

            }
        }
    }

    public class unLikeQuestionHome extends AsyncTask<URL, Integer, Long> {
        String error = "";
        int position;

        protected unLikeQuestionHome() {
        }

        protected void onCancelled() {
            mConversation.isVoted = 1;
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

                conn.unlike(mConversation.conversationId, user.userId);

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
                mConversation.totalVotes = mConversation.totalVotes - 1;
                ivLike.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);

            } else {
                error = (error.equals("")) ? getString(R.string.text_gagal_like) : error;
                mConversation.isVoted = 1;

                showError(error);

            }
        }
    }
}


