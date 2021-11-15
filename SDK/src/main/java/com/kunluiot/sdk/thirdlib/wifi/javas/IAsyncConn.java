package com.kunluiot.sdk.thirdlib.wifi.javas;


import com.kunluiot.sdk.thirdlib.wifi.ConfigCallback;

/**
 * Created by hucn on 2017/3/20.
 * Author: hucn
 * Description: Service提供给外部的连接接口
 */

public interface IAsyncConn {

    void start();

    void stop();

    void send(String message);

    void send(String message, String ip, int port);

    boolean isRunning();

    boolean isActive();

    void addOnReceiveListener(ConfigCallback callback);

    void removeOnReceiveListener(ConfigCallback callback);
}
