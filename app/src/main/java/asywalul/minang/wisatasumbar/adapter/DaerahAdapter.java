package asywalul.minang.wisatasumbar.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Daerah;

public class DaerahAdapter extends RecyclerView.Adapter<DaerahAdapter.ItemViewHolderCategoryList> {

    private Context mContext;

    private ArrayList<Daerah> mDaerahList ;
    View view;
    private OnItemClickListener mListener;
    private MoreCerita mMoreCerita;

    public DaerahAdapter(ArrayList<Daerah> mDaerahListt, Context context) {
        mDaerahList = mDaerahListt;
        mContext = context;
    }
    public void moreCerita(MoreCerita morecerita) {
        mMoreCerita = morecerita;
    }

    @Override
    public ItemViewHolderCategoryList onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item_kabupaten, parent, false);

        return new ItemViewHolderCategoryList(itemView);
    }

    public void setData(ArrayList<Daerah> data) {
        this.mDaerahList = data;
    }

    @Override
    public int getItemCount() {
        return (mDaerahList.size());
    }

    @Override
    public void onBindViewHolder(ItemViewHolderCategoryList holder, final int position) {
        Daerah daerah= mDaerahList.get(position);


        holder.tvNamakabupaten.setText(daerah.namakabupaten);
        holder.tvNamaIbukota.setText(daerah.ibukota);
        holder.tvLuasWilayah.setText(daerah.luaswilayah);

        if (daerah.foto == null || daerah.foto.equals("")) {
            holder.ivFoto.setVisibility(View.GONE);
        } else {
            holder.ivFoto.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse(daerah.foto);
            holder.ivFoto.setImageURI(uri);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mMoreCerita.OnMoreClickCerita(v, position);
            }
        });

    }

    class ItemViewHolderCategoryList extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNamakabupaten;
        TextView tvNamaIbukota;
        SimpleDraweeView ivFoto;
        FrameLayout container;
        TextView tvLuasWilayah;

        public ItemViewHolderCategoryList(View itemView) {
            super(itemView);

            tvNamaIbukota = (TextView) itemView.findViewById(R.id.tv_location);
            tvNamakabupaten = (TextView) itemView.findViewById(R.id.tv_location_name);
            ivFoto = (SimpleDraweeView) itemView.findViewById(R.id.iv_location);
            container = (FrameLayout) itemView.findViewById(R.id.frameLayout);
            tvLuasWilayah = (TextView)itemView.findViewById(R.id.tv_luas_wilayah);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.mListener = clickListener;
    }

    public interface OnItemClickListener {
        public abstract void onItemClick(View view, int position);
    }

    public interface MoreCerita {
        public abstract void OnMoreClickCerita(View view, int position);
    }

}