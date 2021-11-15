package com.kunluiot.sdk.thirdlib.wifi;

/**
 * Created by Mike on 2018/7/24.
 * Author:
 * Description:
 */
public interface IConfig {

    int startConfig(String ssid, String password, String pinCode, ConfigCallback callback);

    void stopConfig();

    void sendUdp(int token, String ip, int port, String data);

    String softAP(String ssid, String password, String pinCode);
}
