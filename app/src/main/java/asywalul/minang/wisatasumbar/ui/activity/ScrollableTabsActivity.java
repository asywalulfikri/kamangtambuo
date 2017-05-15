package asywalul.minang.wisatasumbar.ui.activity;

import asywalul.minang.wisatasumbar.R;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import asywalul.minang.wisatasumbar.ui.fragment.ArticlesFragment;
import asywalul.minang.wisatasumbar.ui.fragment.DaerahFragment;
import asywalul.minang.wisatasumbar.ui.fragment.KulinerFragment;
import asywalul.minang.wisatasumbar.ui.fragment.PesanTravel;
import asywalul.minang.wisatasumbar.ui.fragment.ProfileFragment;
import asywalul.minang.wisatasumbar.ui.fragment.QuestionFragment;
import asywalul.minang.wisatasumbar.ui.fragment.StoreFragment;
import asywalul.minang.wisatasumbar.ui.fragment.TransportasionFragment;
import asywalul.minang.wisatasumbar.ui.fragment.WeatherFragment;


public class ScrollableTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new QuestionFragment(), "TANYA WISATA");
        adapter.addFrag(new ArticlesFragment(), "ARTIKEL");
        adapter.addFrag(new KulinerFragment(), "KULINER");
        adapter.addFrag(new StoreFragment(), "TOKO");
        adapter.addFrag(new PesanTravel(), "PESAN TRAVEL");
       // adapter.addFrag(new EventFragment(), "ACARA");
        adapter.addFrag(new TransportasionFragment(), "TRANSPORTASI");
        adapter.addFrag(new DaerahFragment(), "DAERAH");
        adapter.addFrag(new ProfileFragment(), "PROFIL");
        adapter.addFrag(new WeatherFragment(), "CUACA");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
