package com.kunluiot.sdk.helper

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.user.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.*
import com.kunluiot.sdk.kalle.Kalle
import com.kunluiot.sdk.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.SPUtil
import com.kunluiot.sdk.ws.WebsocketUtil
import java.io.File

object UserRequestUtil {

    /**
     * 账号密码登录
     */
    fun loginWithPhonePassword(countryCode: String, phone: String, passwd: String, callback: ILoginCallback) {
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_LOGIN)
            .setHeaders(KunLuHelper.getSign())
            .param("username", phone)
            .param("password", passwd)
            .param("clientType", "ANDROID")
            .param("areaCode", countryCode)
            .perform(object : KunLuNetCallback<BaseRespBean<SessionBean>>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<BaseRespBean<SessionBean>, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    val data = response.succeed()
                    if (data.status != 200) {
                        callback.onError(data.status.toString(), data.message)
                    } else {
                        SPUtil.apply(KunLuHomeSdk.instance.getApp(), UserApi.KHA_API_LOGIN, data.data)
                        val user = User()
                        user.uid = data.data.user
                        callback.onSuccess(user)
                        WebsocketUtil.init(ReqApi.KHA_WEB_SOCKET_URL)
                    }
                }
            }
        })
    }

    /**
     * 获取图像验证
     */
    fun getVerifyImageCode(callback: IVerifyImageCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GET_VERIFY_IMAGE_CODE)
            .setHeaders(KunLuHelper.getSign())
            .perform(object : KunLuNetCallback<BaseRespBean<VerifyImageBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<VerifyImageBean>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 检测图像验证有效性
     */
    fun checkVerifyImageCode(rid: String, code: String, callback: ICheckVerifyImageCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_CHECK_VERIFY_IMAGE_CODE)
            .setHeaders(KunLuHelper.getSign())
            .param("rid", rid)
            .param("code", code)
            .perform(object : KunLuNetCallback<BaseRespBean<CheckVerifyImageBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<CheckVerifyImageBean>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 获取验证码
     */
    fun getVerifyCode(phoneNumber: String, type: String, token: String, callback: IResultCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GET_VERIFY_CODE)
            .setHeaders(KunLuHelper.getSign())
            .param("type", type)
            .param("phoneNumber", phoneNumber)
            .param("token", token)
            .perform(object : KunLuNetCallback<BaseRespBean<String>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<String>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess()
                        }
                    }
                }
            })
    }

    /**
     * 检测验证码有效性
     */
    fun checkVerifyCode(phoneNumber: String, type: String, areaCode: String, code: String, callback: ICodeCallback) {
        var area = areaCode
        if (area.isEmpty()) area = "86"
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_CHECK_VERIFY_CODE)
            .setHeaders(KunLuHelper.getSign())
            .param("type", type)
            .param("phoneNumber", phoneNumber)
            .param("areaCode", area)
            .param("code", code)
            .perform(object : KunLuNetCallback<BaseRespBean<VerifyCodeBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<VerifyCodeBean>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 注册
     */
    fun register(phoneNumber: String, password: String, token: String, callback: IRegisterCallback) {
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_REGISTER)
            .setHeaders(KunLuHelper.getSign())
            .param("phoneNumber", phoneNumber)
            .param("password", password)
            .param("token", token)
            .perform(object : KunLuNetCallback<BaseRespBean<User>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<User>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 重置密码
     */
    fun resetPassword(phoneNumber: String, password: String, token: String, callback: IResetPasswordCallback) {
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_RESET_PASSWORD)
            .setHeaders(KunLuHelper.getSign())
            .param("phoneNumber", phoneNumber)
            .param("password", password)
            .param("token", token)
            .perform(object : KunLuNetCallback<BaseRespBean<User>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<User>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }

    /**
     * 更新用户昵称
     */
    fun updateUserNick(nick: String, callback: IResultCallback) {
//        try {
//            val params: MutableMap<String, Any> = HashMap()
//            params["lastName"] = nick
//            KunluRequest().setParams(params).setUrl(UserApi.KHA_API_GETUSERINFO).setMethod("PUT").setCallback(object : ReqCallback<BaseRespBean<Any>> {
//                override fun success(result: BaseRespBean<Any>) {
//                    callback.onSuccess()
//                }
//
//                override fun error(code: String, msg: String) {
//                    callback.onError(code, msg)
//                }
//            }).request()
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
    }

    /**
     * 获取用户详情
     */
    fun getUserInfo(callback: IUserCallback) {
//        try {
//            KunluRequest().setUrl(UserApi.KHA_API_GETUSERINFO).setCallback(object : ReqCallback<BaseRespBean<User>> {
//                override fun error(code: String, msg: String) {
//                    callback.onError(code, msg)
//                }
//
//                override fun success(result: BaseRespBean<User>) {
//                    result.data.let { callback.onSuccess(it) }
//                }
//            }).request()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    /**
     * 上传用户头像
     */
    fun uploadHeader(file: File, callback: IAvatarCallback) {
//        try {
//            KunluRequest().setCallback(object : ReqCallback<BaseRespBean<AvatarBean>> {
//                override fun error(code: String, msg: String) {
//                    callback.onError(code, msg)
//                }
//
//                override fun success(result: BaseRespBean<AvatarBean>) {
//                    result.data.let { callback.onSuccess(it) }
//                }
//            }).setFile(file).request()
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
    }
}