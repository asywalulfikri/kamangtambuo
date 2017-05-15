package asywalul.minang.wisatasumbar.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.util.blur.BlurBehind;

public class LoginFirstActivity extends BaseActivity {

    private TextView tvLogin;
    private TextView tvLater;

    private RelativeLayout mLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_first);

        BlurBehind.getInstance()
                .withAlpha(80)
                .withFilterColor(Color.parseColor("#000000"))
                .setBackground(this);

        mLayout = (RelativeLayout) findViewById(R.id.layout);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvLater = (TextView) findViewById(R.id.tv_later);

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentY = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentY);

                finish();
            }
        });

        tvLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
