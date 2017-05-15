package asywalul.minang.wisatasumbar.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.ui.fragment.AlbumFragment;
import asywalul.minang.wisatasumbar.util.camera.CameraBaseActivity;
import asywalul.minang.wisatasumbar.util.camera.FileUtils;
import asywalul.minang.wisatasumbar.util.camera.ImageUtils;
import asywalul.minang.wisatasumbar.util.camera.StringUtils;
import asywalul.minang.wisatasumbar.util.camera.model.Album;

/**
 * 相册界面
 * Created by sky on 2015/7/8.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class AlbumActivity extends CameraBaseActivity {

    private Map<String, Album> albums;
    private List<String> paths = new ArrayList<String>();

    private  PagerSlidingTabStrip tab;
    private  ViewPager pager;
    private  Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        tab = (PagerSlidingTabStrip)findViewById(R.id.indicator);
        pager = (ViewPager)findViewById(R.id.pager);
        toolbar = (Toolbar)findViewById(R.id.title_layout);

        albums = ImageUtils.findGalleries(this, paths, 0);
        toolbar.setTitle("Gallery");
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tab.setViewPager(pager);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent result) {
        if (resultCode == RESULT_OK) {
            // super.onActivityResult(requestCode,resultCode,result);
            Intent intent = new Intent();
            intent.putExtra("path",result.getStringExtra("path"));
            setResult(RESULT_OK,intent);
            finish();
        }else {
            finish();
        }
    }


    public  class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return AlbumFragment.newInstance(albums.get(paths.get(position)).getPhotos());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Album album = albums.get(paths.get(position % paths.size()));
            if (StringUtils.equalsIgnoreCase(FileUtils.getInst().getSystemPhotoPath(),
                    album.getAlbumUri())) {
                return "CAMERA";
            } else if (album.getTitle().length() > 13) {
                return album.getTitle().substring(0, 11) + "...";
            }
            return album.getTitle();
        }

        @Override
        public int getCount() {
            return paths.size();
        }
    }

}
