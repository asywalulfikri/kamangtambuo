package asywalul.minang.wisatasumbar.util.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Stack;

import asywalul.minang.wisatasumbar.image.CameraActivity;
import asywalul.minang.wisatasumbar.image.CropPhotoActivity;
import asywalul.minang.wisatasumbar.image.Hasil;
import asywalul.minang.wisatasumbar.util.camera.model.PhotoItem;

/**
 * 相机管理类
 * Created by sky on 15/7/6.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class CameraManager {

    private static CameraManager mInstance;
    private Stack<Activity> cameras = new Stack<Activity>();

    public static CameraManager getInst() {
        if (mInstance == null) {
            synchronized (CameraManager.class) {
                if (mInstance == null)
                    mInstance = new CameraManager();
            }
        }
        return mInstance;
    }

    //Membuka antarmuka kamera
    public void openCamera(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }

    //Tentukan apakah gambar harus disesuaikan
    public void processPhotoItem(Activity activity, PhotoItem photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo
                .getImageUri()) : Uri.parse("file://" + photo.getImageUri());
        if (ImageUtils.isSquare(photo.getImageUri())) {
            Intent newIntent = new Intent(activity, Hasil.class);
            newIntent.setData(uri);
            activity.startActivity(newIntent);
        } else {
            Intent i = new Intent(activity, CropPhotoActivity.class);
            i.setData(uri);
            //TODO稍后添加
            activity.startActivityForResult(i, AppConstants.REQUEST_CROP);
        }
    }

    public void close() {
        for (Activity act : cameras) {
            try {
                act.finish();
            } catch (Exception e) {

            }
        }
        cameras.clear();
    }

    public void addActivity(Activity act) {
        cameras.add(act);
    }

    public void removeActivity(Activity act) {
        cameras.remove(act);
    }



}
