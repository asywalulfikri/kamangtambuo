package asywalul.minang.wisatasumbar.adapter;

import android.content.Context;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Store;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StoreAdapter extends HFRecyclerViewAdapter<Store,StoreAdapter.ItemViewHolder> {

    private Context mContext;

    private PrettyTime mPrettyTime;
    private int lastPosition = -1;
    View view;
    private OnItemClickListener mListener;

    public StoreAdapter(Context context) {
        super(context);
        this.mContext = context;

        mPrettyTime = new PrettyTime();
    }

    @Override
    public ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_store, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void footerOnVisibleItem() {

    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, int position) {


        Store store = getData().get(position);


        holder.tvNamabarang.setText(store.namabarang);
        holder.tvHargabarang.setText(String.valueOf(store.hargabarang));
        holder.tvLokasi.setText(store.lokasi);
        holder.tvUsername.setText(store.user.fullName);

        if(store.user.avatar == null || store.user.avatar.equals("")){

        } else {
            Picasso.with(mContext)
                    .load((store.user.avatar.equals("")) ? null : store.user.avatar)
                    .placeholder(getDrawableModify(mContext, R.drawable.ic_profile_blank))
                    .error(R.drawable.ic_profile_blank)
                    .into(holder.avatar);
        }



        if (store.pathh == null || store.pathh.equals("")) {
            holder.ivFoto.setVisibility(View.GONE);
        } else {
            holder.ivFoto.setVisibility(View.VISIBLE);

            Picasso.with(mContext) //
                    .load((store.pathh.equals("")) ? null : store.pathh) //
                    .placeholder(getDrawableModify(mContext, R.drawable.no_image))
                    .error(R.drawable.no_image)
                    .into(holder.ivFoto);
        }
        setAnimation(holder.container, position);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNamabarang;
        TextView tvHargabarang;
        TextView tvUsername;
        ImageView avatar;
        ImageView ivFoto;
        TextView tvLokasi;
        CardView container;


        public ItemViewHolder(View itemView) {
            super(itemView);

            tvNamabarang = (TextView) itemView.findViewById(R.id.tv_title_resep);
            tvHargabarang = (TextView) itemView.findViewById(R.id.tv_content);
            tvUsername = (TextView)itemView.findViewById(R.id.tv_seller_name);
            avatar = (ImageView)itemView.findViewById(R.id.iv_seller);
            container = (CardView)itemView.findViewById(R.id.card);
            tvLokasi = (TextView)itemView.findViewById(R.id.tv_seller_location);
            ivFoto = (ImageView)itemView.findViewById(R.id.iv_location);

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

    private long timeStringtoMilis(String time) {
        long milis = 0;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date 	= sd.parse(time);
            milis 		= date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return milis;
    }

    private static final Drawable getDrawableModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
