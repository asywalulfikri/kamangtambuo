package asywalul.minang.wisatasumbar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
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
import asywalul.minang.wisatasumbar.model.Wisata;
import asywalul.minang.wisatasumbar.util.DateUtil;

public class WisataAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Typeface mFont;
    private Context mContext;

    private LikeQuestion mQuestionLike;
    private LikeArticle mArticleLike;
    private ProfileUser mProfileUser;
    private MoreQuestion mQuestionMore;
    private ViewHolder holder;
    private ArrayList<Wisata> mWisataList = new ArrayList<Wisata>();
    private PrettyTime mPrettyTime;

    private DataValueOpen mDataValueOpen;
    private ArrayList<Wisata> mDataList;
    String  useridd;

    public WisataAdapter(Context context, String userid) {
        init(context,userid);
    }

    public WisataAdapter(Context context, DataValueOpen value,String userid) {
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

    public void setData(ArrayList<Wisata> list) {
        mDataList = list;
    }

    public void likeQuestion(LikeQuestion question) {
        mQuestionLike = question;
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
            itemView = mInflater.inflate(R.layout.list_item_wisata, null);

            holder = new ViewHolder();
            holder.tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            holder.ivFoto = (SimpleDraweeView) itemView.findViewById(R.id.iv_location);
            holder.tvTitle = (TextView) itemView.findViewById(R.id.tv_location_name);
            holder.tvLokasi = (TextView)itemView.findViewById(R.id.tv_location);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final Wisata wisata = mDataList.get(position);
        String time = mPrettyTime.format(DateUtil.stringToDateTime(wisata.dateSubmitted));
        if (time.equals("yang lalu")) {
            time = "beberapa saat yang lalu";
        }

        holder.tvContent.setText(wisata.summary);
        holder.tvLokasi.setText(wisata.daerah);
        holder.tvTitle.setText(wisata.title);

        Uri uri = Uri.parse(wisata.attachment);
        holder.ivFoto.setImageURI(uri);


        return itemView;
    }


    public interface DataValueOpen {
        public void setValueOpen(String id, String value);
    }

    public interface LikeQuestion {
        public abstract void OnLikeClickQuestion(View view, int position);
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
        TextView tvLokasi;
        SimpleDraweeView ivFoto;
        TextView tvTitle;

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