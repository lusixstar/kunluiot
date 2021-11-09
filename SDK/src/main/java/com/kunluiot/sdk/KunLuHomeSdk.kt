package com.kunluiot.sdk

import android.app.Application
import com.kunluiot.sdk.api.user.IKunLuUser
import com.kunluiot.sdk.api.user.KunLuUserImpl
import com.kunluiot.sdk.bean.user.SessionBean
import com.kunluiot.sdk.net.request.ReqApi
import com.kunluiot.sdk.net.request.UserApi
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil

class KunLuHomeSdk {

    private lateinit var app: Application
    private lateinit var appKey: String
    private lateinit var appSecret: String
    private var msgId = 1

    /**
     * 初始化SDK
     */
    fun init(app: Application) {
        this.app = app
    }

    fun init(app: Application, appKey: String, appSecret: String) {
        this.app = app
        this.appKey = appKey
        this.appSecret = appSecret
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

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            KunLuHomeSdk()
        }

        val userImpl by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            val user: IKunLuUser = KunLuUserImpl()
            user
        }
    }
}
