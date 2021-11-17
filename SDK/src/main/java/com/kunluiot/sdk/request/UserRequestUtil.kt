package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.user.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.*
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.log.KunLuLog
import com.kunluiot.sdk.thirdlib.kalle.FileBinary
import com.kunluiot.sdk.thirdlib.kalle.FormBody
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
import com.kunluiot.sdk.thirdlib.ws.WebsocketUtil
import java.io.File

object UserRequestUtil {

    /**
     * 刷新登录token
     */
    fun refreshToken(refreshToken: String, callback: ILoginCallback) {
        val map = mutableMapOf<String, String>()
        map["refresh_token"] = refreshToken
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_REFRESH_TOKEN)
            .setHeaders(KunLuHelper.getSign())
            .body(JsonBody(param))
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
     * 手机号登录
     */
    fun login(countryCode: String, phone: String, passwd: String, callback: ILoginCallback) {
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
     * 绑定第三方账号
     */
    fun bindOtherAccount(token: String, callback: IResultCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_BIND_OTHER_ACCOUNT)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("token", token)
            .perform(object : KunLuNetCallback<BaseRespBean<BaseRespBean<Any>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<BaseRespBean<Any>>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
                    }
                }
            })
    }

    /**
     * 解绑第三方账号
     */
    fun unBindOtherAccount(type: String, callback: IResultCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_UNBIND_OTHER_ACCOUNT)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("type", type)
            .perform(object : KunLuNetCallback<BaseRespBean<BaseRespBean<Any>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<BaseRespBean<Any>>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
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
        val map = mutableMapOf<String, String>()
        map["phoneNumber"] = phoneNumber
        map["password"] = password
        map["token"] = token
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_REGISTER)
            .setHeaders(KunLuHelper.getSign())
            .body(JsonBody(param))
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
    fun resetPassword(phoneNumber: String, password: String, token: String, verifyCode: String, callback: IResultCallback) {
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_RESET_PASSWORD)
            .setHeaders(KunLuHelper.getSign())
            .param("phoneNumber", phoneNumber)
            .param("password", password)
            .param("token", token)
            .param("verifyCode", verifyCode)
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
                    }
                }
            })
    }

    /**
     * 修改密码
     */
    fun changePassword(oldPassword: String, newPassword: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["oldPassword"] = oldPassword
        map["newPassword"] = newPassword
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_CHANGE_PASSWORD)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
                    }
                }
            })
    }

    /**
     * 获取用户详情
     */
    fun getUserInfo(callback: IUserCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GETUSERINFO)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
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
     * 获取设备数量
     */
    fun getDeviceCount(callback: IUserDevicesCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GET_DEVICE_COUNT)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
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
        val map = mutableMapOf<String, String>()
        map["lastName"] = nick
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GETUSERINFO)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
                    }
                }
            })
    }

    /**
     * 更新用户头像
     */
    fun updateHeader(url: String, callback: IResultCallback) {
        val m = mutableMapOf<String, String>()
        m["small"] = url
        val map = mutableMapOf<String, Map<String, String>>()
        map["avatarUrl"] = m
        val param = JsonUtils.toJson(map)
        Kalle.put(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GETUSERINFO)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
                    }
                }
            })
    }

    /**
     * 上传用户头像
     */
    fun uploadHeader(filePath: String, callback: IAvatarCallback) {
        val boundary = KunLuHelper.getUuid()
        val binary = FileBinary(File(filePath))
        val formBody: FormBody = FormBody.newBuilder()
            .boundary(boundary)
            .binary("file", binary)
            .build()

        formBody.onProgress{ _, progress -> KunLuLog.d("formBody progress == $progress") }

        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_UPDATE_PHOTO)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(formBody)
            .perform(object : KunLuNetCallback<BaseRespBean<AvatarBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<AvatarBean>, String>) {
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
     * 修改手机号码
     */
    fun changePhoneNum(verifyCode: String, token: String, phoneNumber: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["verifyCode"] = verifyCode
        map["token"] = token
        map["phoneNumber"] = phoneNumber
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_CHANGE_PHONE_NUM)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        callback.onSuccess()
                    }
                }
            })
    }
}