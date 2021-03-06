package asywalul.minang.wisatasumbar.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.DaerahAdapter;
import asywalul.minang.wisatasumbar.http.DaerahConnection;
import asywalul.minang.wisatasumbar.model.Daerah;
import asywalul.minang.wisatasumbar.model.DaerahWrapper;
import asywalul.minang.wisatasumbar.ui.activity.PostQuestionAcivity;
import asywalul.minang.wisatasumbar.widget.LoadingLayout;
import asywalul.minang.wisatasumbar.widget.SimpleDividerItemDecoration;

/**
 * Created by Toshiba on 3/12/2016.
 */
public class DaerahFragment extends BaseListFragment implements DaerahAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadingLayout;
    private DaerahAdapter mAdapter;
    private DaerahWrapper wrapper;
    private ArrayList<Daerah> mDaerahList = new ArrayList<Daerah>();
    private int mPage = 1;
    private View footerView;
    private RelativeLayout rlLoadMore;
    private ProgressBar progressBar;
    private TextView tvLoadMore;
    protected SwipeRefreshLayout swipContainer;
    private static final int REQ_CODE = 1000;
    private LoadTask mLoadTask;
    FloatingActionButton fab;


    public static StoreFragment newInstance() {
        return new StoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);


        final Toolbar toolbar = (Toolbar)root.findViewById(R.id.toolbar);

        fab = (FloatingActionButton)root.findViewById(R.id.multiple_actions);
        mRecyclerView  = (RecyclerView)root.findViewById(R.id.rv_browse_store);
        mLoadingLayout = (LoadingLayout)root.findViewById(R.id.layout_loading);
        swipContainer  = (SwipeRefreshLayout)root.findViewById(R.id.ptr_layout);
        swipContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(getActivity(),PostQuestionAcivity.class);
                startActivity(pindah);
            }
        });

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity().getApplicationContext()
        ));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

       /* mLayoutManager.setSpanSizeLookup(new MyGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((mAdapter.hasHeader() && mAdapter.isHeader(position)) ||
                        mAdapter.hasFooter() && mAdapter.isFooter(position))
                    return mLayoutManager.getSpanCount();

                return 1;
            }
        });*/

        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.item_footer, mRecyclerView, false);

        rlLoadMore = (RelativeLayout) footerView.findViewById(R.id.rl_load_more);
        progressBar = (ProgressBar) footerView.findViewById(R.id.pb_progress);
        tvLoadMore = (TextView) footerView.findViewById(R.id.tv_loading);

        rlLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                tvLoadMore.setText(getString(R.string.text_loading));
                (mLoadTask = new LoadTask(true)).execute();
            }
        });

        (mLoadTask = new LoadTask(false)).execute();
        return root;
    }

    protected boolean isListVisible() {
        return (mRecyclerView.getVisibility() == View.VISIBLE) ? true : false;
    }


    private void updateView(DaerahWrapper wrapper) {
        showList();
        progressBar.setVisibility(View.GONE);
        tvLoadMore.setText(getString(R.string.text_next));

        Log.e("DDD", "DISINI");
        mAdapter = new DaerahAdapter(wrapper.list,getActivity());
        mAdapter.setData(wrapper.list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(wrapper.list);
        mAdapter.setClickListener(this);
        mAdapter.notifyDataSetChanged();
    }


    protected void showClearEmpty(String message) {
        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

        mRecyclerView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.hideAndEmpty(message, false);
    }

    protected void showList() {
        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Daerah daerah = mDaerahList.get(position);
        Intent intent = new Intent(getActivity(), PostQuestionAcivity.class);

       /* intent.putExtra(Util.getIntentName("group"), group);
        intent.putExtra(Util.getIntentName("position"), position);*/
        startActivityForResult(intent, REQ_CODE);
    }

    public class LoadTask extends AsyncTask<URL, Integer, Long> {
        boolean loadMore;

        protected LoadTask(boolean loadMore) {
            this.loadMore = loadMore;
        }

        @Override
        protected void onPreExecute() {
//            showLoading();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {

                DaerahConnection conn = new DaerahConnection();
                wrapper = conn.getList();
                result = 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
//            if (isAdded()) {
//            doneLoading();
            swipContainer.setRefreshing(false);

            if (result == 1) {
                Log.e("DDD", "DISINI1");
                if (wrapper != null) {
                    if (wrapper.list.size() == 0) {
                        Log.e("DDD", "DISINI2");
                        showClearEmpty(getActivity().getString(R.string.text_no_data));
                    } else {
                        if (loadMore) {
                            Log.e("DDD", "DISINI3");
//                                loadMoreView(wrapper);
                        } else {
                            Log.e("DDD", "DISINI4");
                            mDaerahList = new ArrayList<Daerah>();
                            updateView(wrapper);

                        }
                        for (int i = 0; i < wrapper.list.size(); i++) {
                            mDaerahList.add(wrapper.list.get(i));
                        }
//                            mPage = mPage + 1;
                        showList();

//                            (mUpdateCacheTask = new UpdateCacheTask(wrapper)).execute();
                    }
                } else {
                    showClearEmpty(getActivity().getString(R.string.text_no_data));
                }
            } else {
                showClearEmpty(getActivity().getString(R.string.text_no_data));
            }
//            }
        }
    }

}