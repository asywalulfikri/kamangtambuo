package asywalul.minang.wisatasumbar.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.util.Cons;

public class FooterView {

	private View mFooterView;
    private TextView mLoadingTv;
    private ListView mListView;
    private ProgressBar mLoadingPb;
    private OnLoadClickListener mListener;
    private Context mContext;

    public FooterView(Context context, ListView listView, boolean left) {
        LayoutInflater inflater = LayoutInflater.from(context);

        mContext        = context;
        mListView       = listView;
        mFooterView     = inflater.inflate((left) ? R.layout.view_footer_loading : R.layout.view_footer_loading_right, null);
        mLoadingTv      = (TextView) mFooterView.findViewById(R.id.tv_loading);
        mLoadingPb      = (ProgressBar) mFooterView.findViewById(R.id.pb_progress);

        mLoadingPb.setVisibility(View.GONE);
       /* mFooterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLoadClick();
            }
        });*/

    }

    public FooterView(Context context, ListView listView) {
        this(context, listView, true);
    }

    public void setOnLoadClickListener(OnLoadClickListener listener) {
        mListener = listener;
    }

    public void hide() {
        mListView.removeFooterView(mFooterView);
    }

    public void show() {
        mListView.addFooterView(mFooterView);
    }

    public void showProgress() {
        mLoadingTv.setText(mContext.getString(R.string.text_loading));
        mLoadingPb.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mLoadingTv.setText(mContext.getString(R.string.text_next));
        mLoadingPb.setVisibility(View.GONE);
    }

    public View getView() {
        return mFooterView;
    }

    public boolean isShowing() {
        return (mListView.getFooterViewsCount() == 0) ? false : true;
    }

    public interface OnLoadClickListener {
        public abstract void onLoadClick();
    }
}
