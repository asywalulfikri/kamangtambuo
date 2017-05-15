package asywalul.minang.wisatasumbar.util.camera;

public class DistanceUtil extends CameraBaseActivity{

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
