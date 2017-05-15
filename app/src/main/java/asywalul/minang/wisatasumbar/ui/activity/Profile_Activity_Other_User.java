package asywalul.minang.wisatasumbar.ui.activity;

import android.content.Intent;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Conversation;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.FullImageActivity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

/**
 * Created by Toshiba on 4/12/2016.
 */
public class Profile_Activity_Other_User extends BaseActivity {

    private TextView username,  status;
    private SimpleDraweeView avatar;
    private Conversation mConversation;
    private  int mPosition = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        Bundle bundle       = getIntent().getExtras();

        mConversation = bundle.getParcelable(Util.getIntentName(Cons.CONVERSATION));
        mPosition            = bundle.getInt(Util.getIntentName("position"));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_chevron_left_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setTitle("Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed(); //or whatever you used to do on your onOptionItemSelected's android.R.id.home callback
            }
        });
        username = (TextView)findViewById(R.id.tv_q_author);
        status    = (TextView)findViewById(R.id.status);
        avatar   = (SimpleDraweeView) findViewById(R.id.iv_avatar);

        username.setText(mConversation.user.fullName);
        status.setText(mConversation.user.status);



        if (mConversation.user.avatar == null || mConversation.user.avatar.equals("")) {
            avatar.setVisibility(View.GONE);
        } else {
            avatar.setVisibility(View.VISIBLE);

           /* Picasso.with(this) //
                    .load((mConversation.user.avatar.equals("")) ? null : mConversation.user.avatar) //
                    .placeholder(R.drawable.ic_profile_blank) //
                    .error(R.drawable.ic_profile_blank)
                    .into(avatar);*/

            Uri uri = Uri.parse(mConversation.user.avatar);
            avatar.setImageURI(uri);
        }

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Activity_Other_User.this, FullImageActivity.class);
                intent.putExtra("imageFull", mConversation.user.avatar);
                startActivity(intent);
            }
        });
    }
}