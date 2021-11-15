package com.kunluiot.sdk.thirdlib.wifi.javas;

import com.kunluiot.sdk.thirdlib.wifi.ConfigCallback;
import com.kunluiot.sdk.thirdlib.wifi.IConfig;

/**
 * Created by Mike on 2018/7/25.
 * Author:
 * Description:
 */
public class JavaConfig implements IConfig{


    @Override
    public int startConfig(String ssid, String password, String pinCode, ConfigCallback callback) {
        return 0;
    }

    @Override
    public void stopConfig() {

    }

    @Override
    public void sendUdp(int token, String ip, int port, String data) {

    }

    @Override
    public String softAP(String ssid, String password, String pinCode) {
        return null;
    }
}
