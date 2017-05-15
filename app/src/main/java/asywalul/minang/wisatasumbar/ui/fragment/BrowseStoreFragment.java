package asywalul.minang.wisatasumbar.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.ui.activity.KabubatenKotaListActivity;
import asywalul.minang.wisatasumbar.ui.activity.ListWisata;
import asywalul.minang.wisatasumbar.ui.activity.TabKulinerActivity;
import asywalul.minang.wisatasumbar.widget.PageIndicator;


public class BrowseStoreFragment extends BaseFragment {

    RelativeLayout mLayoutSlider;
    private PageIndicator mIndicator;
    private ViewPager mViewPager;
    private RecyclerView mRecyclerView;

    private int[] mResource = {R.drawable.destinasi, R.drawable.jam_gadang,R.drawable.tabuik, R.drawable.pacu_jawi};
    private String[] mResourceTitle = {"RUMAH GADANG","JAM GADANG", "TABUIK", "PACU JAWI"};

    private static final int ANIM_VIEWPAGER_DELAY = 5000;
    private Handler handler;
    private Runnable animateViewPager;
    private boolean stopSliding = false;
    private CustomPagerAdapter mCustomPagerAdapter;
    private CardView wisataalam,wisatabudaya,wisatareligi,wisatakuliner,wisatakeluarga,wisatapantai;


    public BrowseStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mIndicator     = (PageIndicator) view.findViewById(R.id.indicatorHome);
        mViewPager     = (ViewPager) view.findViewById(R.id.pagerBrowseSlider);
        mRecyclerView  = (RecyclerView) view.findViewById(R.id.rv_browse_store);
        mLayoutSlider  = (RelativeLayout)view.findViewById(R.id.rl_home_slider);

        wisataalam     = (CardView)view.findViewById(R.id.custom1);
        wisatakuliner  = (CardView)view.findViewById(R.id.custom2);
        wisatapantai   = (CardView)view.findViewById(R.id.custom3);
        wisatabudaya   = (CardView)view.findViewById(R.id.custom4);
        wisatareligi   = (CardView)view.findViewById(R.id.custom5);
        wisatakeluarga = (CardView)view.findViewById(R.id.custom6);

        wisataalam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), KabubatenKotaListActivity.class);
                i.putExtra("wisata","1");
                startActivity(i);
            }
        });

        wisatakuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TabKulinerActivity.class);
                i.putExtra("wisata","2");
                startActivity(i);
            }
        });

        wisatapantai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListWisata.class);
                i.putExtra("wisata","3");
                startActivity(i);
            }
        });

        wisatabudaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListWisata.class);
                i.putExtra("wisata","4");
                startActivity(i);
            }
        });

        wisatareligi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListWisata.class);
                i.putExtra("wisata","5");
                startActivity(i);
            }
        });

        wisatakeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListWisata.class);
                i.putExtra("wisata","6");
                startActivity(i);
            }
        });



        mLayoutSlider.setFocusable(true);
        mLayoutSlider.setFocusableInTouchMode(true);


        setupSliderHome();

        return view;
    }

    @Override
    public void onResume() {
        if (mResource.length != 0) {
            handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacks(animateViewPager);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResource.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.home_slider, container, false);

            ImageView ivCoverSlide = (ImageView) itemView.findViewById(R.id.iv_home_slider);
            TextView tvTitle = (TextView) itemView.findViewById(R.id.tv_home_slider);

            ivCoverSlide.setImageResource(mResource[position]);
            tvTitle.setText(mResourceTitle[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    private void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(-1);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

    private void setupSliderHome() {
        mCustomPagerAdapter = new CustomPagerAdapter(getActivity());

        mViewPager.setAdapter(mCustomPagerAdapter);
        mIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        runnable(mResource.length);
        handler.postDelayed(animateViewPager,
                ANIM_VIEWPAGER_DELAY);
    }

}
