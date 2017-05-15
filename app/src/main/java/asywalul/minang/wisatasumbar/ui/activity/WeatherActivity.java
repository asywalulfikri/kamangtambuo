package asywalul.minang.wisatasumbar.ui.activity;

import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.WeatherListAdapter;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Weather;
import asywalul.minang.wisatasumbar.model.WeatherWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.NetworkUtils;

public class WeatherActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener{
    User user;


    String[] list;

    WeatherWrapper wrapper;

    private LinearLayout mWeatherView;
    private LinearLayout mLoadingViews;
    private ListView mWeatherLv;
    private TextView mNoData;
    private WeatherListAdapter mAdapter;
    private ArrayList<Weather> mWeatherList;
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        user = getUser();

        mWeatherView 	= (LinearLayout) findViewById(R.id.rl_weather);
        mLoadingViews 	= (LinearLayout) findViewById(R.id.ll_loading);
        mNoData			= (TextView) findViewById(R.id.tv_no_data);
        toolbar();

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            weatherTask();
        } else {
            mNoData.setVisibility(View.VISIBLE);
            mLoadingViews.setVisibility(View.GONE);
        }

    }

    public void toolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Cuaca");
        toolbar.setTitleTextColor(Color.WHITE);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //or whatever you used to do on your onOptionItemSelected's android.R.id.home callback
            }
        });
        ApplyFontToolbar();
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
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void updateWeather(ArrayList<Weather> weather) {
        mWeatherList = new ArrayList<Weather>();

        int size = weather.size();
        for (int i = 0; i < size; i++) {
            mWeatherList.add(weather.get(i));
        }

        showHistoryList();
    }

    private void showHistoryList() {
        mWeatherLv = new ListView(getActivity());
        mAdapter = new WeatherListAdapter(getActivity());

        mAdapter.setData(mWeatherList);

        mWeatherLv.setDivider(null);
        mWeatherLv.setDividerHeight(0);
        mWeatherLv.setAdapter(mAdapter);
        mWeatherLv.setSelector(R.drawable.list_selector);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mWeatherLv.setLayoutParams(lp);

        mWeatherView.addView(mWeatherLv);
    }

    private void weatherTask() {
        startCountTime();
        mNoData.setVisibility(View.GONE);
        mLoadingViews.setVisibility(View.VISIBLE);

        //state = state.replace(" ", "_");
        String url = Cons.INBOUND_URL + "/pull/weather/Sumatera_Barat";

        StringRequest  mRequest;
        mRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTimerPage.cancel();
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Debug.i(response);
                            try {
                                wrapper = weatherParsing(response);
                                result();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimerPage.cancel();
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                            Log.e("FFF", "ERROR, " + error.getMessage());

                            mNoData.setVisibility(View.VISIBLE);
                            mLoadingViews.setVisibility(View.GONE);

                            errorHandle(error);

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(mRequest);

    }

    public WeatherWrapper weatherParsing(String jsonString) throws JSONException {
        WeatherWrapper wrapper = null;

        JSONArray jsonArray = new JSONArray(jsonString);
        int size			= jsonArray.length();

        if (size > 0) {
            wrapper = new WeatherWrapper();
            wrapper.list = new ArrayList<Weather>();

            for (int i = 0; i < size; i++) {
                JSONObject object 	= jsonArray.getJSONObject(i);
                JSONObject today 	= object.getJSONObject("hari ini");
                JSONObject besok 	= object.getJSONObject("besok");

                Weather weather = new Weather();

                weather.kota 				= object.getString("kota");
                weather.descToday 			= today.getString("deskripsi");
                weather.suhuToday 			= today.getString("suhuMin")+"-"+today.getString("suhuMax")+"°C";
                weather.kelembabanToday 	= today.getString("kelembabanMin")+"-"+today.getString("kelembabanMax")+"%";
                weather.kecepatanAnginToday = today.getString("kecepatanAngin")+" (km/jam)";
                weather.arahAnginToday		= (today.isNull("arahAngin")) ? "-" : today.getString("arahAngin");
                weather.descBesok 			= besok.getString("deskripsi");
                weather.suhuBesok 			= besok.getString("suhuMin")+"-"+besok.getString("suhuMax")+"°C";
                weather.kelembabanBesok 	= besok.getString("kelembabanMin")+"-"+besok.getString("kelembabanMax")+"%";
                weather.kecepatanAnginBesok = besok.getString("kecepatanAngin")+" (km/jam)";
                weather.arahAnginBesok		= (besok.isNull("arahAngin")) ? "-" : besok.getString("arahAngin");

                wrapper.list.add(weather);
            }
        }

        return wrapper;
    }

    public void result() {
        if (wrapper != null) {
            if (wrapper.list.size() == 0) {
                mNoData.setVisibility(View.VISIBLE);
                mLoadingViews.setVisibility(View.GONE);
            } else {
                updateWeather(wrapper.list);
                mNoData.setVisibility(View.GONE);
                mLoadingViews.setVisibility(View.GONE);
            }
        } else {
            mNoData.setVisibility(View.VISIBLE);
            mLoadingViews.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        weatherTask();
    }
}

