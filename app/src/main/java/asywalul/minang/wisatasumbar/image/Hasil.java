package asywalul.minang.wisatasumbar.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.util.camera.ImageUtils;


/**
 * Created by asywalulfikri on 11/23/16.
 */

public class Hasil extends AppCompatActivity {

    ImageView mGPUImageView;
    private Bitmap currentBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image_hasil);
        mGPUImageView = (ImageView) findViewById(R.id.ivnya);

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
               // mGPUImageView.setImageURI(currentBitmap);
            }
        });
    }
}