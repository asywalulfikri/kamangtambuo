package asywalul.minang.wisatasumbar.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.DateUtil;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;

/**
 * Created by asywalulfikri on 1/26/17.
 */

public class PesanTravelAdapter extends HFRecyclerViewAdapter<Conversation,PesanTravelAdapter.ItemViewHolder> {

    private Context mContext;
    private LikeQuestion mQuestionLike;
    private LikeArticle mArticleLike;
    private ProfileUser mProfileUser;
    private MoreQuestion mQuestionMore;
    private PrettyTime mPrettyTime;
    private int lastPosition = -1;

    View view;

    String  useridd;
    private OnItemClickListener mListener;

    public PesanTravelAdapter(Context context,String userid) {
        super(context);
        this.mContext = context;
        mPrettyTime = new PrettyTime();
        this.useridd = userid;

    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
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
    public ItemViewHolder onCreateDataItemViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_question, parent, false);
        lastPosition = viewType;
        return new ItemViewHolder(v);
    }

    @Override
    public void footerOnVisibleItem() {

    }

    @Override
    public void onBindDataItemViewHolder(ItemViewHolder holder, final int position) {

        final Conversation conversation = getData().get(position);
        String time = mPrettyTime.format(DateUtil.stringToDateTime(conversation.dateSubmitted));


        if(conversation.type.equals(Cons.TYPE_QUESTIONS)){
            holder.LAYOUT_QUESTIONS.setVisibility(View.VISIBLE);
            holder.LAYOUT_ARTICLES.setVisibility(View.GONE);
            holder.tvContent.setAutoLinkText(conversation.content);
            holder.tvTags.setText("Kategori : " +conversation.tags);
            holder.username.setText(conversation.user.fullName);
            holder.tvCommentQuestionHome.setText(String.valueOf(conversation.totalResponses));
            holder.tvLikeQuestionHome.setText(String.valueOf(conversation.totalVotes));

            if (time.equals("yang lalu")) {
                time = "beberapa saat yang lalu";
            }
            holder.tvDate.setText(time);

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

                Picasso.with(mContext) //
                        .load((conversation.attachment.equals("")) ? null : conversation.attachment) //
                        .placeholder(getDrawableModify(mContext, R.drawable.progres_animation))
                        .error(R.drawable.no_image)
                        .into(holder.ivFoto);

            }

            //setAnimation(holder.LAYOUT_QUESTIONS, position);

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


            if(conversation.attachment.equals("")&& conversation.user.userId.equals(useridd)){
                holder.moreAction.setVisibility(View.VISIBLE);
            }else if(conversation.attachment.equals(conversation.attachment)){
                holder.moreAction.setVisibility(View.VISIBLE);
            }else if(conversation.user.userId.equals(useridd)) {
                holder.moreAction.setVisibility(View.VISIBLE);
            }else {
                holder.moreAction.setVisibility(View.GONE);
            }
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
            holder.aContent.setText(conversation.summary);
            holder.aTags.setText(conversation.tags);
            holder.aAuthor.setText(conversation.user.fullName);
            holder.aDate.setText(time);
            holder.aVote.setText(String.valueOf(conversation.totalVotes));
            holder.aResponse.setText(String.valueOf(conversation.totalResponses));

            if(conversation.attachment == null || conversation.attachment.equals("")){

            } else {
                Picasso.with(mContext)
                        .load((conversation.attachment.equals("")) ? null : conversation.attachment)
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(holder.aPhoto);
            }

            if(conversation.totalVotes==0) {
                holder.aVote.setVisibility(View.GONE);
            }else {
                holder.aVote.setVisibility(View.VISIBLE);
            }

            holder.likeArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mArticleLike.OnLikeClickArticle(v, position);
                }
            });

            if(conversation.totalResponses>0) {
                holder.aComment.setColorFilter(R.color.text_article_headtitle);

            }else {
                holder.aComment.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
            }


        }


    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Questions
        AutoLinkTextView tvContent;
        TextView tvTags;
        TextView tvDate;
        SelectableRoundedImageView ivFoto;
        TextView username;
        CardView LAYOUT_QUESTIONS;
        ImageView avatar;
        RelativeLayout ivLikeQuestionHome ;
        TextView tvCommentQuestionHome;
        TextView tvLikeQuestionHome;
        // ImageView  qLikeQuestion;
        ImageView qLikeQuestion;
        ImageView qComment;
        ImageView moreAction;
        LinearLayout lllikeQuestion;
        LoadingLayout mLoading;


        //Articles;
        CardView LAYOUT_ARTICLES;
        TextView aTitle;
        TextView aContent;
        TextView aDate;
        TextView aTags;
        TextView aAuthor;
        TextView aVote;
        TextView aResponse;
        SelectableRoundedImageView aPhoto;
        ImageView aLikeArtice;
        ImageView aComment;
        LinearLayout likeArticle;



        public ItemViewHolder(View itemView) {
            super(itemView);
            //questions
            tvContent          = (AutoLinkTextView) itemView.findViewById(R.id.tv_q_content);

            tvContent.addAutoLinkMode(
                    AutoLinkMode.MODE_HASHTAG,
                    AutoLinkMode.MODE_PHONE,
                    AutoLinkMode.MODE_URL,
                    AutoLinkMode.MODE_MENTION,
                    AutoLinkMode.MODE_CUSTOM);
            tvContent.setCustomRegex("\\sAllo\\b");

            tvContent.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            tvContent.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            tvContent.setCustomModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            tvContent.setUrlModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            tvContent.setMentionModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            tvContent.setEmailModeColor(ContextCompat.getColor(mContext, R.color.instagram));
            tvContent.setSelectedStateColor(ContextCompat.getColor(mContext, R.color.instagram));
            tvTags             = (TextView) itemView.findViewById(R.id.tv_q_date);
            tvDate             = (TextView)itemView.findViewById(R.id.tv_q_desc);
            ivFoto             = (SelectableRoundedImageView)itemView.findViewById(R.id.iv_q_photo);
            username           = (TextView)itemView.findViewById(R.id.tv_q_author);
            ivFoto.setCornerRadiiDP(6, 6, 6, 6);
            avatar             = (ImageView)itemView.findViewById(R.id.iv_q_author);
            ivLikeQuestionHome = (RelativeLayout)itemView.findViewById(R.id.rlLikeQuestion);
            tvCommentQuestionHome = (TextView)itemView.findViewById(R.id.tvCommentQuestionHome);
            tvLikeQuestionHome = (TextView)itemView.findViewById(R.id.tvLikeQuestionHome);
            // qLikeQuestion      = (LikeButton) itemView.findViewById(R.id.ivLikeQuestionHome);
            qComment           = (ImageView)itemView.findViewById(R.id.ivCommentQuestionHome);
            LAYOUT_QUESTIONS   = (CardView)itemView.findViewById(R.id.layout_questions);
            moreAction         = (ImageView)itemView.findViewById(R.id.iv_more_action);
            lllikeQuestion     = (LinearLayout)itemView.findViewById(R.id.lllikequestion);
            mLoading           = (LoadingLayout)itemView.findViewById(R.id.layout_loading);


            //articles
            // LAYOUT_ARTICLES = (CardView)itemView.findViewById(R.id.layout_articles);
            aTitle          = (TextView)itemView.findViewById(R.id.tv_location_name);
            aContent        = (TextView)itemView.findViewById(R.id.tv_headline);
            // aPhoto          = (SelectableRoundedImageView)itemView.findViewById(R.id.iv_photo);
            aPhoto.setCornerRadiiDP(6, 6, 6, 6);
            aAuthor         = (TextView)itemView.findViewById(R.id.tv_author);
            aDate           = (TextView)itemView.findViewById(R.id.tv_time);
    /*        aTags           = (TextView)itemView.findViewById(R.id.tv_tags);
            aVote           = (TextView)itemView.findViewById(R.id.tvLikeArticleHome);
            aResponse       = (TextView)itemView.findViewById(R.id.tvCommentArticleHome);
            aLikeArtice     = (LikeButton)itemView.findViewById(R.id.ivLikeArticleHome);
            aComment        = (ImageView)itemView.findViewById(R.id.ivCommentArticleHome);
            likeArticle     = (LinearLayout)itemView.findViewById(R.id.lllike);*/


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

    /* private void setAnimation(View viewToAnimate, int position) {
         if (position > lastPosition) {
             Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_in);
             viewToAnimate.startAnimation(animation);
             lastPosition = position;
         }
     }*/
    private static final Drawable getDrawableModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

}

