package asywalul.minang.wisatasumbar.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Articles;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.PageIndicator;

/**
 * Created by Toshiba on 4/13/2016.
 */
public class ArticleDetailActivity extends BaseActivity  {


    private TextView tvDate;
    private Articles mArticles;
    private int mPosition =0;
    private Button sumber;
    public static final String sendImage = "imageFull";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    private String sumberr;
    private AdView mAdView;
    private FloatingActionButton btnMaps;

    private PageIndicator mIndicator;
    private ViewPager mViewPager;
    RelativeLayout mLayoutSlider;
    private CustomPagerAdapter mCustomPagerAdapter;


    private static final int ANIM_VIEWPAGER_DELAY = 5000;
    private Handler handler;
    private boolean stopSliding = false;
    private Runnable animateViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebView content = (WebView) findViewById(R.id.tv_content);
       // ivFoto    = (ImageView)findViewById(R.id.iv_location);
        tvDate     = (TextView)findViewById(R.id.tv_date);
        sumber    = (Button)findViewById(R.id.btn_sumber);
        btnMaps  = (FloatingActionButton)findViewById(R.id.fab);

        mIndicator     = (PageIndicator)findViewById(R.id.indicatorHome);
        mViewPager     = (ViewPager)findViewById(R.id.pagerBrowseSlider);
        mLayoutSlider  = (RelativeLayout)findViewById(R.id.rl_home_slider);


        String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";

       /* mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);*/

        Bundle bundle = getIntent().getExtras();
        mArticles = bundle.getParcelable(Util.getIntentName("articles"));
        mPosition = bundle.getInt(Util.getIntentName("position"));



        CollapsingToolbarLayout mCTollbar   = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar.setTitle(mArticles.title);

        mCTollbar.setTitle(mArticles.title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mArticles.title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //or whatever you used to do on your onOptionItemSelected's android.R.id.home callback
            }
        });
        if(mArticles.latitude.equals("")||mArticles.longitude.equals("")){
            btnMaps.setVisibility(View.GONE);
        }else {
            btnMaps.setVisibility(View.VISIBLE);
            btnMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mArticles.latitude.equals("")&&longitude.equals("")){

                    }else {
                        Intent i = new Intent(ArticleDetailActivity.this,LocationShown.class);
                        i.putExtra("latitude",mArticles.latitude);
                        i.putExtra("longitude",mArticles.longitude);
                        i.putExtra("origin",mArticles.title);
                        startActivity(i);
                    }
                }
            });
        }


        sumberr = mArticles.sumber_url;

        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        String custom =
                "<html>" +
                        head +
                        "<body style='color:#555555;font-size:14px'>" + mArticles.content +
                        "</body>" +
                        "</html>";
        content.loadData(custom, "text/html", "UTF-8");
        tvDate.setText(mArticles.title);
        tvDate.setVisibility(View.GONE);


        sumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(sumberr));
                startActivity(i);

            }
        });


        mLayoutSlider.setFocusable(true);
        mLayoutSlider.setFocusableInTouchMode(true);


        setupSliderHome();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        intent.putExtra(Util.getIntentName("conversation"), mArticles);
        intent.putExtra(Util.getIntentName("position"), mPosition);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    private void setupSliderHome() {
        mCustomPagerAdapter = new CustomPagerAdapter(this);

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
        int[] mResource = {1,2,3};
        runnable(mResource.length);
        handler.postDelayed(animateViewPager,
                ANIM_VIEWPAGER_DELAY);
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        int[] mResource = {1,2,3};
        String[] mResourceTitle = {"", "", ""};
        LayoutInflater mLayoutInflater;
        private String fotonya;

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

            int satu =(mResource[position]);



            if (isNetworkAvailable(mContext)) {
                if(satu==1){
                    fotonya = mArticles.image_satu;
                }
                else  if(satu==2){
                    if(mArticles.image_dua.equals("")||mArticles.image_dua.equals(null)){
                        fotonya = mArticles.image_satu;
                    }else {
                        fotonya = mArticles.image_dua;
                    }

                }else if (satu ==3) {
                    if(mArticles.image_tiga.equals("")){
                        fotonya = mArticles.image_satu;
                    }else {
                        fotonya = mArticles.image_tiga;
                    }

                }
            }  else {
                fotonya = mArticles.image_satu;
            }

            tvTitle.setText(mResourceTitle[position]);

            if(fotonya.equals("")||fotonya.equals("null")){
                ivCoverSlide.setVisibility(View.GONE);
            }

            Picasso.with(mContext)
                    .load((fotonya.equals("")) ? null : fotonya)
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(ivCoverSlide);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (mViewPager.getCurrentItem() == size - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(
                                mViewPager.getCurrentItem() + 1, true);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }
    @Override
    public void onResume() {
        int[] mResource = {1,2,3};
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


}