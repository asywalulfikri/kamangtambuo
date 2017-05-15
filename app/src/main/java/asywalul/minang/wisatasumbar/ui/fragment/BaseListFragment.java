package asywalul.minang.wisatasumbar.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.Timer;
import java.util.TimerTask;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;
import asywalul.minang.wisatasumbar.widget.ExpandableHeightListView;
import asywalul.minang.wisatasumbar.widget.FooterView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;

public class BaseListFragment extends BaseFragment implements OnScrollListener {
    protected FooterView mFooterView;
    protected LoadingLayout mLoadingView;
    protected ExpandableHeightListView mListView;
    protected int mPage = 1;
    protected int mListSize = 0;

    protected boolean mIsLoading = false;
    protected boolean mLoadMore = false;
    protected boolean mIsRefresh = false;
    public Timer mTimerPage;
    public int totalTimeSec = 0;

    public Timer mTimerPage2;
    public int totalTimeSec2 = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root = inflater.inflate(R.layout.layout_list, container, false);

        prepareView(root);

        return root;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        if (mListView.getLastVisiblePosition() >= mListView.getCount() - 1 && scrollState == SCROLL_STATE_IDLE) {
            if (!mIsLoading && mFooterView.isShowing()) {
                loadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
    }

    protected void enableAutoLoadMore() {
        mListView.setOnScrollListener(this);
    }

    protected void prepareView(View root) {
        mLoadingView	= (LoadingLayout) root.findViewById(R.id.layout_loading);
        mListView		= (ExpandableHeightListView) root.findViewById(R.id.lv_data);
        mListView.setFocusable(false);
        mFooterView		= new FooterView(getActivity(), mListView, false);

        mFooterView.setOnLoadClickListener(new FooterView.OnLoadClickListener() {
            @Override
            public void onLoadClick() {
                loadMore();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                onListItemClick(position);
            }
        });

        mLoadingView.setListener(new LoadingLayout.LoadingLayoutListener() {
            @Override
            public void onRetryClick() {
                mPage = 1;

                loadData();
            }

            @Override
            public void onActionEmptyClick() {
                onActionClick();
            }
        });

    }

    protected void loadData() {
    }

    protected void loadMore() {
        mLoadMore = true;
    }

    protected void onListItemClick(int position) {
    }

    protected void onActionClick() {
    }

    protected void refresh() {
        mIsRefresh = true;
        mPage = 1;

        loadData();
    }

    protected void doneLoading() {
        if (mLoadMore) {
            mFooterView.hideProgress();
        }

        mIsLoading = false;
        mLoadMore = false;
    }

    protected void showLoading() {
        mIsLoading = true;
        if (mLoadingView.getVisibility() == View.GONE) {
            if (mLoadMore) {
                mFooterView.showProgress();
            }
        } else {
            mLoadingView.show(getString(R.string.text_loading));
        }
    }

    protected void showClearEmpty(String message) {
        mListView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mLoadingView.hideAndEmpty(message, false);
    }

    protected void showWarning(String message) {
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.hideAndRetry(message);
        }
    }

    protected void showEmpty(String message) {
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.hideAndEmpty(message, false);
        }
    }

    protected void showEmpty(String message, boolean action) {
        showEmpty(message, action, true);
    }

    protected void showEmpty(String message, boolean action, boolean withtoast) {
        showEmpty(message, action, 0, withtoast);
    }

    protected void showEmpty(String message, boolean action, int icon, boolean withtoast) {
        if (withtoast) Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        if (mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.hideAndEmpty(message, action, icon);
        }
    }

    protected void showList() {
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            mListView.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
        }
    }

    protected void scrollMyListViewToBottom(final int sel) {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(sel);
            }
        });
    }

    public boolean isShowingg() {
        return (mListView.getFooterViewsCount() == 0) ? false : true;
    }

    protected void showFooterView(boolean show) {
        mFooterView = new FooterView(getActivity(), mListView, false);
       mFooterView.show();
    }

    protected void resetList() {
        mLoadingView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }
    public User getUser() {
        return ((BaseActivity) getActivity()).getUser();
    }


    public boolean buildVersion() {
        return ((BaseActivity) getActivity()).buildVersion();
    }

    public void startCountTime() {
        totalTimeSec = 0;

        mTimerPage = new Timer();
        mTimerPage.schedule(new TimerTask() {
            public void run() {
                totalTimeSec++;
            }
        }, 1, 1); //execute every 1 seconds
    }

    public void startCountTime2() {
        totalTimeSec2 = 0;

        mTimerPage2= new Timer();
        mTimerPage2.schedule(new TimerTask() {
            public void run() {
                totalTimeSec2++;
            }
        }, 1, 1); //execute every 1 seconds
    }


    public void errorHandle(VolleyError error) {
        ((BaseActivity) getActivity()).errorHandle(error);
    }

    public void showDialogLogin(String message) {
        ((BaseActivity) getActivity()).showDialogLogin(message);
    }

    public void showError(String message) {
        ((BaseActivity) getActivity()).showError(message);
    }


}