package asywalul.minang.wisatasumbar.adapter;

import android.content.Context;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Kuliner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KulinerAdapter extends HFRecyclerViewAdapter<Kuliner,KulinerAdapter.ItemViewHolder> {

    private Context mContext;

    private PrettyTime mPrettyTime;
    private int lastPosition = -1;
    View view;
    private OnItemClickListener mListener;

    public KulinerAdapter(Context context) {
        super(context);
        this.mContext = context;

        mPrettyTime = new PrettyTime();
    }

    @Override
    public ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kuliner, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void footerOnVisibleItem() {

    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, int position) {


        Kuliner kuliner = getData().get(position);


        holder.tvNamabarang.setText(kuliner.namakuliner);

        if (kuliner.foto == null || kuliner.foto.equals("")) {
            holder.ivFoto.setVisibility(View.GONE);
        } else {
            holder.ivFoto.setVisibility(View.VISIBLE);

            Picasso.with(mContext) //
                    .load((kuliner.foto.equals("")) ? null : kuliner.foto) //
                    .placeholder(R.drawable.progress) //
                    .error(R.drawable.no_image)
                    .into(holder.ivFoto);
        }

    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvNamabarang;
        ImageView ivFoto;
        CardView container;


        public ItemViewHolder(View itemView) {
            super(itemView);

            tvNamabarang = (TextView) itemView.findViewById(R.id.tv_title_resep);
            ivFoto = (ImageView)itemView.findViewById(R.id.iv_location);
            container = (CardView)itemView.findViewById(R.id.card);


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
}
