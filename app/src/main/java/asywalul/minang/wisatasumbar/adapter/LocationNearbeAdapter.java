package asywalul.minang.wisatasumbar.adapter;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.LocationNearby;

public class LocationNearbeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Typeface mFont;
    private Context mContext;
    private String urlfoto ="https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=";
    private String googlemapkey = "&key=AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";

    private LikeQuestion mQuestionLike;
    private LikeArticle mArticleLike;
    private ProfileUser mProfileUser;
    private MoreQuestion mQuestionMore;
    private ViewHolder holder;
    private ArrayList<LocationNearby> mWisataList = new ArrayList<LocationNearby>();
    private PrettyTime mPrettyTime;

    private DataValueOpen mDataValueOpen;
    private ArrayList<LocationNearby> mDataList;
    String  useridd;

    public LocationNearbeAdapter(Context context) {
        init(context);
    }

    public LocationNearbeAdapter(Context context, DataValueOpen value) {
        init(context);

        this.mDataValueOpen = value;
    }

    private void init(Context context) {
        mContext = context;

        if (context != null) {

            mInflater = LayoutInflater.from(context);
            mPrettyTime = new PrettyTime();


        }

    }

    public void setOpenData(DataValueOpen open) {
        mDataValueOpen = open;
    }

    public void setData(ArrayList<LocationNearby> list) {
        mDataList = list;
    }

    public void profileUser(ProfileUser profile) {
        mProfileUser = profile;
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
            itemView = mInflater.inflate(R.layout.list_item_location_nearby, null);

            holder = new ViewHolder();
            holder.tvPlaceName     = (TextView) itemView.findViewById(R.id.tv_title_resep);
            holder.ivFoto          = (ImageView) itemView.findViewById(R.id.iv_location);
            holder.tvLocationname  = (TextView) itemView.findViewById(R.id.tv_content);
            holder.tvJambuka       = (TextView)itemView.findViewById(R.id.tv_jambuka);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final LocationNearby location = mDataList.get(position);

        holder.tvPlaceName.setText(location.name);
        holder.tvLocationname.setText(location.location);
        String buka = location.jambuka;
        if(buka.equals("true")){
            holder.tvJambuka.setText(mContext.getString(R.string.buka));
        }else if(buka.equals("false")) {
            holder.tvJambuka.setText(mContext.getString(R.string.tutup));
        }else {
            holder.tvJambuka.setVisibility(View.GONE);
        }
        String path = urlfoto+location.foto+googlemapkey;
        if (location.foto.equals("")||location.foto.equals("null")){
            Picasso.with(mContext) //
                    .load((location.icon.equals("")) ? null : location.icon) //
                    .placeholder(R.drawable.no_image) //
                    .error(R.drawable.no_image)
                    .into(holder.ivFoto);
        }else {
            Picasso.with(mContext) //
                    .load((path.equals("")) ? null : path) //
                    .placeholder(R.drawable.no_image) //
                    .error(R.drawable.no_image)
                    .into(holder.ivFoto);
        }



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

        TextView tvPlaceName;
        TextView tvLocationname;
        ImageView ivFoto;
        TextView tvJambuka;
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