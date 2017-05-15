package asywalul.minang.wisatasumbar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Weather;



public class WeatherListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;

    private ArrayList<Weather> mDataList;

    public WeatherListAdapter(Context context) {
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

    }

    public void setData(ArrayList<Weather> list) {
        mDataList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (mDataList == null) ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_weather1, null);

            holder = new ViewHolder();

            holder.kotaTv = (TextView) convertView.findViewById(R.id.tv_kota);
            holder.descToday = (TextView) convertView.findViewById(R.id.tv_deskToday);
            holder.suhuToday = (TextView) convertView.findViewById(R.id.tv_suhuToday);
            holder.kelembabanToday = (TextView) convertView.findViewById(R.id.tv_kelembabanToday);
            holder.kecAnginToday = (TextView) convertView.findViewById(R.id.tv_kecAnginToday);
            holder.arahAnginToday = (TextView) convertView.findViewById(R.id.tv_arahAnginToday);
            holder.descBesok = (TextView) convertView.findViewById(R.id.tv_deskBesok);
            holder.suhuBesok = (TextView) convertView.findViewById(R.id.tv_suhuBesok);
            holder.kelembabanBesok = (TextView) convertView.findViewById(R.id.tv_kelembabanBesok);
            holder.kecAnginBesok = (TextView) convertView.findViewById(R.id.tv_kecAnginBesok);
            holder.arahAnginBesok = (TextView) convertView.findViewById(R.id.tv_arahAnginBesok);

            holder.weatherTodayIv = (ImageView) convertView.findViewById(R.id.iv_weather_today);
            holder.weahterTommorowIv = (ImageView) convertView.findViewById(R.id.iv_weather_tomorrow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Weather weather = mDataList.get(position);

        holder.kotaTv.setText(weather.kota);
        holder.descToday.setText(weather.descToday);
        holder.descBesok.setText(weather.descBesok);

        if (!weather.descToday.equals("")) {
            holder.weatherTodayIv.setImageResource(getWeatherIcon(weather.descToday.trim()));
        } else {
            holder.weatherTodayIv.setVisibility(View.GONE);
        }

        if (!weather.descBesok.equals("")) {
            holder.weahterTommorowIv.setImageResource(getWeatherIcon(weather.descBesok.trim()));
        } else {
            holder.weahterTommorowIv.setVisibility(View.GONE);
        }

        if (!weather.suhuToday.equals("0-0°C")) {
            holder.suhuToday.setText(weather.suhuToday);
        } else {
            holder.suhuToday.setVisibility(View.GONE);
        }

        if (!weather.kelembabanToday.equals("0-0%")) {
            holder.kelembabanToday.setText(mContext.getString(R.string.text_kelembaban) + " : " + weather.kelembabanToday);
        } else {
            holder.kelembabanToday.setVisibility(View.GONE);
        }

        if (!weather.kecepatanAnginToday.equals("0 (km/jam)")) {
            holder.kecAnginToday.setText(mContext.getString(R.string.text_kec_angin) + " : " + weather.kecepatanAnginToday);
        } else {
            holder.kecAnginToday.setVisibility(View.GONE);
        }

        if (!weather.arahAnginToday.equals("-")) {
            holder.arahAnginToday.setText(mContext.getString(R.string.text_arah_angin) + " : " + weather.arahAnginToday);
        } else {
            holder.arahAnginToday.setVisibility(View.GONE);
        }

        if (!weather.suhuBesok.equals("0-0°C")) {
            holder.suhuBesok.setText(weather.suhuBesok);
        } else {
            holder.suhuBesok.setVisibility(View.GONE);
        }

        if (!weather.kelembabanBesok.equals("0-0%")) {
            holder.kelembabanBesok.setText(weather.kelembabanBesok);
        } else {
            holder.kelembabanBesok.setVisibility(View.GONE);
        }

        if (!weather.kecepatanAnginBesok.equals("0 (km/jam)")) {
            holder.kecAnginBesok.setText(weather.kecepatanAnginBesok);
        } else {
            holder.kecAnginBesok.setVisibility(View.GONE);
        }

        if (!weather.arahAnginBesok.equals("-")) {
            holder.arahAnginBesok.setText(weather.arahAnginBesok);
        } else {
            holder.arahAnginBesok.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {

        public TextView kotaTv;
        public TextView descToday;
        public TextView suhuToday;
        public TextView kelembabanToday;
        public TextView kecAnginToday;
        public TextView arahAnginToday;
        public TextView descBesok;
        public TextView suhuBesok;
        public TextView kelembabanBesok;
        public TextView kecAnginBesok;
        public TextView arahAnginBesok;

        public ImageView weatherTodayIv;
        public ImageView weahterTommorowIv;

    }

    private int getWeatherIcon(String weather) {
        int pic;

        if (weather.equals("Hujan Ringan")) {
            pic = R.drawable.hujan_ringan;
        } else if (weather.equals("Hujan Sedang")) {
            pic = R.drawable.hujan_ringan;
        } else if (weather.equals("Hujan Lebat")) {
            pic = R.drawable.hujan_lebat;
        } else if (weather.equals("Hujan Petir")) {
            pic = R.drawable.hujan_lokal;
        } else if (weather.equals("Hujan Lokal")) {
            pic = R.drawable.hujan_lokal;
        } else if (weather.equals("Berawan")) {
            pic = R.drawable.berawan;
        } else if (weather.equals("Cerah Berawan")) {
            pic = R.drawable.berawan;
        } else {
            pic = R.drawable.cerah;
        }

        return pic;
    }
}
