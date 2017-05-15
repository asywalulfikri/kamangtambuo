package asywalul.minang.wisatasumbar.ui.activity;

import android.content.Intent;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.Comment;
import asywalul.minang.wisatasumbar.model.Question;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.widget.FullImageActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Toshiba on 4/12/2016.
 */
public class Profile_activity_Other_User2 extends BaseActivity {

    private TextView username, status;
    private ImageView avatar;
    private Comment mComment;
    private  int mPosition = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user);

        Bundle bundle       = getIntent().getExtras();

        mComment            = bundle.getParcelable(Util.getIntentName("comment"));
        mPosition            = bundle.getInt(Util.getIntentName("position"));

        username = (TextView)findViewById(R.id.tv_q_author);
        status    = (TextView)findViewById(R.id.status);
        avatar   = (ImageView)findViewById(R.id.iv_avatar);

        username.setText(mComment.user.fullName);
        status.setText(mComment.user.email);



        if (mComment.user.avatar == null || mComment.user.avatar.equals("")) {
            avatar.setVisibility(View.GONE);
        } else {
            avatar.setVisibility(View.VISIBLE);

            Picasso.with(this) //
                    .load((mComment.user.avatar.equals("")) ? null : mComment.user.avatar) //
                    .placeholder(R.drawable.ic_profile_blank) //
                    .error(R.drawable.ic_profile_blank)
                    .into(avatar);
        }
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_activity_Other_User2.this, FullImageActivity.class);
                intent.putExtra("imageFull",mComment.user.avatar);
                startActivity(intent);
            }
        });




    }
}