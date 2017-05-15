package asywalul.minang.wisatasumbar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkTextView;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Comment;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;
import asywalul.minang.wisatasumbar.util.TimeUtil;


public class CommentListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;

	private FullImage mFullImage;
	private Option mOption;

	private PrettyTime mPrettyTime;

	private List<Comment> mDataList;

	private User user;

	private String mOwner;

	public CommentListAdapter(Context context) {
		mContext		= context;
		mInflater 		= LayoutInflater.from(context);

		mPrettyTime 	= new PrettyTime();

		user			= ((BaseActivity) context).getUser();
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	public void setData(List<Comment> list) {
		mDataList = list;
	}

	public void setContentOwner(String userId) {
		mOwner = userId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (mDataList == null) ? 0 : mDataList.size();
	}

	public void fullImage(FullImage fullImage){
		mFullImage = fullImage;
	}

	public void option(Option option){
		mOption = option;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView			=  mInflater.inflate(R.layout.list_item_comment, null);

			holder 				= new ViewHolder();
			holder.avatarIv		= (ImageView) convertView.findViewById(R.id.iv_avatar);
			holder.timeTv		= (TextView) convertView.findViewById(R.id.tv_time);
			holder.authorTv		= (TextView) convertView.findViewById(R.id.tv_author);
			holder.commentTv	= (AutoLinkTextView) convertView.findViewById(R.id.tv_comment);

			holder. commentTv.addAutoLinkMode(
					AutoLinkMode.MODE_HASHTAG,
					AutoLinkMode.MODE_PHONE,
					AutoLinkMode.MODE_URL,
					AutoLinkMode.MODE_MENTION,
					AutoLinkMode.MODE_CUSTOM);
			holder.commentTv.setCustomRegex("\\sAllo\\b");

			holder. commentTv.setHashtagModeColor(ContextCompat.getColor(mContext, R.color.instagram));
			holder. commentTv.setPhoneModeColor(ContextCompat.getColor(mContext, R.color.instagram));
			holder.commentTv.setCustomModeColor(ContextCompat.getColor(mContext, R.color.instagram));
			holder.commentTv.setUrlModeColor(ContextCompat.getColor(mContext, R.color.instagram));
			holder.commentTv.setMentionModeColor(ContextCompat.getColor(mContext, R.color.instagram));
			holder.commentTv.setEmailModeColor(ContextCompat.getColor(mContext, R.color.instagram));
			holder. commentTv.setSelectedStateColor(ContextCompat.getColor(mContext, R.color.instagram));

			holder.ivMoreAction = (ImageView)convertView.findViewById(R.id.iv_more_action);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Comment comment = mDataList.get(position);

		holder.timeTv.setText(TimeUtil.unixToTimeAgo(comment.timeunix));

		holder.authorTv.setText(comment.user.fullName);
		holder.commentTv.setAutoLinkText(comment.content);

        if(comment.user.userId.equals(user.userId)){
			holder.ivMoreAction.setVisibility(View.VISIBLE);
		} if(mOwner.equals(user.userId)) {
			holder.ivMoreAction.setVisibility(View.VISIBLE);
		}



		holder.ivMoreAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mOption.onOptionClick(v, position);
			}
		});

		Picasso.with(mContext) //
				.load((comment.user.avatar.equals("")) ? null : comment.user.avatar) //
				.placeholder(R.drawable.ic_profile_blank) //
				.error(R.drawable.ic_profile_blank) //
				.into(holder.avatarIv);

		return convertView;
	}

	public interface FullImage {
		public abstract void OnFullImageClick(View view, int position);
	}

	public interface Option {
		public abstract void onOptionClick(View view, int position);
	}

	static class ViewHolder {
		public View option;
		ImageView avatarIv;
		TextView authorTv;
		AutoLinkTextView commentTv;
		TextView timeTv;
		ImageView ivMoreAction;
	}

}