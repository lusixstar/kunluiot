package com.kunluiot.sdk.net.request;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunluiot.sdk.BuildConfig;
import com.kunluiot.sdk.KunLuHomeSdk;
import com.kunluiot.sdk.bean.common.BaseRespBean;
import com.kunluiot.sdk.enums.RequestEnum;
import com.kunluiot.sdk.net.log.KunLuLog;
import com.kunluiot.sdk.util.JsonUtils;


/**
 * http 请求
 */
public class KunluRequest implements Runnable {

    private String url;
    private String baseUrl = ReqApi.KHA_UAA_BASE_URL;
    private Map<String, Object> header;
    private Object params;
    private String method = "GET";
    private ReqCallback callback;
    private File file;

    public KunluRequest() {

    }

    public KunluRequest(String url, Map<String, Object> header, Object params, String method) {

        this.url = url;
        this.header = header;
        this.params = params;
    }

    public KunluRequest(String url, Map<String, Object> header) {
        this.url = url;
        this.header = header;
    }

    public KunluRequest(String url) {
        this.url = url;
    }

    public KunluRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public KunluRequest setHeader(Map<String, Object> header) {
        this.header = header;
        return this;
    }

    public KunluRequest setParams(Object params) {
        this.params = params;
        return this;
    }

    public KunluRequest setMethod(String method) {
        this.method = method;
        return this;
    }

    public KunluRequest setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public KunluRequest setCallback(ReqCallback callback) {
        this.callback = callback;
        return this;
    }

    public KunluRequest setFile(File file) {
        this.file = file;
        return this;
    }

    public KunluRequest appendUrlPath(String... params) {
        for (String param : params) {
            url += "/" + param;
        }
        return this;
    }

    public KunluRequest appendUrlParams(Map<String, Object> params) {
        if (params instanceof Map) {
            url = url + "?";
            for (String key : params.keySet()) {
                try {
                    String value = URLEncoder.encode(params.get(key).toString(), "UTF-8");
                    url += (key + "=" + value + "&");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            url = url.substring(0, url.length() - 1);
        }
        return this;
    }

    public void request() {
        RequestThreadPool.execute(this);
    }

    @Override
    public void run() {
        if (header == null) {
            header = new HashMap<>();
        }
        if (KunLuHomeSdk.Companion.getInstance().getSessionBean() != null) {
            header.put("authorization", "Bearer " + KunLuHomeSdk.Companion.getInstance().getSessionBean().getAccessToken());
        }
        Message msg = null;
        if (callback != null) {
            msg = new Message();
        }
        try {
            String result = "";
            if (file == null) {
                if ("GET".equals(method)) {
                    if (params instanceof Map) {
                        appendUrlParams((Map) params);
                    }
                    String reqUrl = url;
                    if (!reqUrl.startsWith("http")) {
                        reqUrl = baseUrl + url;
                    }
                    result = HttpUtil.get(reqUrl, header);
                } else {
                    String json = "";
                    if (params != null) {
                        json = new Gson().toJson(params);
                    }
                    KunLuLog.d("post json is " + json);
                    String reqUrl = url;
                    if (!reqUrl.startsWith("http")) {
                        reqUrl = baseUrl + url;
                    }
                    if (TextUtils.isEmpty(method)) {
                        method = "POST";
                    }
                    result = HttpUtil.post(reqUrl, json, header, method);
                }
            } else {
                result = HttpUtil.uploadFile(file.toString(), header);
            }

            if (msg != null) {
                msg.what = RequestEnum.SUCCESS.getCode();
                msg.obj = result;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (msg != null) {
                msg.what = RequestEnum.FAILURE.getCode();
                msg.obj = e.getMessage();
                handler.sendMessage(msg);
            }
        }
    }

    public interface ReqCallback<T> {
        void success(T result);

        void error(String code, String msg);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Type type = Map.class;
            try {
                Type[] type1 = callback.getClass().getGenericInterfaces();
                ParameterizedType ptype = (ParameterizedType) type1[0];
                Type[] iType = ptype.getActualTypeArguments();
                type = iType[0];
            } catch (Exception e) {
                KunLuLog.e("type error " + e.getMessage());
            }
            String result = (String) msg.obj;
            if (!TextUtils.isEmpty(result) && BuildConfig.DEBUG) {
                KunLuLog.d("REQUEST RESULT", result);
            }
            if (result != null && (!result.contains("status") || result.startsWith("["))) {
                try {
                    if ("".equals(result)) {
                        result = JsonUtils.toJson(new Object());
                    }
                    Object ob = new Object();
                    if (result.startsWith("{")) {
                        ob = new Gson().fromJson(result, Map.class);
                    } else if (result.startsWith("[")) {

                        ob = JsonUtils.fromJson(result, new TypeToken<List<Map>>() {});
                    }
                    BaseRespBean<Object> resp = new BaseRespBean<>(0, "", null);
                    resp.setStatus(200);
                    resp.setMessage("success");
                    resp.setData(ob);
                    result = new Gson().toJson(resp);
                } catch (Exception e) {
                    //                    e.printStackTrace();
                    KunLuLog.e("JSON format error json is " + result);
                }
            }

            RequestEnum requestEnum = RequestEnum.code2Enum(msg.what);
            switch (requestEnum) {
                case SUCCESS:
                    callback.success(new Gson().fromJson(result, type));
                    break;
                case FAILURE:
                    break;
            }
        }
    };
}
