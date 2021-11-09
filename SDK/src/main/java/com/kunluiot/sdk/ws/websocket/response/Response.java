package com.kunluiot.sdk.ws.websocket.response;


import com.kunluiot.sdk.ws.websocket.dispatcher.IResponseDispatcher;
import com.kunluiot.sdk.ws.websocket.dispatcher.ResponseDelivery;

/**
 * WebSocket 响应数据接口
 * Created by ZhangKe on 2018/6/26.
 */
public interface Response<T> {

    /**
     * 获取响应的数据
     */
    T getResponseData();

    /**
     * 设置响应的数据
     */
    void setResponseData(T responseData);

    void onResponse(IResponseDispatcher dispatcher, ResponseDelivery delivery);

    /**
     * 回收资源
     */
    void release();
}
