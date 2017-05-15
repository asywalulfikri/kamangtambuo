package asywalul.minang.wisatasumbar.util;

import android.app.Activity;
import android.app.Dialog;
import asywalul.minang.wisatasumbar.R;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by user on 1/27/2016.
 */
public class LoadingAlertDialog {
    Activity activity;
    Dialog builder;

    public LoadingAlertDialog(Activity activity) {
        this.activity = activity;
    }
    public void show(){
        builder = new Dialog(activity,
                R.style.CustomDialog);

        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_loading,
                null);
        builder.setContentView(dialogView);
        Window window = builder.getWindow();
        builder.setCancelable(false);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.dimAmount = 0.8f;
        builder.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        wlp.gravity = Gravity.TOP;
        window.setAttributes(wlp);
        builder.show();

    }
    public void dismiss(){
        builder.dismiss();
    }
}
