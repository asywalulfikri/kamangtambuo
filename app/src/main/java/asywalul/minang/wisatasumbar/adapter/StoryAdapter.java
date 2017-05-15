package asywalul.minang.wisatasumbar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Story;

/**
 * Created by asywalul fikri on 9/10/16.
 */
public class StoryAdapter extends  HFRecyclerViewAdapter<Story,StoryAdapter.StoryInfoHolder> {

    private Context mContext;
    String  useridd;
    private int lastPosition = -1;
    private OnItemClickListener mListener;

    public StoryAdapter(Context context,String userid) {
        super(context);
        this.mContext = context;
        this.useridd = userid;

    }

    @Override
    public void footerOnVisibleItem() {

    }
    public void setClickListener(StoryAdapter.OnItemClickListener clickListener) {
        this.mListener = clickListener;
    }


    @Override
    public StoryInfoHolder  onCreateDataItemViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_story, parent, false);
        lastPosition = viewType;
        return new StoryInfoHolder(v);
    }

    @Override
    public void onBindDataItemViewHolder(final StoryInfoHolder holder, int position) {

        final  Story story = getData().get(position);

        holder.title.setText(story.title);
        holder.username.setText(story.user.fullName+" || ");
        holder.content.setText(story.content);
        holder.time.setText(story.dateSubmitted);
        Picasso.with(mContext)
                .load((story.user.avatar.equals("")) ? null : story.user.avatar)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(holder.avatar);



    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    class StoryInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView avatar;
        TextView title ;
        TextView content;
        TextView time;
        TextView username;


        public StoryInfoHolder(View itemView) {
            super(itemView);

            title                = (TextView)itemView.findViewById(R.id.tv_title_resep);
            content              = (TextView)itemView.findViewById(R.id.tv_content);
            avatar               = (ImageView)itemView.findViewById(R.id.iv_avatar);
            time                 = (TextView)itemView.findViewById(R.id.tv_time);
            username             = (TextView)itemView.findViewById(R.id.tv_username);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }
    public interface OnItemClickListener {
        public abstract void onItemClick(View view, int position);
    }
}