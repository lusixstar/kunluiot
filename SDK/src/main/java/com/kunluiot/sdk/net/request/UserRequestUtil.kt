package com.kunluiot.sdk.net.request

import com.kunluiot.sdk.BuildConfig
import com.kunluiot.sdk.KunLuHomeSdk.Companion.instance
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.user.AvatarBean
import com.kunluiot.sdk.bean.user.SessionBean
import com.kunluiot.sdk.bean.user.User
import com.kunluiot.sdk.bean.user.VerifyCodeBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.IAvatarCallback
import com.kunluiot.sdk.callback.user.ILoginCallback
import com.kunluiot.sdk.callback.user.IUserCallback
import com.kunluiot.sdk.net.request.KunluRequest.ReqCallback
import com.kunluiot.sdk.util.SPUtil
import com.kunluiot.sdk.ws.WebsocketUtil
import java.io.File
import java.util.*

object UserRequestUtil {

    /**
     * 账号密码登录
     */
    fun loginWithPhonePassword(countryCode: String, phone: String, passwd: String, callback: ILoginCallback) {
        val params: MutableMap<String, Any> = HashMap()
        params["username"] = phone
        params["password"] = passwd
        params["clientType"] = "ANDROID"
        params["pid"] = BuildConfig.PID
        params["areaCode"] = countryCode
        try {
            KunluRequest().setUrl(UserApi.KHA_API_LOGIN).setMethod("POST").appendUrlParams(params).setCallback(object : ReqCallback<BaseRespBean<SessionBean>> {
                override fun success(result: BaseRespBean<SessionBean>) {
                    if (result.data.user.isNotEmpty()) {
                        SPUtil.apply(instance.getApp(), UserApi.KHA_API_LOGIN, result.data)
                        val user = User()
                        user.uid = result.data!!.user
                        callback.onSuccess(user)
                        WebsocketUtil.init(ReqApi.KHA_WEB_SOCKET_URL)
                    } else {
                        callback.onError("-1", "user is null")
                    }
                }

                override fun error(code: String, msg: String) {
                    callback.onError(code, msg)
                }
            }).setParams(params).request()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取验证码
     */
    fun getVerifyCode(phoneNumber: String, type: String, areaCode: String, callback: IResultCallback) {
        val params: MutableMap<String, Any> = HashMap()
        params["type"] = type
        params["phoneNumber"] = phoneNumber
        params["pid"] = BuildConfig.PID
        params["areaCode"] = areaCode
        try {
            KunluRequest().setUrl(UserApi.KHA_API_GET_VERIFY_CODE).appendUrlParams(params).setCallback(object : ReqCallback<BaseRespBean<VerifyCodeBean>> {
                override fun success(result: BaseRespBean<VerifyCodeBean>) {
                    callback.onError(result.data.code.toString(), result.data.desc)
                }

                override fun error(code: String, msg: String) {
                    callback.onError(code, msg)
                }
            }).setParams(params).request()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 更新用户昵称
     */
    fun updateUserNick(nick: String, callback: IResultCallback) {
        try {
            val params: MutableMap<String, Any> = HashMap()
            params["lastName"] = nick
            KunluRequest().setParams(params).setUrl(UserApi.KHA_API_GETUSERINFO).setMethod("PUT").setCallback(object : ReqCallback<BaseRespBean<Any>> {
                override fun success(result: BaseRespBean<Any>) {
                    callback.onSuccess()
                }

                override fun error(code: String, msg: String) {
                    callback.onError(code, msg)
                }
            }).request()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取用户详情
     */
    fun getUserInfo(callback: IUserCallback) {
        try {
            KunluRequest().setUrl(UserApi.KHA_API_GETUSERINFO).setCallback(object : ReqCallback<BaseRespBean<User>> {
                override fun error(code: String, msg: String) {
                    callback.onError(code, msg)
                }

                override fun success(result: BaseRespBean<User>) {
                    result.data.let { callback.onSuccess(it) }
                }
            }).request()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 上传用户头像
     */
    fun uploadHeader(file: File, callback: IAvatarCallback) {
        try {
            KunluRequest().setCallback(object : ReqCallback<BaseRespBean<AvatarBean>> {
                override fun error(code: String, msg: String) {
                    callback.onError(code, msg)
                }

                override fun success(result: BaseRespBean<AvatarBean>) {
                    result.data.let { callback.onSuccess(it) }
                }
            }).setFile(file).request()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}