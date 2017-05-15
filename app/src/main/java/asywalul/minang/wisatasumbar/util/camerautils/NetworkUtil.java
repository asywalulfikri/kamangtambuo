package asywalul.minang.wisatasumbar.util.camerautils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;


/**
 * Created by yue on 15/10/29.
 * 网络状态的判断工具
 */
public class NetworkUtil {
    private NetworkUtil() {
    }

    public static String NETWORK_TYPE_WIFI = "WIFI";
    public static String NETWORK_TYPE_MOBILE = "MOBILE";
    public static String NETWORK_TYPE_ERROR = "ERROR";

    /**
     * 返回网络是否可用。需要权限： < uses-permission
     * android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return 网络可用则返回true，否则返回false
     */
    public static boolean isAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 判断网络连接类型
     *
     * @param context
     * @return
     */
    public static String getNetType(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                            return NETWORK_TYPE_WIFI;
                        } else {
                            return NETWORK_TYPE_MOBILE;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return NETWORK_TYPE_ERROR;
        }
        return NETWORK_TYPE_ERROR;
    }

    /**
     * 返回Wifi是否启用
     *
     * @param context
     * @return Wifi网络可用则返回true，否则返回false
     */
    public static boolean isWIFIActivate(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .isWifiEnabled();
    }


    public static void startNetSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }



}
