package asywalul.minang.wisatasumbar.http;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.model.Weather;
import asywalul.minang.wisatasumbar.model.WeatherWrapper;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.HttpManager1;

public class InboundConnection {

    public WeatherWrapper getWeather(String kota) throws Exception {
        WeatherWrapper wrapper = null;

        try {
            kota = kota.replace(" ", "_");
            String url = Cons.INBOUND_URL + "/pull/weather/" + "Sumatera Barat";
            Log.e("XXX", url);
            String get = HttpManager1.httpGet(url);

            String statusCode = get.substring(get.lastIndexOf("`")+1, get.length());
            String response = get.substring(0, get.lastIndexOf("`"));

            if (statusCode.equals("200")) {
                JSONArray jsonArray = new JSONArray(response);
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
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return wrapper;
    }

}
