package com.kunluiot.sdk.util;

import android.text.TextUtils;

import com.kunluiot.sdk.bean.device.DeviceNewBean;

import java.util.HashMap;
import java.util.Locale;

public class Tools {

    public static HashMap<String, String> parseUrl(String url) {
        HashMap<String, String> strUrlParas = new HashMap<>();
        if (TextUtils.isEmpty(url)) return strUrlParas;
        String strUrl = "";
        String strUrlParams = "";
        if (url.contains("?")) {
            String[] strUrlPatten = url.split("\\?");
            strUrl = strUrlPatten[0];
            strUrlParams = strUrlPatten[1];
        } else {
            strUrl = url;
            strUrlParams = url;
        }
        strUrlParas.put("URL", strUrl);
        String[] params = null;
        if (strUrlParams.contains("&")) {
            params = strUrlParams.split("&");
        } else {
            params = new String[]{strUrlParams};
        }
        for (String p : params) {
            if (p.contains("=")) {
                String[] param = p.split("=");
                if (param.length == 1) {
                    strUrlParas.put(param[0], "");
                } else {
                    String key = param[0];
                    String value = param[1];
                    strUrlParas.put(key, value);
                }
            } else {
                strUrlParas.put("errorParam", p);
            }
        }
        return strUrlParas;
    }

    public static String getLanguage() {
        String lan = Locale.getDefault().getLanguage();
        if ("zh".equals(lan)) {
            return "zh-CN";
        }
        return lan;
    }

    public static String getAppTid() {
        return ("sdm845");
    }

    public static String getProtocolCacheId(DeviceNewBean device) {
        if (device.getDevType().equals("SUB")) {
            if (!TextUtils.isEmpty(device.getAndroidPageZipURL())) {
                return "" + device.getAndroidPageZipURL().hashCode();
            }
        }
        return device.getCtrlKey() + device.getDevTid();
    }
}
