package asywalul.minang.wisatasumbar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;
import java.util.List;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;
import asywalul.minang.wisatasumbar.ui.activity.CeritaActivity;
import asywalul.minang.wisatasumbar.ui.activity.InterestialLogout;
import asywalul.minang.wisatasumbar.ui.activity.ListLocationNearby;
import asywalul.minang.wisatasumbar.ui.activity.LoginActivity;
import asywalul.minang.wisatasumbar.ui.activity.ProfileActivity;
import asywalul.minang.wisatasumbar.ui.activity.RekomendasiListWisata;
import asywalul.minang.wisatasumbar.ui.activity.ResepListActivity;
import asywalul.minang.wisatasumbar.ui.activity.SearchListActivity;
import asywalul.minang.wisatasumbar.ui.activity.SearchWisata;
import asywalul.minang.wisatasumbar.ui.activity.WeatherActivity;
import asywalul.minang.wisatasumbar.ui.fragment.BrowseStoreFragment;
import asywalul.minang.wisatasumbar.ui.fragment.QuestionFragment;
import asywalul.minang.wisatasumbar.ui.fragment.QuestionFragment2;
import asywalul.minang.wisatasumbar.ui.fragment.VideoFragment;
import asywalul.minang.wisatasumbar.ui.fragment.angkutan.AngkutanPariwisata;
import asywalul.minang.wisatasumbar.ui.fragment.articles.ArticlesFragmentBudaya;
import asywalul.minang.wisatasumbar.ui.fragment.articles.ArticlesFragmentKuliner;
import asywalul.minang.wisatasumbar.ui.fragment.articles.ArticlesFragmentWisata;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.JSONParser;
import asywalul.minang.wisatasumbar.widget.CircleTransform;
import asywalul.minang.wisatasumbar.widget.CustomTypefaceSpan;
import asywalul.minang.wisatasumbar.widget.FullImageActivity;

public class MainActivityBeranda extends BaseActivity {

    private TextView tvNamauser;
    private SimpleDraweeView ivFotouser;
    private SearchHistoryTable mHistoryDatabase;
    private List<SearchItem> mSuggestionsList;
    private SearchView mSearchView;
    private int mVersion = SearchView.VERSION_MENU_ITEM;
    private int mTheme = SearchView.THEME_LIGHT;
    private String url;
    private final static String SP = "sharedAt";
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private ImageView avatar;
    private Button logout;
    private LinearLayout mWeatherLl;
    private String[] fill_profile = new String[10];
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    String urlfoto = "http://wisatasumbar.esy.es/restful/";
    private User user;
    private View mDrawerHeaderView;
    private NavigationView mNavigationView;
    private String isLogin = "0";
    private Menu mDrawerMenu;
    private  DrawerLayout drawer;
    private int[] groupChoice = {0, 0, 0};
    private int mCurrentGrouping = 0;
    private TextView YoutTube;
    private TextView Instagram;
    private TextDrawable drawable;
    //private GPSTracker mGpsTracker;

    private static final String TAG = "MainActivityBeranda";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getFirstTimeOpen()) {
            saveDefaultLanguage("id");
            setupLanguage();
            saveFirstTimeOpen();
        }
        setContentView(R.layout.activity_mainn);
        user = getUser();

        url = Cons.URL_DETAIL_USER + user.userId;
        Log.d("urx", url);

        isLogin = user.isLogin;
        if (isLogin.equals(null) || isLogin.equals("")) {

        }
        toobar();
        updateView();
        setSearchView();
//        initDatabase();

    }


    private void showSearchView() {
        mSuggestionsList.clear();
        mSuggestionsList.addAll(mHistoryDatabase.getAllItems(1));
        mSearchView.open(true);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    public void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);
        mSuggestionsList = new ArrayList<>();

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setVersion(mVersion);
        mSearchView.setTheme(mTheme, true);
        mSearchView.setDivider(false);
        mSearchView.setHint("Search");
        mSearchView.setHint("Cari Cerita");
        mSearchView.setTextSize(12);
        mSearchView.setVoice(true);
        mSearchView.setVoiceText("Voice");
        mSearchView.setAnimationDuration(360);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.close(false);
                mHistoryDatabase.addItem(new SearchItem(query));
                Intent intent = new Intent(getActivity(), SearchWisata.class);
                intent.putExtra("keyword", query);
                startActivityForResult(intent,RESULT_OK);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        List<SearchItem> mResultsList = new ArrayList<>();
        SearchAdapter mSearchAdapter = new SearchAdapter(this, mResultsList);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mSearchView.close(false);
                TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                String text = textView.getText().toString();
//                mHistoryDatabase.addItem(new SearchItem(text));
                Intent intent = new Intent(getActivity(), SearchWisata.class);
                intent.putExtra("keyword", text);
                startActivityForResult(intent,RESULT_OK);
            }
        });

        mSearchView.setAdapter(mSearchAdapter);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BrowseStoreFragment(), getString(R.string.home));
        adapter.addFrag(new RekomendasiListWisata(), "Rekomendasi Wisata");
        adapter.addFrag(new QuestionFragment(),getString(R.string.tanya_wisat));
        adapter.addFrag(new ArticlesFragmentWisata(), getString(R.string.article_wisata));
       // adapter.addFrag(new ArticlesFragmentKuliner(), getString(R.string.article_kuliner));
      //  adapter.addFrag(new ArticlesFragmentBudaya(), getString(R.string.article_budaya));
        adapter.addFrag(new VideoFragment(), getString(R.string.text_video));

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.search) {
            showSearchView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

     public void toobar(){

         toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         ApplyFontToolbarRimba();

         if (Build.VERSION.SDK_INT >= 21) {

             // Set the status bar to dark-semi-transparentish

             getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                     WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

             // Set paddingTop of toolbar to height of status bar.
             // Fixes statusbar covers toolbar issue
             toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
         }


         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         viewPager = (ViewPager) findViewById(R.id.viewpager);
         setupViewPager(viewPager);

         tabLayout = (TabLayout) findViewById(R.id.tabs);
         tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#8dc63f"));
         tabLayout.setupWithViewPager(viewPager);
     }

    public void ApplyFontToolbarRimba() {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/wwf-webfont.ttf");
                if (tv.getText().equals(toolbar.getTitle())) {
                    tv.setTypeface(custom_font);
                    tv.setTextSize(25);
                    break;
                }
            }
        }
    }    public void updateView() {
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerHeaderView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        mNavigationView.addHeaderView(mDrawerHeaderView);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(mNavigationView);
        mNavigationView.inflateMenu(R.menu.activity_main_drawer);
        mDrawerMenu = mNavigationView.getMenu();

        for (int i=0;i<mDrawerMenu.size();i++) {
            MenuItem mi = mDrawerMenu.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        setMenuItem();
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/wwf-webfont.ttf");

        mNavigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mNavigationView.setItemIconTintList(null);
        tvNamauser = (TextView) mDrawerHeaderView.findViewById(R.id.tv_name_user);
        ivFotouser = (SimpleDraweeView) mDrawerHeaderView.findViewById(R.id.iv_foto_user);
        tvNamauser.setTypeface(custom_font);
        tvNamauser.setTextSize(20);

        logout = (Button) mDrawerHeaderView.findViewById(R.id.btn_logout);
        if (isLogin.equals("")) {
            logout.setText(getString(R.string.text_login));
        } else {
            logout.setText(getString(R.string.text_logout_app));
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isLogin.equals("")) {
                    Intent i = new Intent(MainActivityBeranda.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    exit();
                }
            }
        });


        mWeatherLl = (LinearLayout) mDrawerHeaderView.findViewById(R.id.mWeatherLl);
        tvNamauser.setText(user.fullName);
        if(isLogin.equals("0")||equals("")) {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(30) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(getString(R.string.login), Color.RED);
        }else {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .fontSize(30) /* size in px */
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(user.fullName,Color.RED);
        }
        if(user.isLogin.equals("")){
            ivFotouser.setImageDrawable(drawable);
        }else {
            Uri src_profile = Uri.parse(user.avatar);
            ivFotouser.setImageURI(src_profile);
        }

        ivFotouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isLogin.equals("")) {
                    Intent i = new Intent(MainActivityBeranda.this, LoginActivity.class);
                    startActivity(i);
                } else {
                   Intent i = new Intent(MainActivityBeranda.this, FullImageActivity.class);
                    i.putExtra("imageFull",user.avatar);
                    startActivity(i);
                }
            }
        });


    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.text_logout))
                .setCancelable(false)//tidak bisa tekan tombol back
                //jika pilih yess
                .setPositiveButton(getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(user.typeLogin.equals("facebook")){
                            LoginManager.getInstance().logOut();
                        }
                        clearUser();
                        getActivity().finish();

                        Intent intent = new Intent(getActivity(),
                                MainActivityBeranda.class);
                        getActivity().startActivity(intent);

                    }
                })
                //jika pilih no
                .setNegativeButton(getString(R.string.text_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("0", "0", "0");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 212) {
            User profile = getUser();
            String theAction = data.getStringExtra("avatar");
            if (theAction.equals("avatar")) {
                Uri path = Uri.parse(profile.avatar);
                ivFotouser.setImageURI(path);
            }
        }
    }

    public void onBackPressed() {
        if (mSearchView != null && mSearchView.isSearchOpen()) {
            mSearchView.close(true);
        } else {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.dialog_info, null);

            TextView messageTv = (TextView) view.findViewById(R.id.tv_message);

            messageTv.setText(getActivity()
                    .getString(R.string.text_confirm_signout));

            android.app.AlertDialog.Builder builder = getBuilder(getActivity());

            builder.setView(view)
                    .setNegativeButton(getString(R.string.text_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(getString(R.string.text_yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivityBeranda.this, InterestialLogout.class);
                                    startActivity(intent);

                                }
                            });

            builder.create().show();
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
       /* Typeface font = Typeface.createFromAsset(getAssets(),  "fonts/wwf-webfont.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);*/
        SpannableString spanString = new SpannableString("Wisata Sumbar");
        spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#125688")), 0, spanString.length(), 0); // fix the color to white
       // mi.setTitle(spanString);
    }

    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_Profile:
                Intent profile = new Intent(MainActivityBeranda.this,ProfileActivity.class);
                profile.addFlags(profile.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(profile,212);
                break;
            case R.id.nav_nearme:
                Intent maps = new Intent(MainActivityBeranda.this, ListLocationNearby.class);
                maps.addFlags(maps.FLAG_ACTIVITY_NO_ANIMATION);
                setResult(Activity.RESULT_OK, maps);
                maps.putExtra("latitude",latitude);
                maps.putExtra("longitude",longitude);
                startActivity(maps);
                break;

            case R.id.nav_cerita:
                Intent cerita = new Intent(MainActivityBeranda.this, CeritaActivity.class);
                cerita.addFlags(cerita.FLAG_ACTIVITY_NO_ANIMATION);
                setResult(Activity.RESULT_OK,cerita);
                startActivity(cerita);
                break;

            case R.id.nav_resep:
                Intent resep = new Intent(MainActivityBeranda.this, ResepListActivity.class);
                resep.addFlags(resep.FLAG_ACTIVITY_NO_ANIMATION);
                setResult(Activity.RESULT_OK,resep);
                startActivity(resep);
                break;

            case R.id.nav_Transportasi:
                Intent angkutan = new Intent(MainActivityBeranda.this,AngkutanPariwisata.class);
                angkutan.addFlags(angkutan.FLAG_ACTIVITY_NO_ANIMATION);
                setResult(Activity.RESULT_OK,angkutan);
                startActivity(angkutan);
                break;

            case R.id.nav_cuaca:
                Intent iii = new Intent(MainActivityBeranda.this,WeatherActivity.class);
                startActivity(iii);
                break;
            case R.id.nav_bahasa:
                showLanguageOptions();
                break;
            case R.id.nav_tentang:
                About();
                break;


        }

        drawer.closeDrawers();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());

    }

    private void setMenuItem() {
        MenuItem profile = mDrawerMenu.findItem(R.id.nav_Profile);
        MenuItem store = mDrawerMenu.findItem(R.id.nav_Toko);

        MenuItem transportasi = mDrawerMenu.findItem(R.id.nav_Transportasi);
        MenuItem event = mDrawerMenu.findItem(R.id.nav_cuaca);
        MenuItem forum = mDrawerMenu.findItem(R.id.nav_tentang);

        if (isLogin.equals("")) {
            profile.setVisible(false);

        }
    }


    public void showLanguageOptions() {
        String[] languages = getResources().getStringArray(R.array.array_lang);

        android.app.AlertDialog.Builder builder = getBuilder(getActivity());
        builder.setTitle(getString(R.string.title_dialog_changelanguage));
        builder.setIcon(R.drawable.ic_language);
        builder.setItems(languages,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        if (arg1 == 0) {
                            saveDefaultLanguage(Cons.LANG_ID);
                        } else if(arg1==1) {
                            saveDefaultLanguage(Cons.LANG_EN);
                        }else if(arg1==2) {
                            saveDefaultLanguage(Cons.LANG_MN);
                        }

                        setupLanguage();

                        refreshFeed();
                    }
                });

        builder.create().show();
    }

    public void refreshFeed() {
        finish();

        Intent intent = new Intent(getActivity(), MainActivityBeranda.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void About()
    {

        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_about, null);
        YoutTube = (TextView)view.findViewById(R.id.youtube);
        Instagram = (TextView)view.findViewById(R.id.instagram);
        Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.instagram.com/asywalulfikri/"));
                startActivity(i);
            }
        });
        YoutTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.youtube.com/channel/UCvq8tG1V_tn6TdGd6DGDGbQ"));
                startActivity(i);
            }
        });

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialog.setContentView(view);

        dialog.show();
    }

}


