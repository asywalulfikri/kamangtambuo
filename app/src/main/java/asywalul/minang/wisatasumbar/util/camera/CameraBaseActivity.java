package asywalul.minang.wisatasumbar.util.camera;

import android.os.Bundle;
import android.util.DisplayMetrics;


public class CameraBaseActivity extends asywalul.minang.wisatasumbar.util.camera.BaseActivity {
    private DisplayMetrics     displayMetrics = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.getInst().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraManager.getInst().removeActivity(this);
    }

    public float getScreenDensity() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.density;
    }

    public int getScreenHeight() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.widthPixels;
    }

    public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }

    public int dp2px(float f)
    {
        return (int)(0.5F + f * getScreenDensity());
    }

    public int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    //获取应用的data/data/....File目录
    public String getFilesDirPath() {
        return getFilesDir().getAbsolutePath();
    }

    //获取应用的data/data/....Cache目录
    public String getCacheDirPath() {
        return getCacheDir().getAbsolutePath();
    }


    public int getCameraAlbumWidth() {
        return (getScreenWidth() - dp2px(10)) / 4 - dp2px(4);
    }

    // 相机照片列表高度计算
    public  int getCameraPhotoAreaHeight() {
        return getCameraPhotoWidth() + dp2px(4);
    }

    public int getCameraPhotoWidth() {
        return getScreenWidth() / 4 - dp2px(2);
    }

    //活动标签页grid图片高度
    public int getActivityHeight() {
        return (getScreenWidth() - dp2px(24)) / 3;
    }


}
