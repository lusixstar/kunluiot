package com.kunluiot.sdk.net.log;

import android.util.Log;

import androidx.annotation.NonNull;

import com.kunluiot.sdk.BuildConfig;


public class KunLuLog {

    //是否需要打印bug
    public static boolean DEBUG = BuildConfig.DEBUG;

    private static final String TAG = "KunLuSDK";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (DEBUG)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (DEBUG)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (DEBUG)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (DEBUG)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            Log.v(tag, msg);
    }

    public static void largeI(String tagName, @NonNull String msg) {
        if (DEBUG) {
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.i(tagName, msg == null ? "NULL_MSG" : msg);
            } else {
                while (msg.length() > segmentSize) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    Log.i(tagName, logContent);
                }
                Log.i(tagName, msg);// 打印剩余日志
            }
        }
    }
}
