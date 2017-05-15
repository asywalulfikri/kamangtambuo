package asywalul.minang.wisatasumbar.ui.fragment;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WeatherFragment extends BaseListFragment  implements SwipeRefreshLayout.OnRefreshListener{
    User user;


    String[] list;

    WeatherWrapper wrapper;

    private LinearLayout mWeatherView;
    private LinearLayout mLoadingViews;
    private ListView mWeatherLv;
    private TextView mNoData;
    private WeatherListAdapter mAdapter;
    private ArrayList<Weather> mWeatherList;

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root 	=  inflater.inflate(R.layout.fragment_weather, container, false);
        user = getUser();

        mWeatherView 	= (LinearLayout) root.findViewById(R.id.rl_weather);
        mLoadingViews 	= (LinearLayout) root.findViewById(R.id.ll_loading);
        mNoData			= (TextView) root.findViewById(R.id.tv_no_data);


        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            weatherTask();
        } else {
            mNoData.setVisibility(View.VISIBLE);
            mLoadingViews.setVisibility(View.GONE);
        }

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
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
                        if(isAdded()) {
                            try {
                                wrapper = weatherParsing(response);
                                result();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                        if(isAdded()) {
                            Log.e("FFF", "ERROR, " + error.getMessage());

                            mNoData.setVisibility(View.VISIBLE);
                            mLoadingViews.setVisibility(View.GONE);

                            errorHandle(error);
                        }

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

