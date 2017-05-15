package asywalul.minang.wisatasumbar.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.util.ScreenUtil;

/**
 * Created by asywalulfikri on 11/9/16.
 */

public class DetailLocationNearby extends BaseActivity {

    private ImageView ivMaps;
    private ImageView ivPhotos;
    private TextView tvDate;
    private TextView tvLocation;
    private TextView tvJambuka;

    private String latitude;
    private String longitude;
    private String jambuka, lokasi, foto, name;

    private int mapWidth;
    private int mapHeight;
    private int heigtScreen;
    private int widthScreen;
    private int diffWidth;
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location_nearme);

        Intent intent = getIntent();

        heigtScreen = ScreenUtil.getScreenHeight(getActivity());
        widthScreen = ScreenUtil.getScreenWidth(getActivity());


        diffWidth = widthScreen - 650;
        mapWidth    = widthScreen - diffWidth;
        mapHeight   = (heigtScreen/2) - diffWidth;

        latitude  = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        jambuka   = intent.getStringExtra("jambuka");
        lokasi    = intent.getStringExtra("lokasi");
        foto      = intent.getStringExtra("foto");
        name      = intent.getStringExtra("name");


        ivMaps     = (ImageView) findViewById(R.id.iv_maps);
        ivPhotos   = (ImageView) findViewById(R.id.iv_location);
        tvDate     = (TextView) findViewById(R.id.tv_date);
        tvLocation = (TextView) findViewById(R.id.tv_content);
        tvJambuka  = (TextView) findViewById(R.id.tv_jambuka);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout mCTollbar   = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar.setTitle(name);
        mCTollbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        updateview();
    }




    public void  updateview(){

        tvLocation.setText(lokasi);
        String openHours = jambuka;
        if (openHours.equals("ture")){
            tvJambuka.setText(getString(R.string.buka));
        }else {
            tvJambuka.setText(getString(R.string.tutup));
        }

        String LatLng = latitude + "," + longitude;

        String mapUrl = "http://maps.google.com/maps/api/staticmap?center=" + LatLng +
                "&zoom=50&size=" + mapWidth + "x" + mapHeight +"&sensor=true&markers=" + LatLng;

        Picasso.with(this) //
                .load((foto.equals("")) ? null : foto) //
                .placeholder(R.drawable.no_image) //
                .error(R.drawable.no_image)
                .into(ivPhotos);

        Picasso.with(getActivity())
                .load(mapUrl)
                .error(R.drawable.no_image)
                .into(ivMaps);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}