package asywalul.minang.wisatasumbar.adapter;

/**
 * Created by asywalulfikri on 10/9/16.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.model.Video;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;
import asywalul.minang.wisatasumbar.util.Cons;


public class VIdeoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;

    private LikeQuestion mQuestionLike;
    private LikeArticle mArticleLike;
    private ProfileUser mProfileUser;
    private MoreQuestion mQuestionMore;
    private ViewHolder holder;
    private ArrayList<Video> mVideoList = new ArrayList<Video>();
    private PrettyTime mPrettyTime;
    private User user;
    private DataValueOpen mDataValueOpen;
    private ArrayList<Video> mDataList;
    String  useridd;

    public VIdeoAdapter(Context context, String userid) {
        init(context,userid);
    }

    public VIdeoAdapter(Context context, DataValueOpen value, String userid) {
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

    public void setData(ArrayList<Video> list) {
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
            itemView = mInflater.inflate(R.layout.list_item_video, null);

            holder = new ViewHolder();
            holder.playButton           =   (ImageView) itemView.findViewById(R.id.btnYoutube_player);

            holder.relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            holder.youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_view);
            holder. title                = (TextView)itemView.findViewById(R.id.tv_title_category_list);
            holder.totalViews           = (TextView)itemView.findViewById(R.id.tv_play_time);
            holder.avatar               = (ImageView)itemView.findViewById(R.id.iv_logo);
            holder.channel              = (TextView)itemView.findViewById(R.id.tv_from);
            holder.ivWatch              = (ImageView)itemView.findViewById(R.id.iv_watch);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final Video video = mDataList.get(position);

        holder.title.setText(video.title);
        holder.totalViews.setText(String.valueOf(video.totalView));
        holder.channel.setText(video.channel);

        if (video.isWatch == 1) {
            holder.ivWatch.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.ivWatch.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
        }

        Picasso.with(mContext)
                .load((video.avatar.equals("")) ? null : video.avatar)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.avatar);

        holder.playButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.isLogin.equals("")||user.isLogin.equals("null")){
                   // Toast.makeText(mContext,mContext.getString(R.string.text_login_before_video),Toast.LENGTH_SHORT).show();
                    ((BaseActivity)mContext).showDialogLogin(mContext.getString(R.string.text_login_before_video));

                }else {
                    mQuestionLike.OnLikeClickQuestion(v, position);
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, Cons.API_KEY, video.url);
                    mContext.startActivity(intent);
                }
            }
        });


        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
            }
        };

        holder.youTubeThumbnailView.initialize(Cons.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(video.url);
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });


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
        RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        ImageView playButton;
        TextView title ;
        TextView totalViews;
        ImageView avatar;
        TextView channel;
        ImageView ivWatch;
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