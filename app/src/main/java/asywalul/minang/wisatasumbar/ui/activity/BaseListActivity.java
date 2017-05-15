package asywalul.minang.wisatasumbar.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.widget.FooterView;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;


public class BaseListActivity extends BaseActivity implements OnScrollListener {
	protected ListView mListView;
//	protected ExpandableHeightListView mListView;
	protected FooterView mFooterView;
	protected LoadingLayout mLoadingView;
	
	protected int mPage = 1;
	protected int mListSize = 0;
	
	protected boolean mIsLoading = false;
	protected boolean mLoadMore = false;
	protected boolean mIsRefresh = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_list);
		prepareView();
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mListView.getLastVisiblePosition() >= mListView.getCount()-1 && scrollState == SCROLL_STATE_IDLE) {	        
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
	
	protected void prepareView() {
		mLoadingView	= (LoadingLayout) findViewById(R.id.layout_loading);
		mListView		= (ListView) findViewById(R.id.lv_data);
		
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
		mIsRefresh 	= true;
		mPage		= 1;
		
		loadData();
	}
	
	protected void doneLoading() {
		if (mLoadMore) {
			mFooterView.hideProgress();
		}
		
		mIsLoading 	= false;
		mLoadMore   = false;
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
	
	public void showWarning(String message) {
		
		if (mLoadingView.getVisibility() == View.VISIBLE) {
			mLoadingView.hideAndRetry(message);
		}
	}
	
	protected void showEmpty(String message) {
		//Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
		
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
	
	protected void showFooterView(boolean show) {
		if (!show) {
			if (mFooterView.isShowing()) {
				mFooterView.hide();
			}
		} else {
			if (!mFooterView.isShowing()) {     					
				mFooterView.show();
			}
		}
	}
	
	protected void resetList() {
		mLoadingView.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
	}
	
	protected boolean isListVisible() {
		return (mListView.getVisibility() == View.VISIBLE) ? true : false;
	}	

}
