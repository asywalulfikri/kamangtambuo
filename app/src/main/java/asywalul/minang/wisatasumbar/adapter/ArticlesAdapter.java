package asywalul.minang.wisatasumbar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Articles;
import asywalul.minang.wisatasumbar.util.DateUtil;

public class ArticlesAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;

    private PindahHalaman  mPindahHalaman;
    private PindahHalaman2 mPindahHalaman2;
    private LikeArticle mArticleLike;
    private ProfileUser mProfileUser;
    private MoreQuestion mQuestionMore;
    private ViewHolder holder;
    private ArrayList<Articles> mArticlesList = new ArrayList<Articles>();
    private PrettyTime mPrettyTime;

    private DataValueOpen mDataValueOpen;
    private ArrayList<Articles> mDataList;
    String  useridd;

    public ArticlesAdapter(Context context, String userid) {
        init(context,userid);
    }

    public ArticlesAdapter(Context context, DataValueOpen value, String userid) {
        init(context,userid);

        this.mDataValueOpen = value;
    }

    private void init(Context context, String userid) {
        mContext = context;
        this.useridd = userid;

        if (context != null) {

            mInflater = LayoutInflater.from(context);
            mPrettyTime = new PrettyTime();


        }

    }

    public void setOpenData(DataValueOpen open) {
        mDataValueOpen = open;
    }

    public void setData(ArrayList<Articles> list) {
        mDataList = list;
    }

    public void pindahHalaman(PindahHalaman pindahalaman) {
        mPindahHalaman= pindahalaman;
    }
    public void pindahHalaman2(PindahHalaman2 pindahalaman2) {
        mPindahHalaman2= pindahalaman2;
    }

    public void likeArticle(LikeArticle article) {
        mArticleLike = article;
    }

    public void profileUser(ProfileUser profile) {
        mProfileUser = profile;
    }

    public void moreQuestion (MoreQuestion more) {
        mQuestionMore = more;
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
            itemView = mInflater.inflate(R.layout.list_item_articles, null);

            holder = new ViewHolder();
            holder.tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            holder.ivFoto = (SimpleDraweeView)itemView.findViewById(R.id.iv_location);
            holder.tvTitle = (TextView)itemView.findViewById(R.id.tv_title_resep);
            holder.tvTime  = (TextView)itemView.findViewById(R.id.tv_time);
            holder.cardView = (CardView)itemView.findViewById(R.id.card);

            holder.tvTitle2 = (TextView)itemView.findViewById(R.id.news_summary_title_tv);
            holder.ivFoto1  = (SimpleDraweeView) itemView.findViewById(R.id.news_summary_photo_iv_left);
            holder.ivFoto2  = (SimpleDraweeView) itemView.findViewById(R.id.news_summary_photo_iv_middle);
            holder.ivFoto3  = (SimpleDraweeView) itemView.findViewById(R.id.news_summary_photo_iv_right);
            holder.tvTime2  = (TextView)itemView.findViewById(R.id.news_summary_ptime_tv);
            holder.tvContent2 = (TextView)itemView.findViewById(R.id.tv_contentt);
            holder.cardView2 = (CardView)itemView.findViewById(R.id.card2);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final Articles articles= mDataList.get(position);
        String time = mPrettyTime.format(DateUtil.stringToDateTime(articles.dateSubmitted));
        if (time.equals("yang lalu")) {
            time = "beberapa saat yang lalu";
        }

        if(articles.type.equals("1")) {
            holder.cardView2.setVisibility(View.VISIBLE);
            holder.cardView.setVisibility(View.GONE);

            holder.tvTime2.setText(time + " | " + articles.tags);
            holder.tvTitle2.setText(articles.title);
            holder.tvContent2.setText(articles.summary);

            if (articles.image_satu == null || articles.image_satu.equals("")) {
                holder.ivFoto1.setVisibility(View.GONE);
            } else {
                holder.ivFoto1.setVisibility(View.VISIBLE);

                Uri uri = Uri.parse(articles.image_satu);
                holder.ivFoto1.setImageURI(uri);
            }

            if (articles.image_dua == null || articles.image_dua.equals("")) {
                holder.ivFoto2.setVisibility(View.GONE);
            } else {
                holder.ivFoto2.setVisibility(View.VISIBLE);

                Uri uri = Uri.parse(articles.image_dua);
                holder.ivFoto2.setImageURI(uri);
            }

            if (articles.image_tiga == null || articles.image_tiga.equals("")) {
                holder.ivFoto3.setVisibility(View.GONE);
            } else {
                holder.ivFoto3.setVisibility(View.VISIBLE);

                Uri uri = Uri.parse(articles.image_tiga);
                holder.ivFoto3.setImageURI(uri);
            }

            holder.cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPindahHalaman2.OnLikeClickPindahHalaman2(v,position);
                }
            });



        }else {

            holder.cardView2.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
            holder.tvContent.setText(articles.summary);
            holder.tvTitle.setText(articles.title);
            holder.tvTime.setText(time + " | " + articles.tags);


            if (articles.image_satu == null || articles.image_satu.equals("")) {
                holder.ivFoto.setVisibility(View.GONE);
            } else {
                holder.ivFoto.setVisibility(View.VISIBLE);

                Uri uri = Uri.parse(articles.image_satu);
                holder.ivFoto.setImageURI(uri);
            }


        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPindahHalaman.OnLikeClickPindahHalaman(v,position);
            }
        });




        return itemView;
    }

    public interface DataValueOpen {
        public void setValueOpen(String id, String value);
    }

    public interface PindahHalaman {
        public abstract void OnLikeClickPindahHalaman(View view, int position);
    }
    public interface PindahHalaman2 {
        public abstract void OnLikeClickPindahHalaman2(View view, int position);
    }

    public interface LikeArticle {
        public abstract void OnLikeClickArticle(View view, int position);
    }
    public interface MoreQuestion {
        public abstract void OnMoreClickQuestion(View view, int position);
    }
    public interface ProfileUser {
        public abstract void OnClickProfile(View view, int position);
    }

    static class ViewHolder {

        TextView tvContent;
        SimpleDraweeView ivFoto;
        TextView tvTitle;
        TextView tvTime;
        CardView cardView;

        TextView tvTitle2;
        SimpleDraweeView ivFoto1;
        SimpleDraweeView ivFoto2;
        SimpleDraweeView ivFoto3;
        TextView tvTime2;
        TextView tvContent2;
        CardView cardView2;
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