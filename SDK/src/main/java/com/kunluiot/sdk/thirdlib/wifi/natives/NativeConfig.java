package com.kunluiot.sdk.thirdlib.wifi.natives;

import android.util.Log;

import com.kunluiot.sdk.thirdlib.wifi.ConfigCallback;
import com.kunluiot.sdk.thirdlib.wifi.IConfig;


/**
 * Created by Mike on 2018/6/11.
 * Author:
 * Description:
 */
public class NativeConfig  implements IConfig {

    private static final String TAG = NativeConfig.class.getSimpleName();

    private static NativeConfig instance;

    private ConfigCallback mCallback;
    private int mSocketToken;

    public static NativeConfig getInstance() {
        if (instance == null) {
            synchronized (NativeConfig.class) {
                if (instance == null) {
                    instance = new NativeConfig();
                }
            }
        }

        return instance;
    }

    private NativeConfig() {

    }

    @Override
    public int startConfig(String ssid, String password, String pinCode, ConfigCallback callback) {
        Log.d(TAG, "Start Config: " + ssid + ", " + password + ", " + pinCode);
        stopConfig();
        mSocketToken = nativeStartConfig(ssid, password, pinCode);
        this.mCallback = callback;
        return mSocketToken;
    }

    @Override
    public void stopConfig() {
        Log.d(TAG, "Stop Config");
        nativeStopConfig(mSocketToken);
        mCallback = null;
        mSocketToken = 0;
    }

    @Override
    public void sendUdp(int token, String ip, int port, String data) {
        nativeSendUdp(token, ip, port, data);
    }

    @Override
    public String softAP(String ssid, String password, String pinCode) {
        return nativeSoftAP(ssid, password, pinCode);
    }

    native int nativeStartConfig(String ssid, String password, String pinCode);

    native void nativeStopConfig(int token);

    native void nativeSendUdp(int token, String ip, int port, String data);

    native String nativeSoftAP(String ssid, String password, String pinCode);

    void onMessage(String ip, String message) {
        // Log.d(TAG, "Receive: " + ip + "=>" + message);
        if (mCallback != null) {
            mCallback.onReceive(ip, message);
        }
    }
}
