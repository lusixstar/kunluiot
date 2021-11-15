package com.kunluiot.sdk.thirdlib.wifi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by Mike on 2018/3/23.
 * Author:
 * Description:
 */

public class NetworkUtil {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo net = connectivityManager.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo net = connectivityManager.getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_WIFI && net.isConnected();
    }

    public static boolean isMobileConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo net = connectivityManager.getActiveNetworkInfo();
        return net != null && net.getType() == ConnectivityManager.TYPE_MOBILE && net.isConnected();
    }

    /**
     * 获取wifi名称
     * @param context
     * @return
     */
    public static String getCurrentSSID(Context context) {
        // 在nexus 5x 8.1系统上，且应用的target version提升到27，如果关闭了系统安全性和位置信息中隐私设置的位置信息，那么会获取到<unknown ssid>
        // 如果关闭本应用的定位权限，也会同样的情况
        // 但是不同手机的表现不一样
        // 所以当出现unknown ssid 后需要提醒用户去打开设置
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String currentWifi = "";
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                String ssid = wifiInfo.getSSID();
                if (ssid != null) {
                    currentWifi = ssid.replace("\"", "");
                }
            }
        }
        return currentWifi;
    }

    public static String getCurrentBSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String currentBSSID = "";
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                String bssid = wifiInfo.getBSSID();
                if (bssid != null) {
                    currentBSSID = bssid.replace("\"", "");
                }
            }
        }
        return currentBSSID;
    }

    public static boolean isValidSSID(String ssid) {
        return !TextUtils.equals("<unknown ssid>", ssid);
    }

    public static boolean isWifi5G(Context context) {
        int freq = getFrequency(context);
        return freq > 4900 && freq < 5900;
    }

    public static boolean isWifi24G(Context context) {
        int freq = getFrequency(context);
        return freq > 2400 && freq < 2500;
    }

    public static String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2g";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3g";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4g";
            default:
                return "unknown";
        }
    }

    private static int getFrequency(Context context) {
        int freq = 0;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = null;
        if (wifiManager != null) {
            wifiInfo = wifiManager.getConnectionInfo();
        }
        if (wifiInfo != null) {
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                freq = wifiInfo.getFrequency();
            } else {
                String ssid = wifiInfo.getSSID();
                if (ssid != null && ssid.length() > 2) {
                    String ssidTemp = ssid.substring(1, ssid.length() - 1);
                    List<ScanResult> scanResults = wifiManager.getScanResults();
                    for (ScanResult scanResult : scanResults) {
                        if (scanResult.SSID.equals(ssidTemp)) {
                            freq = scanResult.frequency;
                            break;
                        }
                    }
                }
            }
        }

        return freq;
    }
}
