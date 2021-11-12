package com.kunluiot.sdk

import android.app.Application
import com.kunluiot.sdk.api.family.IKunLuFamily
import com.kunluiot.sdk.api.family.KunLuFamilyImpl
import com.kunluiot.sdk.api.user.IKunLuUser
import com.kunluiot.sdk.api.user.KunLuUserImpl
import com.kunluiot.sdk.bean.user.SessionBean
import com.kunluiot.sdk.request.JsonConverter
import com.kunluiot.sdk.request.ReqApi
import com.kunluiot.sdk.request.UserApi
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.KalleConfig
import com.kunluiot.sdk.thirdlib.kalle.connect.BroadcastNetwork
import com.kunluiot.sdk.thirdlib.kalle.connect.http.LoggerInterceptor
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
import com.kunluiot.sdk.thirdlib.ws.WebsocketUtil

class KunLuHomeSdk {

    private lateinit var app: Application
    private lateinit var appKey: String
    private lateinit var appSecret: String
    private var msgId = 1

    private fun setKalle() {
        Kalle.setConfig(KalleConfig.newBuilder()
//            .readTimeout(30, TimeUnit.SECONDS)
//            .connectionTimeout(30, TimeUnit.SECONDS)
//            .connectFactory(OkHttpConnectFactory.newBuilder().build())
//            .cookieStore(DBCookieStore.newBuilder(this).build())
//            .cacheStore(DiskCacheStore.newBuilder(AppConfig.get().PATH_APP_CACHE).build())
            .network(BroadcastNetwork(app))
//            .addInterceptor(LoginInterceptor())
            .addInterceptor(LoggerInterceptor("KunLuSDK", BuildConfig.DEBUG)).converter(JsonConverter(app)).build())
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

    fun getSessionBean(): SessionBean? {
        val sessionJson = SPUtil.get(app, UserApi.KHA_API_LOGIN, "") as String
        return JsonUtils.fromJson(sessionJson, SessionBean::class.java)
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
    }
}
