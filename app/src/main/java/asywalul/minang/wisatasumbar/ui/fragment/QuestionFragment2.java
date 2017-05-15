package asywalul.minang.wisatasumbar.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.QuestionAdapter;
import asywalul.minang.wisatasumbar.http.VolleyParsing;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.ConversationWrapper;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.PostQuestionAcivity;
import asywalul.minang.wisatasumbar.ui.activity.QuestionDetailActivity;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.EndlessRecyclerViewScrollListener;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.FloatingActionButton1;
import asywalul.minang.wisatasumbar.widget.FloatingActionsMenu;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.OnLoadMoreListener;

/**
 * Created by Toshiba Asywalul Fikri on 3/12/2016.
 */
public class QuestionFragment2 extends BaseListFragment implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    LoadingLayout loadingLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionsMenu menuMultipleActions;

    FloatingActionButton1 floatingActionButton_a;
    FloatingActionButton1 floatingActionButton1_b;
    View view;

    private LinearLayoutManager linearLayoutManager;
    private QuestionAdapter mAdapter;
    private ArrayList<Conversation> mConversationList = new ArrayList<Conversation>();
    private String userId;
    private User user;
    private int page = 1;
    private EndlessRecyclerViewScrollListener scrollListener;



    public static QuestionFragment2 newInstance() {
        return new QuestionFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question_2, container, false);

        user = getUser();
        userId = user.userId;

        setupswipeRefresh(root);
        setupRecyclerView(root);
        setupButtonActionMenu(root);
        loadTask2();

        return root;
    }

    private void setupRecyclerView(View root){
        recyclerView = (RecyclerView)root.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new QuestionAdapter(mConversationList,getActivity(),recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
/*
        scrollListener.onScrolled(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadingMore=true;
                sendJsonRequest();
                int curlsize=adapterToprated.getItemCount();
                listMovies.addAll(newlistMovies);
                adapterToprated.notifyItemRangeChanged(curlsize,listMovies.size()-2);
            }
        });*/
    }
   
    private void setupswipeRefresh(View root){
        swipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.swipe_refresh_layout);
        loadingLayout      = (LoadingLayout) root.findViewById(R.id.layout_loading);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onPause() {
        if (menuMultipleActions.isExpanded()) {
            menuMultipleActions.toggle();
            view.setVisibility(View.GONE);
            view.setBackgroundColor(getResources().getColor(R.color.transparent));
        }


        super.onPause();
    }

    protected boolean isListVisible() {
        return (recyclerView.getVisibility() == View.VISIBLE) ? true : false;
    }

    protected void showList() {
        if (loadingLayout.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
        }
    }

    protected void showEmpty(String message) {
        if (loadingLayout.getVisibility() == View.VISIBLE) {
            loadingLayout.hideAndEmpty(message, false);
        }
    }

    public void loadTask2() {
        startCountTime();
        page =1;
        String url = Cons.CONVERSATION_URL + "/listQuestion.php?userId=" + userId + "&page=" + page + "&count=20";
        Log.d("urlnya", url);
        StringRequest mRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTimerPage.cancel();
                        swipeRefreshLayout.setRefreshing(false);

                        Debug.i("Response " + response);
                        try {

                            JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                            JSONArray dataJson = jsonObject.getJSONArray("data");
                            if (dataJson == null){
                                Toast.makeText(getActivity(),"DATA KOSONG",Toast.LENGTH_LONG).show();
                            }else {

                                for (int i = 0; i < dataJson.length() ; i++) {
                                    JSONObject itemJson = dataJson.getJSONObject(i);

                                    Conversation conversation = new Conversation();
                                    conversation.conversationId = (!itemJson.isNull(Cons.CONV_ID)) ? itemJson.getString(Cons.CONV_ID) : "";
                                    conversation.content = (!itemJson.isNull(Cons.CONV_CONTENT)) ? itemJson.getString(Cons.CONV_CONTENT) : "";
                                    conversation.tags = (!itemJson.isNull(Cons.CONV_TAGS)) ? itemJson.getString(Cons.CONV_TAGS) : "";
                                    conversation.dateSubmitted = (!itemJson.isNull(Cons.CONV_DATE_SUBMITTED)) ? itemJson.getString(Cons.CONV_DATE_SUBMITTED) : "";
                                    conversation.attachment = (!itemJson.isNull(Cons.CONV_ATTACHMENT)) ? itemJson.getString(Cons.CONV_ATTACHMENT) : "";
                                    conversation.type = (!itemJson.isNull(Cons.CONV_TYPE)) ? itemJson.getString(Cons.CONV_TYPE) : "";
                                    conversation.title = (!itemJson.isNull(Cons.CONV_TITLE)) ? itemJson.getString(Cons.CONV_TITLE) : "";
                                    conversation.summary = (!itemJson.isNull(Cons.CONV_SUMMARY)) ? itemJson.getString(Cons.CONV_SUMMARY) : "";
                                    conversation.latitude = (!itemJson.isNull(Cons.CONV_LATITUDE)) ? itemJson.getString(Cons.CONV_LATITUDE) : "";
                                    conversation.longitude = (!itemJson.isNull(Cons.CONV_LONGITUDE)) ? itemJson.getString(Cons.CONV_LONGITUDE) : "";
                                    conversation.isVoted = (itemJson.isNull(Cons.CONV_ISVOTED)) ? 0 : itemJson.getInt(Cons.CONV_ISVOTED);
                                    conversation.totalResponses = (itemJson.isNull(Cons.CONV_TOTAL_RESPONSES)) ? 0 : itemJson.getInt(Cons.CONV_TOTAL_RESPONSES);
                                    conversation.totalVotes = (itemJson.isNull(Cons.CONV_TOTAL_VOTE)) ? 0 : itemJson.getInt(Cons.CONV_TOTAL_VOTE);
                                    conversation.page = jsonObject.getInt("page");

                                    conversation.user = new User();

                                    conversation.user.userId = (!itemJson.isNull(Cons.KEY_ID)) ? itemJson.getString(Cons.KEY_ID) : "";
                                    conversation.user.fullName = (!itemJson.isNull(Cons.KEY_NAME)) ? itemJson.getString(Cons.KEY_NAME) : "";
                                    conversation.user.avatar = (!itemJson.isNull(Cons.KEY_AVATAR)) ? itemJson.getString(Cons.KEY_AVATAR) : "";
                                    conversation.user.gender = (!itemJson.isNull(Cons.KEY_GENDER)) ? itemJson.getString(Cons.KEY_GENDER) : "";
                                    conversation.user.status = (!itemJson.isNull(Cons.KEY_STATUS)) ? itemJson.getString(Cons.KEY_STATUS) : "";
                                    conversation.user.msisdn = (!itemJson.isNull(Cons.KEY_MSISDN)) ? itemJson.getString(Cons.KEY_MSISDN) : "";
                                    conversation.user.email  = (!itemJson.isNull(Cons.KEY_EMAIL)) ? itemJson.getString(Cons.KEY_EMAIL) : "";
                                    conversation.user.birthDate = (!itemJson.isNull(Cons.KEY_BIRTH)) ? itemJson.getString(Cons.KEY_BIRTH) : "";

                                    mConversationList.add(conversation);
                                }
                                showList();
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimerPage.cancel();
                        swipeRefreshLayout.setRefreshing(false);
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

    public void setupButtonActionMenu(View root) {

        //------------------FLOATING ACTION BUTTON-------------------<<
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.fab_ripple_color));

        view = (View) root.findViewById(R.id.view);
        menuMultipleActions = (FloatingActionsMenu) root.findViewById(R.id.multiple_actions);
        floatingActionButton_a = (FloatingActionButton1) root.findViewById(R.id.action_a);
        floatingActionButton_a.setVisibility(View.GONE);
        floatingActionButton1_b = (FloatingActionButton1) root.findViewById(R.id.action_b);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuMultipleActions.isExpanded()) {
                    menuMultipleActions.toggle();
                    view.setVisibility(View.GONE);
                    view.setBackgroundColor(getResources().getColor(R.color.transparent));

                }

            }
        });

        menuMultipleActions.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuMultipleActions.toggle();
                if (menuMultipleActions.isExpanded()) {
                    view.setBackgroundColor(getResources().getColor(R.color.transparent_dark));
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setBackgroundColor(getResources().getColor(R.color.transparent));
                    view.setVisibility(View.GONE);
                }

            }
        });

        floatingActionButton_a.setIcon(R.drawable.ic_share);
        floatingActionButton_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isLogin.equals("")||user.isLogin.equals("null")){
                    Toast.makeText(getActivity(),"SILAKAN LOGIN DAHULU UNTUK POSTING",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), PostQuestionAcivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,121);
                }
            }
        });

        floatingActionButton1_b.setIcon(R.drawable.ic_tanya);
        floatingActionButton1_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isLogin.equals("")||user.isLogin.equals("null")){
                    Toast.makeText(getActivity(),"SILAKAN LOGIN DAHULU UNTUK POSTING",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(getActivity(), PostQuestionAcivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(intent,121);
                }
            }
        });


    }



    @Override
    public void onListItemClick(final int position) {
        final Conversation conversation = mConversationList.get(position);
        Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
        intent.putExtra(Util.getIntentName("position"), position);
        intent.putExtra(Util.getIntentName("question"), conversation);
        startActivity(intent);
    }



    @Override
    public void onRefresh() {

        mIsRefresh = true;
        page = 0;
        loadTask2();

    }

}