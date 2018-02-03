package asywalul.minang.wisatasumbar.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.model.User;
import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import asywalul.minang.wisatasumbar.util.Util;
import asywalul.minang.wisatasumbar.util.ValueConfig;
import asywalul.minang.wisatasumbar.util.blur.BlurBehind;
import asywalul.minang.wisatasumbar.util.blur.OnBlurCompleteListener;

/**
 * Created by Toshiba on 3/12/2016.
 */
public class BaseActivity extends AppCompatActivity {

    protected SQLiteDatabase mSqLite;
    protected SharedPreferences mSharedPref;
    protected SharedPreferences mSharedPrefSession;
    private boolean mIsDbOpen = false;
    private boolean mEnableDb = false;
    public Timer mTimerPage;
    public int totalTimeSec = 0;
    public static final int REQUEST_LOCATION = 0;
    public Timer mTimerPage2;
    public int totalTimeSec2 = 0;
    public Timer timer;
    public String latitude = "0.0";
    public String longitude = "0.0";
    public static final int REQUEST_STORAGE = 3;

    public Handler handler;
    public Runnable runnable;

    public static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    public BaseActivity getActivity() {
        return this;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPref = getSharedPreferences(Cons.PRIVATE_PREF,
                Context.MODE_PRIVATE);

        mSharedPrefSession = getSharedPreferences(Cons.SESSION_PREF,
                Context.MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        if (mEnableDb && mIsDbOpen) {
            closeDatabase();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSharedPref = getSharedPreferences(Cons.PRIVATE_PREF,
                Context.MODE_PRIVATE);

        if (mEnableDb && !mIsDbOpen) {
            openDatabase();
        }
    }

    protected void enableDatabase() {
        mEnableDb = true;

        openDatabase();
    }

    private void openDatabase() {
        if (mIsDbOpen) {
            Debug.i(Cons.TAG, "Database already open");
            return;
        }

        String db = Cons.DBPATH + Cons.DBNAME;

        try {
            mSqLite = SQLiteDatabase.openDatabase(db, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            mIsDbOpen = mSqLite.isOpen();

            Debug.i(Cons.TAG, "Database open");
        } catch (SQLiteException e) {
            Debug.e(Cons.TAG, "Can not open database " + db, e);
        }
    }


    // This for Close Koneksi to Database
    private void closeDatabase() {
        if (!mIsDbOpen)
            return;

        mSqLite.close();

        mIsDbOpen = false;

        Debug.i(Cons.TAG, "Database closed");
    }


    @SuppressLint("InflateParams")
    public void showToast(String text, boolean islong) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_toast, null);

        TextView textView = (TextView) layout.findViewById(R.id.tv_title_resep);

        textView.setText(text);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration((islong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    // This setup language
    public String getDefaultLanguage() {
        return mSharedPref.getString(Cons.USER_LANGUAGE, Cons.LANG_ID);
    }

    public void showInfo(String message) {
        showDialog("", message, false);
    }

    public void saveDefaultLanguage(String lang) {
        SharedPreferences.Editor editor = mSharedPref.edit();

        editor.putString(Cons.USER_LANGUAGE, lang);
        editor.commit();
    }


    public void setupLanguage() {
        Locale locale = new Locale(getDefaultLanguage());

        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }


    public static final int getColorModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }




    /**
     * This check build version
     */
    public boolean buildVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }else {
            return false;
        }
    }

    public void showError(String message) {
        showDialog("Error", message, false);
    }


    @SuppressLint("InflateParams")
    public void showDialog(String title, String message, final boolean back) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_info, null);

        TextView messageTv = (TextView) view.findViewById(R.id.tv_message);

//		if (getDefaultLanguage().equals(Cons.LANG_ID)) {
//			messageTv.setTypeface(mFont);
//		}

        messageTv.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (!title.equals(""))
            builder.setTitle(title);

        builder.setCancelable(false);
        builder.setView(view).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (back) {
                            back();
                        }
                    }
                });

        builder.create().show();
    }

    public void back() {
        setResult(RESULT_OK);
        finish();
    }

    public void startCountTime() {
        totalTimeSec = 0;

        mTimerPage = new Timer();
        mTimerPage.schedule(new TimerTask() {
            public void run() {
                totalTimeSec++;
            }
        }, 1, 1); //execute every 1 seconds
    }

    public void startCountTime2() {
        totalTimeSec2 = 0;

        mTimerPage2= new Timer();
        mTimerPage2.schedule(new TimerTask() {
            public void run() {
                totalTimeSec2++;
            }
        }, 1, 1); //execute every 1 seconds
    }
    public void showToast(String text) {
        showToast(text, true);
    }

    public SQLiteDatabase getDatabase() {
        return mSqLite;
    }


    public AlertDialog.Builder getBuilder(Context context) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        return builder;
    }

    public void errorHandle(VolleyError error) {
        if (error != null) {
            NetworkResponse response = error.networkResponse;

            if (response != null && response.data != null) {
                String json = null;
                json = new String(response.data);
                VolleyLog.e("Response Error", "Error" + json);
            }
        }
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        user.isLogin = "1";
        editor.putString(Cons.KEY_ID, user.userId);
        editor.putString(Cons.KEY_EMAIL, user.email);
        editor.putString(Cons.KEY_NAME, user.fullName);
        editor.putString(Cons.KEY_AVATAR, user.avatar);
        editor.putString(Cons.KEY_BIRTH, user.birthDate);
        editor.putString(Cons.KEY_GENDER, user.gender);
        editor.putString(Cons.KEY_CREATED_AT, user.createAt);
        editor.putString(Cons.KEY_LATITUDE, user.latitude);
        editor.putString(Cons.KEY_LONGITUDE, user.longitude);
        editor.putString(Cons.KEY_STATUS, user.status);
        editor.putString(Cons.KEY_HOBBY, user.hobby);
        editor.putString(Cons.KEY_IS_LOGIN,user.isLogin.concat("1"));
        editor.putString(Cons.KEY_TYPE_LOGIN,user.typeLogin);


        editor.commit();

    }

    public void saveUser(User user,String path) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        user.isLogin = "1";
        editor.putString(Cons.KEY_ID, user.userId);
        editor.putString(Cons.KEY_EMAIL, user.email);
        editor.putString(Cons.KEY_NAME, user.fullName);
        editor.putString(Cons.KEY_AVATAR, path);
        editor.putString(Cons.KEY_BIRTH, user.birthDate);
        editor.putString(Cons.KEY_GENDER, user.gender);
        editor.putString(Cons.KEY_CREATED_AT, user.createAt);
        editor.putString(Cons.KEY_LATITUDE, user.latitude);
        editor.putString(Cons.KEY_LONGITUDE, user.longitude);
        editor.putString(Cons.KEY_STATUS, user.status);
        editor.putString(Cons.KEY_HOBBY, user.hobby);
        editor.putString(Cons.KEY_IS_LOGIN,user.isLogin.concat("1"));
        editor.putString(Cons.KEY_TYPE_LOGIN,user.typeLogin);


        editor.commit();

    }

    public User getUser() {


        User user = new User();

        user.userId		 = mSharedPref.getString(Cons.KEY_ID, "");
        user.email		 = mSharedPref.getString(Cons.KEY_EMAIL, "");
        user.fullName	 = mSharedPref.getString(Cons.KEY_NAME, "");
        user.avatar	     = mSharedPref.getString(Cons.KEY_AVATAR, "");
        user.birthDate	 = mSharedPref.getString(Cons.KEY_BIRTH, "");
        user.gender      = mSharedPref.getString(Cons.KEY_GENDER, "");
        user.createAt    = mSharedPref.getString(Cons.KEY_CREATED_AT, "");
        user.updatedAt   = mSharedPref.getString(Cons.KEY_UPDATED_AT, "");
        user.isLogin     = mSharedPref.getString(Cons.KEY_IS_LOGIN,"");
        user.typeLogin   = mSharedPref.getString(Cons.KEY_TYPE_LOGIN,"");
        user.status      = mSharedPref.getString(Cons.KEY_STATUS,"");
        user.hobby       = mSharedPref.getString(Cons.KEY_HOBBY,"");

        return user;
    }

    public void clearUser() {
        SharedPreferences.Editor editor = mSharedPref.edit();

        editor.putString(Cons.KEY_ID, "");
        editor.putString(Cons.KEY_EMAIL, "");
        editor.putString(Cons.KEY_NAME, "");
        editor.putString(Cons.KEY_AVATAR, "");
        editor.putString(Cons.KEY_BIRTH, "");
        editor.putString(Cons.KEY_GENDER, "");
        editor.putString(Cons.KEY_CREATED_AT, "");
        editor.putString(Cons.KEY_UPDATED_AT, "");
        editor.putString(Cons.KEY_IS_LOGIN, "");
        editor.putString(Cons.KEY_TYPE_LOGIN,"");
        editor.putString(Cons.KEY_STATUS,"");
        editor.putString(Cons.KEY_HOBBY,"");

        editor.commit();

    }

    @SuppressLint("InflateParams")
    public void showDialogLogin(String message) {
        BlurBehind.getInstance().execute(getActivity(), new OnBlurCompleteListener() {
            @Override
            public void onBlurComplete() {
                Intent intent = new Intent(getActivity(), LoginFirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
            }
        });
    }

    public void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
        }
    }


    public void requestStoragesPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_STORAGE);
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_STORAGE);
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

    public boolean getFirstTimeOpen() {
        boolean firstTime = mSharedPref.getBoolean("first_time_open", false);

        return firstTime;
    }
    public void saveFirstTimeOpen() {
        Debug.i("Saving first time open..");

        SharedPreferences.Editor editor = mSharedPref.edit();

        editor.putBoolean("first_time_open", true);

        editor.commit();
    }





}
