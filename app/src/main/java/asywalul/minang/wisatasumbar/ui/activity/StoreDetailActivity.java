package asywalul.minang.wisatasumbar.ui.activity;

import android.app.Activity;
import android.content.Intent;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Store;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ferissadjohan on 5/11/16.
 */
public class StoreDetailActivity extends BaseActivity {

    private ImageView ivProduct,ivAvatar;
    private TextView tvContent;
    private TextView tvPrice;
    private TextView tvSeller;
    private Store mStore;
    int mPosition =0;
    private String mIdProduct;
    private CollapsingToolbarLayout mCTollbar;
    private static final int REQ_CODE = 1000;
    private boolean dataChange = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail2);

        Bundle bundle = getIntent().getExtras();
        mStore = bundle.getParcelable(Util.getIntentName("store"));
        mPosition = bundle.getInt(Util.getIntentName("position"));
        mIdProduct = mStore.idbarang;

        initializeToolbar();
        updateView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE && resultCode == Activity.RESULT_OK) {

            if (data == null) return;

            mStore = data.getParcelableExtra(Util.getIntentName("store"));
            mCTollbar.setTitle(mStore.namabarang);

            Log.e("RRR", mStore.namabarang);
            updateView();

            dataChange = true;

            Log.e("RESULT", String.valueOf(mStore.namabarang));

            Debug.i("On Result");
        }
    }

    public void initializeToolbar() {
        mCTollbar   = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(mStore.namabarang);
        mCTollbar.setTitle(mStore.namabarang);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mStore.namabarang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_face_black_24dp);

    }
    public void updateView(){
        ivProduct = (ImageView)findViewById(R.id.iv_location) ;

        tvPrice = (TextView)findViewById(R.id.tv_content);
        tvContent = (TextView)findViewById(R.id.tv_product_detail);
        tvSeller = (TextView)findViewById(R.id.tv_seller_name);
        ivAvatar =(ImageView)findViewById(R.id.iv_seller);



        tvPrice.setText(String.valueOf(mStore.hargabarang));
        tvContent.setText(mStore.keterangan);
        tvSeller.setText(mStore.user.fullName);

        Picasso.with(this) //
                .load((mStore.pathh.equals("")) ? null : mStore.pathh) //
                .placeholder(R.drawable.no_image) //
                .error(R.drawable.no_image)
                .into(ivProduct);


        Picasso.with(this) //
                .load((mStore.user.avatar.equals("")) ? null : mStore.user.avatar) //
                .placeholder(R.drawable.no_image) //
                .error(R.drawable.no_image)
                .into(ivAvatar);
    }
}
