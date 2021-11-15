package com.kunluiot.sdk.thirdlib.wifi.utils;

/**
 * Created by hucn on 2017/3/31.
 * Author: hucn
 * Description: 网络状态变化的接口
 */

public interface NetObservable {

    void onWifiChanged(String type, String effectiveType);
}
