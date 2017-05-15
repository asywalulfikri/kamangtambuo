package asywalul.minang.wisatasumbar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.joooonho.SelectableRoundedImageView;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;
import asywalul.minang.wisatasumbar.util.TimeUtil;
import asywalul.minang.wisatasumbar.widget.AdjustableImageView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.OnLoadMoreListener;

public class QuestionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<Conversation> mConversationList ;
    private OnItemClickListener mListener;
    private User user;
    private String userId;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    /*OnCLick*/
    private LikeQuestion mQuestionLike;
    private PindahHalaman mPindahHalaman;
    private LikeArticle mArticleLike;
    private ProfileUser mProfileUser;
    private MoreQuestion mQuestionMore;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loading;
    private int visibleThreshold = 5;

    public QuestionAdapter(ArrayList<Conversation> list, Context context, RecyclerView recyclerView) {
        mConversationList = list;
        mContext = context;
        user = ((BaseActivity) context).getUser();
        userId = user.userId;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
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
    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemViewType(int position) {
        return mConversationList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return mConversationList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.list_item_question, parent, false);

            vh = new ItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    public void setData(ArrayList<Conversation> data) {
        this.mConversationList = data;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Conversation conversation = mConversationList.get(position);

            if (conversation.type.equals("questions")) {
                ((ItemViewHolder) holder).LAYOUT_QUESTIONS.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).LAYOUT_ARTICLES.setVisibility(View.GONE);
                ((ItemViewHolder) holder).tvContent.setAutoLinkText(conversation.content);
                ((ItemViewHolder) holder).tvTags.setText("Kategori : " + conversation.tags);
                ((ItemViewHolder) holder).username.setText(conversation.user.fullName);
                ((ItemViewHolder) holder).tvCommentQuestionHome.setText(String.valueOf(conversation.totalResponses));
                ((ItemViewHolder) holder).tvLikeQuestionHome.setText(String.valueOf(conversation.totalVotes));


                ((ItemViewHolder) holder).tvDate.setText(TimeUtil.unixToTimeAgo(conversation.dateSubmitted));

                if (conversation.totalResponses == 0) {
                    ((ItemViewHolder) holder).tvCommentQuestionHome.setVisibility(View.GONE);
                } else {
                    ((ItemViewHolder) holder).tvCommentQuestionHome.setVisibility(View.VISIBLE);
                }
                if (conversation.totalVotes == 0) {
                    ((ItemViewHolder) holder).tvLikeQuestionHome.setVisibility(View.GONE);
                } else {
                    ((ItemViewHolder) holder).tvLikeQuestionHome.setVisibility(View.VISIBLE);
                }

                if (conversation.isVoted == 1) {
                    ((ItemViewHolder) holder).qLikeQuestion.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    ((ItemViewHolder) holder).qLikeQuestion.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                }

                if (conversation.totalResponses > 0) {
                    ((ItemViewHolder) holder).qComment.setColorFilter(R.color.text_article_headtitle);

                } else {
                    ((ItemViewHolder) holder).qComment.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                }


                if (conversation.user.avatar == null || conversation.user.avatar.equals("")) {

                } else {
                    Picasso.with(mContext)
                            .load((conversation.user.avatar.equals("")) ? null : conversation.user.avatar)
                            .placeholder(R.drawable.ic_profile_blank)
                            .error(R.drawable.ic_profile_blank)
                            .into(((ItemViewHolder) holder).avatar);
                }


                if (conversation.attachment == null || conversation.attachment.equals("")) {
                    ((ItemViewHolder) holder).ivFoto.setVisibility(View.GONE);
                } else {
                    ((ItemViewHolder) holder).ivFoto.setVisibility(View.VISIBLE);

                    Picasso.with(mContext) //
                            .load((conversation.attachment.equals("")) ? null : conversation.attachment) //
                            .placeholder(getDrawableModify(mContext, R.drawable.progres_animation))
                            .error(R.drawable.no_image)
                            .into(((ItemViewHolder) holder).ivFoto);

                }

                //setAnimation(holder.LAYOUT_QUESTIONS, position);
                ((ItemViewHolder) holder).tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPindahHalaman.OnLikeClickPindahHalaman(v, position);
                    }
                });
                ((ItemViewHolder) holder).qLikeQuestion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mQuestionLike.OnLikeClickQuestion(v, position);
                    }
                });

                ((ItemViewHolder) holder).avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProfileUser.OnClickProfile(v, position);
                    }
                });


                if (conversation.attachment.equals("") && conversation.user.userId.equals(user.userId)) {
                    ((ItemViewHolder) holder).moreAction.setVisibility(View.VISIBLE);
                } else if (conversation.attachment.equals(conversation.attachment)) {
                    ((ItemViewHolder) holder).moreAction.setVisibility(View.VISIBLE);
                } else if (conversation.user.userId.equals(user.userId)) {
                    ((ItemViewHolder) holder).moreAction.setVisibility(View.VISIBLE);
                } else {
                    ((ItemViewHolder) holder).moreAction.setVisibility(View.GONE);
                }
                ((ItemViewHolder) holder).moreAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mQuestionMore.OnMoreClickQuestion(v, position);
                    }
                });

            } else {
                ((ItemViewHolder) holder).LAYOUT_ARTICLES.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).LAYOUT_QUESTIONS.setVisibility(View.GONE);

                ((ItemViewHolder) holder).aTitle.setText(conversation.title);
                ((ItemViewHolder) holder).aContent.setText(conversation.summary);

                if (conversation.attachment == null || conversation.attachment.equals("")) {

                } else {
                    Picasso.with(mContext)
                            .load((conversation.attachment.equals("")) ? null : conversation.attachment)
                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .into(((ItemViewHolder) holder).aPhoto);
                }


            }
        } else {
        ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }


    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Questions
        public AutoLinkTextView tvContent;
        public TextView tvTags;
        public TextView tvDate;
        public TextView username;
        public SimpleDraweeView ivFoto;
        public CardView LAYOUT_QUESTIONS;
        public ImageView avatar;
        public RelativeLayout ivLikeQuestionHome ;
        public TextView tvCommentQuestionHome;
        public TextView tvLikeQuestionHome;
        public ImageView qLikeQuestion;
        public ImageView qComment;
        public ImageView moreAction;
        public LinearLayout lllikeQuestion;
        public LoadingLayout mLoading;


        //Articles;
        public CardView LAYOUT_ARTICLES;
        public TextView aTitle;
        public TextView aContent;
        public TextView aDate;
        public AdjustableImageView aPhoto;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvContent = (AutoLinkTextView) itemView.findViewById(R.id.tv_q_content);
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
            ivFoto             = (SimpleDraweeView)itemView.findViewById(R.id.iv_q_photo);
            username           = (TextView)itemView.findViewById(R.id.tv_q_author);
            avatar             = (ImageView)itemView.findViewById(R.id.iv_q_author);
            ivLikeQuestionHome = (RelativeLayout)itemView.findViewById(R.id.rlLikeQuestion);
            tvCommentQuestionHome = (TextView)itemView.findViewById(R.id.tvCommentQuestionHome);
            tvLikeQuestionHome = (TextView)itemView.findViewById(R.id.tvLikeQuestionHome);
            qLikeQuestion      = (ImageView) itemView.findViewById(R.id.ivLikeQuestionHome);
            qComment           = (ImageView)itemView.findViewById(R.id.ivCommentQuestionHome);
            LAYOUT_QUESTIONS   = (CardView)itemView.findViewById(R.id.layout_questions);
            moreAction         = (ImageView)itemView.findViewById(R.id.iv_more_action);
            lllikeQuestion     = (LinearLayout)itemView.findViewById(R.id.lllikequestion);


            //articles
            LAYOUT_ARTICLES = (CardView)itemView.findViewById(R.id.layout_events);
            aTitle          = (TextView)itemView.findViewById(R.id.tv_event_title);
            aContent        = (TextView)itemView.findViewById(R.id.tv_headline);
            aPhoto          = (AdjustableImageView)itemView.findViewById(R.id.iv_event);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
    public void setClickListener(OnItemClickListener clickListener) {
        this.mListener = clickListener;
    }

    public interface OnItemClickListener {
        public abstract void onItemClick(View view, int position);
    }
    private static final Drawable getDrawableModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
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
}