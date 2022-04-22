package com.kunluiot.sdk.thirdlib.ws;


import com.kunluiot.sdk.KunLuHomeSdk;
import com.kunluiot.sdk.thirdlib.ws.websocket.SocketListener;
import com.kunluiot.sdk.thirdlib.ws.websocket.WebSocketHandler;
import com.kunluiot.sdk.thirdlib.ws.websocket.WebSocketManager;
import com.kunluiot.sdk.thirdlib.ws.websocket.WebSocketSetting;
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse;
import com.kunluiot.sdk.util.JsonUtils;

import com.kunluiot.sdk.thirdlib.java_websocket.framing.Framedata;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class WebsocketUtil {

    static Map<String, WebSocketManager> managerMap = new HashMap<>();

    public static WebSocketManager mCloudManager;

    public static WebSocketManager getWebSocketManager() {
        return mCloudManager;
    }

    public static void init(String url) {

        if (managerMap.get(url) == null) {
            WebSocketSetting setting = new WebSocketSetting();

            setting.setConnectUrl(url);//必填

            //设置连接超时时间
            setting.setConnectTimeout(10 * 1000);

            //设置心跳间隔时间
            setting.setConnectionLostTimeout(15);
            setting.setAutoConnect(true);
            //设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
            setting.setReconnectFrequency(100);

            //        //设置Header
            //        setting.setHttpHeaders(header);

            //设置消息分发器，接收到数据后先进入该类中处理，处理完再发送到下游
            setting.setResponseProcessDispatcher(new AppResponseDispatcher());
            //接收到数据后是否放入子线程处理，只有设置了 ResponseProcessDispatcher 才有意义
            setting.setProcessDataOnBackground(true);

            //网络状态发生变化后是否重连，
            //        WebSocketHandler.registerNetworkChangedReceiver(this);// 方法注册网络监听广播
            setting.setReconnectWithNetworkChanged(true);

            //通过 init 方法初始化默认的 WebSocketManager 对象
            WebSocketManager cloudManager = WebSocketHandler.initGeneralWebSocket(url, setting);
            mCloudManager = cloudManager;
            cloudManager.addListener(new SocketListener() {
                @Override
                public void onConnected() {
//                    XLog.e("onConnected start login");
                    websocketLogin(cloudManager);
                }

                @Override
                public void onConnectFailed(Throwable e) {

                }

                @Override
                public void onDisconnect() {

                }

                @Override
                public void onSendDataError(ErrorResponse errorResponse) {
                    //                    LogUtils.e("Cloud websocket onConnectFailed");
                }

                @Override
                public <T> void onMessage(String message, T data) {
//                    KunLuLog.INSTANCE.d("websocket message " + message);
                }

                @Override
                public <T> void onMessage(ByteBuffer bytes, T data) {


                }

                @Override
                public void onPing(Framedata framedata) {

                }

                @Override
                public void onPong(Framedata framedata) {
                    if (cloudManager != null) {
                        cloudManager.send("{\"action\":\"heartbeat\",\"msgId\":" + 1 + "}");
                    }
                }

            });
            //启动连接
            cloudManager.start();

            //注意，需要在 AndroidManifest 中配置网络状态获取权限
            //注册网路连接状态变化广播
            WebSocketHandler.registerNetworkChangedReceiver(KunLuHomeSdk.Companion.getInstance().getApp());
        }
    }

    public static void websocketLogin(WebSocketManager cloudManager) {

        if (KunLuHomeSdk.Companion.getInstance().getSessionBean() != null) { //登录
            if (cloudManager != null && cloudManager.isConnect()) {
                HashMap<String, String> userInfoMap = new HashMap<>();
                userInfoMap.put("appTid", (android.os.Build.BRAND + android.os.Build.MODEL));
                userInfoMap.put("token", KunLuHomeSdk.Companion.getInstance().getSessionBean().getAccessToken());
                HashMap<String, Object> map = new HashMap<>();
                map.put("msgId", KunLuHomeSdk.Companion.getInstance().getMsgId());
                map.put("action", "appLogin");
                map.put("params", userInfoMap);
                cloudManager.send(JsonUtils.toJson(map));
            }
            if (!cloudManager.isConnect()) {
                cloudManager.reconnect();
            }
        }

    }

    public static void webSocketDisConnect(String url) {
        if (managerMap.get(url) != null) {
            WebSocketManager cloudManager = managerMap.get(url);
            if (cloudManager != null && cloudManager.isConnect()) {
                cloudManager.disConnect();
            }
        }
    }
}
