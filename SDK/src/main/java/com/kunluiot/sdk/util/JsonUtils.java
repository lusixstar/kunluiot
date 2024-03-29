package com.kunluiot.sdk.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
        if (src == src.longValue())
            return new JsonPrimitive(src.longValue());
        return new JsonPrimitive(src);
    });

    public static String getLocalJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    /**
     * 描述：将对象转化为json.
     */
    public static String toJson(Object src) {
        String json = null;
        try {
            Gson gson = gsonBuilder.create();
            json = gson.toJson(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 描述：将列表转化为json.
     */
    public static String toJson(List<?> list) {
        String json = null;
        try {
            Gson gson = gsonBuilder.create();
            json = gson.toJson(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 描述：将json转化为列表.
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        List<?> list = null;
        try {
            Gson gson = gsonBuilder.create();
            Type type = typeToken.getType();
            list = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) list;
    }

    /**
     * 描述：将json转化为对象.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        Object obj = null;
        try {
            Gson gson = gsonBuilder.create();
            obj = gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) obj;
    }
}
