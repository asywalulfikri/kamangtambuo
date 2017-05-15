package asywalul.minang.wisatasumbar.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import asywalul.minang.wisatasumbar.MainActivityBeranda;
import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.ui.introtutorial.TutorialIntro;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;


public class SplashScreen extends BaseActivity{


    ImageView splashImage , camFlash;
    public static int screenWidth,screenHeight;
    RelativeLayout relativeLayout;

    Thread t ;
    Handler h;
    private User user;
    private Location location;
    SharedPreferences sharedPreferences;
    int intro;
    private TextView tvAppName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        tvAppName = (TextView)findViewById(R.id.textView2);
        loadDimension();
        sharedPreferences = getSharedPreferences("config_intro",MODE_PRIVATE);
        intro = sharedPreferences.getInt("intro",0);

        relativeLayout = (RelativeLayout) findViewById(R.id.rell);


        splashImage = (ImageView) findViewById(R.id.splashPicture);
        camFlash = (ImageView) findViewById(R.id.camflash);
        setInvisible(splashImage, camFlash);
        initDatabase();
        user = getUser();
        startTootipAnimation();

        h = new Handler();
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {

                        final float end;
                        ViewGroup.LayoutParams layoutParams = splashImage.getLayoutParams();
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            layoutParams.height = (int) (screenWidth * 0.5);
                            layoutParams.width = (int) (screenWidth * 0.5);
                            end = screenHeight/2.5F;
                        }else{
                            layoutParams.height = (int) (screenHeight * 0.5);
                            layoutParams.width = (int) (screenHeight * 0.5);
                            end = screenHeight/4.5F;
                        }


                        splashImage.setLayoutParams(layoutParams);



                        final ValueAnimator bounceAnim = ObjectAnimator.ofFloat(splashImage, "y",-screenHeight, end);
                        bounceAnim.setDuration(2000);
                        bounceAnim.setInterpolator(new BounceInterpolator());

                        final ValueAnimator rotate = ObjectAnimator.ofFloat(splashImage,"rotation",90,0);
                        rotate.setDuration(2000);
                        rotate.setInterpolator(new BounceInterpolator());

                        final ValueAnimator scaleX = ObjectAnimator.ofFloat(camFlash,"scaleX",0f,1.1f);
                        scaleX.setDuration(180);
                        scaleX.setInterpolator(new BounceInterpolator());
                        final ValueAnimator scaleY = ObjectAnimator.ofFloat(camFlash,"scaleY",0f,1.1f);
                        scaleY.setDuration(180);
                        scaleY.setInterpolator(new BounceInterpolator());

                        final ValueAnimator scaleDownX = ObjectAnimator.ofFloat(camFlash,"scaleX",1.1f,0f);
                        scaleX.setDuration(180);
                        scaleX.setInterpolator(new BounceInterpolator());
                        final ValueAnimator scaleDownY = ObjectAnimator.ofFloat(camFlash,"scaleY",1.1f,0f);
                        scaleY.setDuration(180);
                        scaleY.setInterpolator(new BounceInterpolator());

                        final AnimatorSet set[] = new AnimatorSet[4];
                        initializeAnimators(set);

                        set[0].playTogether(rotate, bounceAnim);
                        set[0].start();

                        splashImage.setVisibility(View.GONE);
                        set[0].addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                camFlash.setVisibility(View.GONE);

                                set[1].playTogether(scaleX, scaleY);
                                set[1].start();
                                set[1].addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationStart(animation);
                                        set[1].removeAllListeners();set[1].end(); set[1].cancel();

                                        set[2].playTogether(scaleDownX, scaleDownY);
                                        set[2].start();

                                        set[2].addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);
                                                set[2].removeAllListeners();set[2].end(); set[2].cancel();
                                                init();
                                                overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
                                                finish();
                                            }
                                        });

                                    }
                                });

                            }
                        });
                    }
                })     ;
            }
        });t.start();

   }

    private void startTootipAnimation() {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvAppName, "scaleY", 0.8f);
        scaleY.setDuration(200);

        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(tvAppName, "scaleY", 1f);
        scaleYBack.setDuration(500);



        scaleYBack.setInterpolator(new BounceInterpolator());
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(500);
        animatorSet.playSequentially(scaleY, scaleYBack);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSet.setStartDelay(500);
                animatorSet.start();
            }
        });
        tvAppName.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        animatorSet.start();
    }

    private void init() {
        if (user.isLogin.equals("1")) {
            if(intro ==1){
                Intent i = new Intent(SplashScreen.this, MainActivityBeranda.class);
                startActivity(i);
            }else {
                Intent intent = new Intent (SplashScreen.this, TutorialIntro.class);
                startActivity(intent);
            }


        } else{
            if(intro == 1) {
                Intent i = new Intent(SplashScreen.this, MainActivityBeranda.class);
                startActivity(i);
            }else {
                Intent intent = new Intent (SplashScreen.this, TutorialIntro.class);
                startActivity(intent);
            }
        }
    }


    public void initDatabase() {
        Util.createAppDir();

        String database = Cons.DBPATH + Cons.DBNAME;
        int currVersion	= mSharedPref.getInt(Cons.DBVERSION_KEY, 0);

        try {
            Debug.i(Cons.TAG, "Current database version is " + String.valueOf(currVersion));

            if (Cons.DB_VERSION > currVersion) {
                File databaseFile  = new File(database);

                if (databaseFile.exists()) {
                    Debug.i(Cons.TAG, "Deleting current database " + Cons.DBNAME);

                    databaseFile.delete();
                }

                InputStream is	= getResources().getAssets().open(Cons.DBNAME);
                OutputStream os = new FileOutputStream(database);

                byte[] buffer	= new byte[1024];
                int length;

                Debug.i(Cons.TAG, "Start copying new database " + database + " version " + String.valueOf(Cons.DB_VERSION));

                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                os.flush();
                os.close();
                is.close();

                SharedPreferences.Editor editor = mSharedPref.edit();

                editor.putInt(Cons.DBVERSION_KEY, Cons.DB_VERSION);
                editor.commit();
            } else {
                if (Cons.ENABLE_DEBUG) {
                    InputStream is	= new FileInputStream(database);
                    OutputStream os = new FileOutputStream(Util.getAppDir() + "/wisatasumbar.db");

                    byte[] buffer	= new byte[1024];
                    int length;

                    Debug.i(Cons.TAG, "[DEVONLY] Copying db " + database + " to " + Util.getAppDir() + "/wisatasumbar.db");

                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }

                    os.flush();
                    os.close();
                    is.close();
                }
            }
        } catch (SecurityException ex) {
            Debug.e(Cons.TAG, "Failed to delete current database " + Cons.DBNAME, ex);
        } catch (IOException ex) {
            Debug.e(Cons.TAG, "Failed to copy new database " + Cons.DBNAME + " version " + String.valueOf(Cons.DB_VERSION), ex);
        }

        enableDatabase();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestStoragesPermission();

                    } else {
                        initDatabase();
                        init();
                    }
                }
                return;
            }

        }
    }

    private void initializeAnimators(AnimatorSet[] set) {
        for(int i = 0 ; i < set.length;i++) {
            set[i] = new AnimatorSet();
        }
    }

    private void setInvisible(ImageView splashImage, ImageView camFlash) {
        splashImage.setVisibility(View.GONE);
        camFlash.setVisibility(View.GONE);
    }




    private  void loadDimension(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
}
