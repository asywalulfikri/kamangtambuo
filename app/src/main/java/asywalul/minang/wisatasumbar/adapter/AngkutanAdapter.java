package asywalul.minang.wisatasumbar.adapter;

/**
 * Created by asywalulfikri on 11/11/16.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Angkutan;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;



public class AngkutanAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Typeface mFont;
    private Context mContext;

    private ViewHolder holder;
    private ArrayList<Angkutan> mConversationList = new ArrayList<Angkutan>();
    private PrettyTime mPrettyTime;

    private DataValueOpen mDataValueOpen;
    private ArrayList<Angkutan> mDataList;
    String  useridd;
    User user;

    public AngkutanAdapter(Context context, String userid) {
        init(context,userid);
    }

    public AngkutanAdapter(Context context, DataValueOpen value,String userid) {
        init(context,userid);
        user = ((BaseActivity) context).getUser();
        this.mDataValueOpen = value;
    }

    private void init(Context context, String userid) {
        mContext = context;
        this.useridd = userid;
        user = ((BaseActivity) context).getUser();

        if (context != null) {

            mInflater = LayoutInflater.from(context);
            mPrettyTime = new PrettyTime();


        }

    }

    public void setOpenData(DataValueOpen open) {
        mDataValueOpen = open;
    }

    public void setData(ArrayList<Angkutan> list) {
        mDataList = list;
    }



    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//		return (mDataList == null) ? 0 : (mDataList.size() + 1);
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {

        if (itemView == null) {
            itemView = mInflater.inflate(R.layout.list_item_angkutan, null);

            holder = new ViewHolder();

            holder.Perusahaan      = (TextView)itemView.findViewById(R.id.tv_perusahaan);
            holder.pemilik         = (TextView)itemView.findViewById(R.id.tv_pemilik);
            holder.jumlahkendaraan = (TextView)itemView.findViewById(R.id.tv_jumlah_kendaraan);
            holder.merek           = (TextView)itemView.findViewById(R.id.tv_merek);


            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final Angkutan angkutan = mDataList.get(position);


            holder.Perusahaan.setText(angkutan.nama_perusahaan);
            holder.pemilik.setText(angkutan.pemilik);
            holder.jumlahkendaraan.setText(angkutan.jumlahkendaraan);
            holder.merek.setText(angkutan.merek);



        return itemView;
    }


    private String getTags(String[] tags) {
        String tag 		 = "";

        StringBuilder sb = new StringBuilder();
        int length		 = tags.length;

        for (int i = 0; i < length; i++) {
            sb.append(tags[i]);

            if (i != length - 1) {
                sb.append(" | ");
            }
        }

        tag = sb.toString();

        return tag;
    }
    public interface DataValueOpen {
        public void setValueOpen(String id, String value);
    }


    static class ViewHolder {

        TextView Perusahaan;
        TextView pemilik;
        TextView jumlahkendaraan;
        TextView merek;

    }
    private static final Drawable getDrawableModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }
}