package com.kunluiot.sdk.thirdlib.wifi.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import android.util.Pair;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by hucn on 2017/3/31.
 * Author: hucn
 * Description: 监听网络状态的变化
 */

public class NetworkMonitor {

    private static final String TAG = NetworkMonitor.class.getSimpleName();

    private static NetworkMonitor instance;

    private CopyOnWriteArrayList<NetObservable> mObservables;
    private Context mContext;
    private NetworkReceiver mReceiver;

    public static NetworkMonitor getInstance() {
        if (instance == null) {
            synchronized (NetworkMonitor.class) {
                if (instance == null) {
                    instance = new NetworkMonitor();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private NetworkMonitor() {
        mReceiver = new NetworkReceiver();
        mObservables = new CopyOnWriteArrayList<>();
    }

    public void startMonitor() {
        Log.d(TAG, "Start Monitor");
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mReceiver, filter);
    }

    public void stopMonitor() {
        Log.d(TAG, "Stop Monitor");
        mContext.unregisterReceiver(mReceiver);
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            Log.d(TAG, "Connectivity action: " + action);
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                if (NetworkUtil.isWifiConnected(mContext)) {
                    notifyWifiChanged("wifi", "");
                } else if (NetworkUtil.isMobileConnected(mContext)) {
                    notifyWifiChanged("cellular", NetworkUtil.getNetworkClass(mContext));
                } else if (NetworkUtil.isConnected(mContext)) {
                    notifyWifiChanged("unknown", "");
                } else {
                    notifyWifiChanged("none", "");
                }
            }
        }
    }

    private void notifyWifiChanged(String type, String effectiveType) {
        Log.i(TAG, "Net changed, type: " + type + ", effectiveType: " + effectiveType);
        for (NetObservable connectable : mObservables) {
            connectable.onWifiChanged(type, effectiveType);
        }
    }

    public void add(NetObservable observable) {
        if (!mObservables.contains(observable)) {
            Log.d(TAG, "Add observable: " + observable);
            mObservables.add(observable);
        }
    }

    public void remove(NetObservable observable) {
        Log.d(TAG, "Remove observable: " + observable);
        mObservables.remove(observable);
    }

    public Pair<String, String> getNetType() {
        if (NetworkUtil.isWifiConnected(mContext)) {
            return new Pair<>("wifi", "");
        } else if (NetworkUtil.isMobileConnected(mContext)) {
            return new Pair<>("cellular", NetworkUtil.getNetworkClass(mContext));
        } else if (NetworkUtil.isConnected(mContext)) {
            return new Pair<>("unknown", "");
        } else {
            return new Pair<>("none", "");
        }
    }
}
