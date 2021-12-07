package com.kunluiot.sdk

import android.app.Application
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.kunluiot.sdk.api.common.IKunLuCommon
import com.kunluiot.sdk.api.common.KunLuCommonImpl
import com.kunluiot.sdk.api.device.IKunLuDevice
import com.kunluiot.sdk.api.device.KunLuDeviceImpl
import com.kunluiot.sdk.api.family.IKunLuFamily
import com.kunluiot.sdk.api.family.KunLuFamilyImpl
import com.kunluiot.sdk.api.scene.IKunLuScene
import com.kunluiot.sdk.api.scene.KunLuSceneImpl
import com.kunluiot.sdk.api.user.IKunLuUser
import com.kunluiot.sdk.api.user.KunLuUserImpl
import com.kunluiot.sdk.bean.user.SessionBean
import com.kunluiot.sdk.request.KunLuHelper
import com.kunluiot.sdk.request.KunLuJsonConverter
import com.kunluiot.sdk.request.ReqApi
import com.kunluiot.sdk.request.UserApi
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.KalleConfig
import com.kunluiot.sdk.thirdlib.kalle.connect.BroadcastNetwork
import com.kunluiot.sdk.thirdlib.kalle.connect.http.LoggerInterceptor
import com.kunluiot.sdk.thirdlib.ws.WebsocketUtil
import com.kunluiot.sdk.thirdlib.ws.websocket.WebSocketManager
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil

class KunLuHomeSdk {

    private lateinit var app: Application
    private lateinit var appKey: String
    private lateinit var appSecret: String
    private var msgId = 1

    internal fun getAppKey(): String = appKey
    internal fun getAppSecret(): String = appSecret

    private fun setKalle() {
        XLog.init(LogLevel.ALL)
        val build = KalleConfig.newBuilder().network(BroadcastNetwork(app)).addInterceptor(LoggerInterceptor("KunLuSDK", BuildConfig.DEBUG)).converter(KunLuJsonConverter(app))
        if (ReqApi.IS_NEW_TEST) {
            build.addHeaders(KunLuHelper.getSign())
        }
        Kalle.setConfig(build.build())
    }

    /**
     * 初始化SDK
     */
    fun init(app: Application) {
        this.app = app
        setKalle()
    }

    fun init(app: Application, appKey: String, appSecret: String) {
        this.app = app
        this.appKey = appKey
        this.appSecret = appSecret
        setKalle()
    }

    fun getApp(): Application {
        return app
    }

    fun logout() {
        SPUtil.apply(instance.getApp(), UserApi.KHA_API_LOGIN, "")
        webSocketDisConnect()
    }

    fun getSessionBean(): SessionBean? {
        val sessionJson = SPUtil.get(app, UserApi.KHA_API_LOGIN, "") as String
        return JsonUtils.fromJson(sessionJson, SessionBean::class.java)
    }

    fun getWebSocketManager(): WebSocketManager? {
        return WebsocketUtil.getWebSocketManager()
    }

    fun getMsgId(): Int {
        msgId++
        return msgId
    }

    fun webSocketDisConnect() {
        WebsocketUtil.webSocketDisConnect(ReqApi.KHA_WEB_SOCKET_URL)
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            KunLuHomeSdk()
        }

        val userImpl by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            val user: IKunLuUser = KunLuUserImpl()
            user
        }

        val familyImpl by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            val family: IKunLuFamily = KunLuFamilyImpl()
            family
        }

        val deviceImpl by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            val device: IKunLuDevice = KunLuDeviceImpl()
            device
        }

        val commonImpl by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            val common: IKunLuCommon = KunLuCommonImpl()
            common
        }

        val sceneImpl by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            val scene: IKunLuScene = KunLuSceneImpl()
            scene
        }
    }
}
