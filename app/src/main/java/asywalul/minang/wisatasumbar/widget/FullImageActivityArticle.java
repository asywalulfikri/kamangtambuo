package asywalul.minang.wisatasumbar.widget;

//import uk.co.senab.photoview.PhotoViewAttacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.ui.activity.ArticleDetailActivity;
import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;
import asywalul.minang.wisatasumbar.util.TouchImageView;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

@SuppressLint("ClickableViewAccessibility")
public class FullImageActivityArticle extends BaseActivity {

    String rawImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_fullscreen);

        TouchImageView mImageView = (TouchImageView) findViewById(R.id.ivFull);

        Intent im = getIntent();
        rawImage = im.getStringExtra(ArticleDetailActivity.sendImage);
        Picasso.with(getActivity())
                .load((rawImage.equals("")) ? null : rawImage)
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(mImageView);
    }
}
