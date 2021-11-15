package com.kunluiot.sdk.thirdlib.wifi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.kunluiot.sdk.thirdlib.wifi.javas.BroadcastUdpConn;
import com.kunluiot.sdk.thirdlib.wifi.javas.CommonUdpConn;
import com.kunluiot.sdk.thirdlib.wifi.javas.MulticastUdpConn;
import com.kunluiot.sdk.thirdlib.wifi.javas.SeparatedDeviceConfig;
import com.kunluiot.sdk.thirdlib.wifi.natives.NativeConfig;
import com.kunluiot.sdk.thirdlib.wifi.utils.ConfigZipUtil;
import com.kunluiot.sdk.thirdlib.wifi.utils.NetObservable;
import com.kunluiot.sdk.thirdlib.wifi.utils.NetworkMonitor;
import com.kunluiot.sdk.thirdlib.wifi.utils.NetworkUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RNHekrConfigModule  {

    private static final String TAG = RNHekrConfigModule.class.getSimpleName();

    private final Context mContext;
    private Map<String, Object> mConstants;
    private NetworkMonitor mNetworkMonitor;
    private NativeConfig mNativeConfig;
    private SeparatedDeviceConfig mDeviceConfig;

    public RNHekrConfigModule(Context reactContext) {
        this.mContext = reactContext;
        this.mConstants = new HashMap<>();
        this.mNetworkMonitor = NetworkMonitor.getInstance();
        this.mNativeConfig = NativeConfig.getInstance();

        buildConstants();
        mNetworkMonitor.init(reactContext);

        // 配网初始化
        BroadcastUdpConn.getInstance(reactContext).start();
        MulticastUdpConn.getInstance(reactContext).start();
        CommonUdpConn.getInstance(reactContext).start();
        mDeviceConfig = new SeparatedDeviceConfig(reactContext);

        // 接收消息
        CommonUdpConn.getInstance(reactContext).addOnReceiveListener(new ConfigCallback() {
            @Override
            public void onReceive(String ip, String message) {
                Log.d(TAG, "Receive: " + ip + "=>" + message);
                Map<String, String> map = new HashMap<>();
                map.put("address", ip);
                map.put("message", message);
                sendEvent("ConfigDeviceEventName", map);
            }
        });

//        initialize();
    }

    public void initialize() {
        mNetworkMonitor.add(new NetObservable() {
            @Override
            public void onWifiChanged(String type, String effectiveType) {
                Log.d(TAG, "On wifi changed");
                Map<String, String> map = new HashMap<>();
                map.put("type", type);
                map.put("effectiveType", effectiveType);
                sendEvent("ConfigWifiEventName", map);
            }
        });
        mNetworkMonitor.startMonitor();
    }

    private void buildConstants() {
        mConstants.put("ConfigDeviceEventName", "ConfigEventDevice");
        mConstants.put("ConfigWifiEventName", "ConfigEventWifi");
    }

    private void sendEvent(String key, Map data) {
        if (!mConstants.containsKey(key)) {
            Log.e(TAG, "No key in mConstants");
        }
        Map param;
        if (data != null) {
            param = data;
        } else {
            param = new HashMap<>();
        }
        String name = (String) mConstants.get(key);
         Log.e(TAG, "Send event to Js: " + name + " -> " + param);
//        mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(name, param);
    }


    public Map getNetworkStatus() {
        Pair<String, String> pair = mNetworkMonitor.getNetType();
        Map<String, String> map = new HashMap<>();
        map.put("type", pair.first);
        map.put("effectiveType", pair.second);
        return map;
    }

    public Map getSSID() {
        String ssid = "";
        String bssid = "";
        String channel = "";

        if (NetworkUtil.isWifiConnected(mContext)) {
            ssid = NetworkUtil.getCurrentSSID(mContext);
            bssid = NetworkUtil.getCurrentBSSID(mContext);
            channel = "";
            if (NetworkUtil.isWifi5G(mContext)) {
                channel = "5";
            } else if (NetworkUtil.isWifi24G(mContext)) {
                channel = "2.4";
            }
        }
        Log.e(TAG, "ssid : "+ssid);
        if(ssid!=null){
            if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
                                   && ssid.contains("unknown ssid")){
                try {
                    Toast.makeText(mContext, "请开启位置服务", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(myIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                Map<String, String> map = new HashMap<>();
                map.put("SSID", ssid);
                map.put("BSSID", bssid);
                map.put("channel", channel);
                Log.d(TAG, "Get SSID json: " + map);
               return map;
            }
        }
        return null;
    }

    public Map getConfigZip(final String from, final String to, final String password) {
        File file = ConfigZipUtil.zip(from, to, password);
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            Map<String, String> map = new HashMap<>();
            map.put("uri", uri.toString());
            Log.d(TAG, "Zip success: " + file.getAbsolutePath());
            return map;
        } else {
            Log.d(TAG, "Zip fail");
            return null;
        }
    }

    public int softAP(final String ssid, final String pwd, final String pin, final Handler handler) {
        Log.d(TAG, "Config by soft ap mode: " + ssid + "-" + pwd + "-" + pin);
        HashMap<String, String> configuration = new HashMap<>();
        configuration.put("ssid", ssid);
        configuration.put("password", pwd);
        configuration.put("pinCode", pin);
        mDeviceConfig.startSoftAPConfig(mContext, configuration, new ConfigCallback() {
            @Override
            public void onReceive(String ip, String message) {
                Log.d(TAG, "Receive: " + ip + "=>" + message);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("ip", ip);
                bundle.putString("message", message);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
        //Map<String, Integer> map = new HashMap<>();
        //map.put("token", 0);
        return 0;
    }

    public int config(final String ssid, final String pwd, final String pin, final Handler handler) {
        HashMap<String, String> configuration = new HashMap<>();
        configuration.put("ssid", ssid);
        configuration.put("password", pwd);
        configuration.put("pinCode", pin);
        mDeviceConfig.startConfig(mContext, configuration, new ConfigCallback() {
            @Override
            public void onReceive(String ip, String message) {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("ip", ip);
                bundle.putString("message", message);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });
//        Map<String, Integer> map = new HashMap<>();
//        map.put("token", 0);
        return 0;
    }

    public void send(final int token, final String msg, final String addr) {
        if (addr != null) {
            try {
                String[] address = addr.split(":");
                if (address.length == 2) {
                    CommonUdpConn.getInstance(mContext).send(msg, address[0], Integer.parseInt(address[1]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(final int token) {
        mDeviceConfig.stopConfig();
    }
}

