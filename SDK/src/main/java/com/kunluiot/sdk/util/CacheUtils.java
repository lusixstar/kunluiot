package com.kunluiot.sdk.util;


import com.kunluiot.sdk.KunLuHomeSdk;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class CacheUtils {

    public static String getCache(String url) {
        String key = MD5Util.md5(url);
        String json = SPUtil.get(KunLuHomeSdk.Companion.getInstance().getApp(), key, "").toString();
        try {
            json = URLDecoder.decode(json, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static void setCache(String url, String object) {
        try {
            String key = MD5Util.md5(url);
            String json = URLEncoder.encode(object, "UTF-8");

            SPUtil.apply(KunLuHomeSdk.Companion.getInstance().getApp(), key, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean containsKey(String key) {
        key = MD5Util.md5(key);
        return SPUtil.contains(KunLuHomeSdk.Companion.getInstance().getApp(), key);
    }

    public static void clearCache(String url) {
        String key = MD5Util.md5(url);
        SPUtil.remove(KunLuHomeSdk.Companion.getInstance().getApp(), key);
    }
}
