package com.kunluiot.sdk.util;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.Map;


public class SPUtil {


    private static final String DEFAULT_NAME = "kunluiot_shared";
    private static final String KEY_FIRST_LOAD = "com.kunluiot.bluetooth.light.KEY_FIRST_LOAD";

    private static final String KEY_LOCATION_IGNORE = "com.kunluiot.bluetooth.light.KEY_LOCATION_IGNORE";

    private static final String KEY_LOG_ENABLE = "com.kunluiot.bluetooth.light.KEY_LOG_ENABLE";

    /**
     * scan device by private mode
     */
    private static final String KEY_PRIVATE_MODE = "com.kunluiot.bluetooth.light.KEY_PRIVATE_MODE";

    private static final String KEY_LOCAL_UUID = "com.kunluiot.bluetooth.light.KEY_LOCAL_UUID";

    private static final String KEY_REMOTE_PROVISION = "com.kunluiot.bluetooth.light.KEY_REMOTE_PROVISION";

    private static final String KEY_FAST_PROVISION = "com.kunluiot.bluetooth.light.KEY_FAST_PROVISION";

    private static final String KEY_NO_OOB = "com.kunluiot.bluetooth.light.KEY_NO_OOB";

    private static String getSpName(Context context) {
        return context.getPackageName() + "_sp";
    }

    /**
     * 保存数据的方法，根据类型调用不同的保存方法
     */
    public static void put(Context context, String key, Object object) {

        if (object == null) {
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(getSpName(context), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else {
            editor.putString(key, object.toString());
        }

        editor.commit();
        //异步提交
        //        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 保存数据的方法，根据类型调用不同的保存方法
     */
    public static void apply(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else {
            editor.putString(key, JsonUtils.toJson(object));
        }

        //        editor.commit();
        //异步提交
        editor.apply();
    }

    /**
     * 获取数据的方法
     */
    public static Object get(Context context, String key, Object defaultObject) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context), Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            return null;
        }
    }

    /**
     * 移除Key对应的value值
     */
    public static void remove(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);

        editor.commit();
        //        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
        //        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询key是否存在
     */
    public static boolean contains(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context), Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll(Context context) {

        SharedPreferences sp = context.getSharedPreferences(getSpName(context), Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static boolean isNoOOBEnable(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_NO_OOB, true);
    }

    public static boolean isPrivateMode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_PRIVATE_MODE, false);
    }
}
