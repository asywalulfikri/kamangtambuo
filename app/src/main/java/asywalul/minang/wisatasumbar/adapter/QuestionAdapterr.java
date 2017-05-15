package asywalul.minang.wisatasumbar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.joooonho.SelectableRoundedImageView;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;
import asywalul.minang.wisatasumbar.util.DateUtil;
import asywalul.minang.wisatasumbar.util.TimeUtil;
import asywalul.minang.wisatasumbar.util.camera.StringUtils;
import asywalul.minang.wisatasumbar.widget.AdjustableImageView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;

public class QuestionAdapterr extends BaseAdapter {

    private LayoutInflater mInflater;
    private Typeface mFont;
    private Context mContext;

    private LikeQuestion mQuestionLike;
    private PindahHalaman mPindahHalaman;
    private TagVieww mTagView;
    private LikeArticle mArticleLike;
    private ProfileUser mProfileUser;
    private MoreQuestion mQuestionMore;
    private ViewHolder holder;
    private PrettyTime mPrettyTime;

    private DataValueOpen mDataValueOpen;
    private ArrayList<Conversation> mDataList;
    String  useridd;
    User user;

    public QuestionAdapterr(Context context, String userid) {
        init(context,userid);
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

    public void setData(ArrayList<Conversation> list) {
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

    public void pindahHalaman(PindahHalaman pindahhalaman) {
        mPindahHalaman = pindahhalaman;
    }
    public void tagView (TagVieww tagVieww){
        mTagView = tagVieww;
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
            itemView = mInflater.inflate(R.layout.list_item_question, null);

            holder = new ViewHolder();
            holder.tvContent = (AutoLinkTextView) itemView.findViewById(R.id.tv_q_content);

            holder. tvContent.addAutoLinkMode(
                    AutoLinkMode.MODE_HASHTAG,
                    AutoLinkMode.MODE_PHONE,
                    AutoLinkMode.MODE_URL,
                    AutoLinkMode.MODE_MENTION,
                    AutoLinkMode.MODE_CUSTOM);
            holder.tvContent.setCustomRegex("\\sAllo\\b");
            holder.rl_event = (RelativeLayout)itemView.findViewById(R.id.rl_event);
            holder. tvContent.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            holder. tvContent.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            holder.tvContent.setCustomModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            holder.tvContent.setUrlModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            holder.tvContent.setMentionModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            holder.tvContent.setEmailModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            holder. tvContent.setSelectedStateColor(ContextCompat.getColor(mContext, R.color.instagram));
            holder.tvTags             = (TextView) itemView.findViewById(R.id.tv_q_date);
            holder.tvDate             = (TextView)itemView.findViewById(R.id.tv_q_desc);
            holder.ivFoto             = (SimpleDraweeView)itemView.findViewById(R.id.iv_q_photo);
            holder.username           = (TextView)itemView.findViewById(R.id.tv_q_author);
           // holder.ivFoto.setCornerRadiiDP(3, 3, 3, 3);
            holder.avatar             = (ImageView)itemView.findViewById(R.id.iv_q_author);
            holder.ivLikeQuestionHome = (RelativeLayout)itemView.findViewById(R.id.rlLikeQuestion);
            holder.tvCommentQuestionHome = (TextView)itemView.findViewById(R.id.tvCommentQuestionHome);
            holder.tvLikeQuestionHome = (TextView)itemView.findViewById(R.id.tvLikeQuestionHome);
            holder.qLikeQuestion      = (ImageView) itemView.findViewById(R.id.ivLikeQuestionHome);
            holder. qComment          = (ImageView)itemView.findViewById(R.id.ivCommentQuestionHome);
            holder.LAYOUT_QUESTIONS   = (CardView)itemView.findViewById(R.id.layout_questions);
            holder.moreAction         = (ImageView)itemView.findViewById(R.id.iv_more_action);
            holder.lllikeQuestion     = (LinearLayout)itemView.findViewById(R.id.lllikequestion);
            holder.mViewTagsLL        = (View)itemView.findViewById(R.id.ll_tags);
            holder.mTagsView          = (TagView)itemView.findViewById(R.id.tag_group);


            //articles
            holder.LAYOUT_ARTICLES = (CardView)itemView.findViewById(R.id.layout_events);
            holder.aTitle          = (TextView)itemView.findViewById(R.id.tv_event_title);
            holder.aContent        = (TextView)itemView.findViewById(R.id.tv_headline);
            holder.aPhoto          = (SimpleDraweeView)itemView.findViewById(R.id.iv_event);

            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final Conversation conversation = mDataList.get(position);


        if(conversation.type.equals("questions")){
            holder.LAYOUT_QUESTIONS.setVisibility(View.VISIBLE);
            holder.LAYOUT_ARTICLES.setVisibility(View.GONE);
            holder.tvContent.setAutoLinkText(conversation.content);
            holder.tvTags.setText(mContext.getString(R.string.text_category)+":" +conversation.tags);
            holder.username.setText(conversation.user.fullName);
            holder.tvCommentQuestionHome.setText(String.valueOf(conversation.totalResponses));
            holder.tvLikeQuestionHome.setText(String.valueOf(conversation.totalVotes));

            if (conversation.tags == null || conversation.equals("")) {
                holder.mViewTagsLL.setVisibility(View.GONE);
            } else {
                holder.mViewTagsLL.setVisibility(View.VISIBLE);
                ArrayList<Tag> tags = new ArrayList<>();
                Tag tag;
                String[] words = conversation.tags.split(",",4);
                //String[] words = {words};
                List<String> stringList = new ArrayList<String>(Arrays.asList(words));
                holder.mTagsView.removeAll();
                for (int i = 0; i < stringList.size(); i++) {
                        tag = new Tag(stringList.get(i));
                        tag.radius = 10f;
                        tag.layoutColor = Color.parseColor("#" + Integer.toHexString(mContext.getResources().getColor(R.color.text_color_white)));
                        tag.layoutBorderColor = Color.parseColor("#" + Integer.toHexString(mContext.getResources().getColor(R.color.text_like)));
                        tag.layoutBorderSize = 1f;
                        tag.tagTextColor = Color.parseColor("#" + Integer.toHexString(mContext.getResources().getColor(R.color.text_like)));
                        tag.tagTextSize = 12f;
                        tags.add(tag);
                        holder.mTagsView.addTags(tags);
                    }
            }

            holder.mTagsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



            if(conversation.dateSubmitted.equals(null)||conversation.dateSubmitted.equals("")){
                 if(conversation.time.equals(null)||conversation.dateSubmitted.equals("")){
                     holder.tvDate.setVisibility(View.GONE);
                 }else {
                     holder.tvDate.setVisibility(View.GONE);
                 }
            }else {
                holder.tvDate.setText(TimeUtil.unixToTimeAgo(conversation.dateSubmitted));
            }

            if(conversation.totalResponses==0){
                holder.tvCommentQuestionHome.setVisibility(View.GONE);
            }else{
                holder.tvCommentQuestionHome.setVisibility(View.VISIBLE);
            }
            if(conversation.totalVotes==0) {
                holder.tvLikeQuestionHome.setVisibility(View.GONE);
            }else {
                holder.tvLikeQuestionHome.setVisibility(View.VISIBLE);
            }

            if (conversation.isVoted == 1) {
                holder.qLikeQuestion.setColorFilter(Color.parseColor("#D50000"));
            }else if(conversation.isVoted ==0) {
                holder.qLikeQuestion.setColorFilter(Color.parseColor("#bdbdbd"));
            }

            if(conversation.totalResponses>0) {
                holder.qComment.setColorFilter(R.color.text_article_headtitle);

            }else {
                holder.qComment.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
            }


            if(conversation.user.avatar == null || conversation.user.avatar.equals("")){

            } else {
                Picasso.with(mContext)
                        .load((conversation.user.avatar.equals("")) ? null : conversation.user.avatar)
                        .placeholder(R.drawable.ic_profile_blank)
                        .error(R.drawable.ic_profile_blank)
                        .into(holder.avatar);
            }



            if (conversation.attachment == null || conversation.attachment.equals("")) {
                holder.ivFoto.setVisibility(View.GONE);
            } else {
                holder.ivFoto.setVisibility(View.VISIBLE);

               /* Picasso.with(mContext) //
                        .load((conversation.attachment.equals("")) ? null : conversation.attachment) //
                        .placeholder(getDrawableModify(mContext, R.drawable.progres_animation))
                        .error(R.drawable.no_image)
                        .into(holder.ivFoto);*/
                Uri uri = Uri.parse(conversation.attachment);
                holder.ivFoto.setImageURI(uri);

            }

            //setAnimation(holder.LAYOUT_QUESTIONS, position);
            holder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPindahHalaman.OnLikeClickPindahHalaman(v,position);
                }
            });
            holder.qLikeQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mQuestionLike.OnLikeClickQuestion(v, position);
                }
            });

            holder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProfileUser.OnClickProfile(v, position);
                }
            });


            holder.moreAction.setVisibility(View.VISIBLE);

            holder.moreAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mQuestionMore.OnMoreClickQuestion(v, position);
                }
            });

        }else {
            holder.LAYOUT_ARTICLES.setVisibility(View.VISIBLE);
            holder.LAYOUT_QUESTIONS.setVisibility(View.GONE);

            holder.aTitle.setText(conversation.title);
            holder.aContent.setText(conversation.dateSubmitted);
//            holder.aTags.setText(conversation.tags);
//            holder.aDate.setText(time);

            if(conversation.attachment == null || conversation.attachment.equals("")){

            } else {
                /*Picasso.with(mContext)
                        .load((conversation.attachment.equals("")) ? null : conversation.attachment)
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(holder.aPhoto);*/
                Uri uri  = Uri.parse(conversation.attachment);
                holder.aPhoto.setImageURI(uri);
            }

            holder.rl_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPindahHalaman.OnLikeClickPindahHalaman(v,position);
                }
            });


        }




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
    public interface PindahHalaman {
        public abstract void OnLikeClickPindahHalaman(View view, int position);
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
    public interface TagVieww{
        public abstract void OnClickTag(View view, int position);
    }

    static class ViewHolder {
        //Questions
        AutoLinkTextView tvContent;
        TextView tvTags;
        TextView tvDate;
        SimpleDraweeView ivFoto;
        TextView username;
        CardView LAYOUT_QUESTIONS;
        ImageView avatar;
        RelativeLayout ivLikeQuestionHome ;
        TextView tvCommentQuestionHome;
        TextView tvLikeQuestionHome;
        ImageView qLikeQuestion;
        ImageView qComment;
        ImageView moreAction;
        LinearLayout lllikeQuestion;
        LoadingLayout mLoading;
        RelativeLayout rl_event;
        private View mViewTagsLL;
        private TagView mTagsView;

        //Articles;
        CardView LAYOUT_ARTICLES;
        TextView aTitle;
        TextView aContent;
        TextView aDate;
        SimpleDraweeView aPhoto;

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