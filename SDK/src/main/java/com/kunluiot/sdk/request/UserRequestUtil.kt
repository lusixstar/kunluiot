package com.kunluiot.sdk.request

import com.elvishew.xlog.XLog
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.user.AvatarBean
import com.kunluiot.sdk.bean.user.SessionBean
import com.kunluiot.sdk.bean.user.User
import com.kunluiot.sdk.bean.user.VerifyCodeBean
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.common.OnSuccessStrResult
import com.kunluiot.sdk.callback.user.AvatarSuccessResult
import com.kunluiot.sdk.callback.user.LoginSuccessResult
import com.kunluiot.sdk.callback.user.UserSuccessResult
import com.kunluiot.sdk.callback.user.VerifyCodeSuccessResult
import com.kunluiot.sdk.thirdlib.kalle.FileBinary
import com.kunluiot.sdk.thirdlib.kalle.FormBody
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.thirdlib.ws.WebsocketUtil
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.KotlinSerializationUtils
import com.kunluiot.sdk.util.SPUtil
import java.io.File

object UserRequestUtil {

    /**
     * 刷新登录token
     */
    fun refreshToken(refreshToken: String, fail: OnFailResult, success: LoginSuccessResult) {
        val map = mutableMapOf<String, String>()
        map["refresh_token"] = refreshToken
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_REFRESH_TOKEN)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SessionBean>(response.succeed()).let { data ->
                        SPUtil.apply(KunLuHomeSdk.instance.getApp(), UserApi.KHA_API_LOGIN, data)
                        val user = User()
                        user.uid = data.user
                        success.success(user)
                        WebsocketUtil.init(ReqApi.KHA_WEB_SOCKET_URL)
                    }
                }
            }
        })
    }

    /**
     * 登录
     */
    fun login(phone: String, passwd: String, fail: OnFailResult, success: LoginSuccessResult) {
        val kalle = Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_LOGIN)
        kalle.param("username", phone)
        kalle.param("password", passwd)
        kalle.param("clientType", "ANDROID")
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SessionBean>(response.succeed()).let { data ->
                        SPUtil.apply(KunLuHomeSdk.instance.getApp(), UserApi.KHA_API_LOGIN, data)
                        val user = User()
                        user.uid = data.user
                        success.success(user)
                        WebsocketUtil.init(ReqApi.KHA_WEB_SOCKET_URL)
                    }
                }
            }
        })
    }

    /**
     * 获取验证码
     */
    fun getVerifyCode(phoneNumber: String, type: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GET_VERIFY_CODE)
        kalle.param("phoneNumber", phoneNumber).param("type", type)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 检测验证码有效性
     */
    fun checkVerifyCode(phoneNumber: String, type: String, code: String, fail: OnFailResult, success: VerifyCodeSuccessResult) {
        val kalle = Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_CHECK_VERIFY_CODE)
        kalle.param("type", type).param("phoneNumber", phoneNumber).param("code", code)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<VerifyCodeBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 注册
     */
    fun register(phoneNumber: String, password: String, token: String, fail: OnFailResult, success: UserSuccessResult) {
        val map = mutableMapOf<String, String>()
        map["phoneNumber"] = phoneNumber
        map["password"] = password
        map["token"] = token
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_REGISTER)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<User>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 重置密码
     */
    fun resetPassword(phoneNumber: String, password: String, token: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_RESET_PASSWORD)
        kalle.param("phoneNumber", phoneNumber).param("password", password).param("token", token)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 获取用户详情
     */
    fun getUserInfo(fail: OnFailResult, success: UserSuccessResult) {
        val kalle = Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GETUSERINFO)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<User>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 更新用户昵称
     */
    fun updateUserNick(nick: String, fail: OnFailResult, success: OnSuccessResult) {
        val map = mutableMapOf<String, String>()
        map["lastName"] = nick
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.put(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GETUSERINFO)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 上传用户头像
     */
    fun uploadHeader(filePath: String, fail: OnFailResult, success: AvatarSuccessResult) {
        val boundary = KunLuHelper.getUuid()
        val binary = FileBinary(File(filePath))
        val formBody: FormBody = FormBody.newBuilder().boundary(boundary).binary("file", binary).build()

        formBody.onProgress { _, progress -> XLog.d("formBody progress == $progress") }

        val kalle = Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_UPDATE_PHOTO)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(formBody)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<AvatarBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 更新用户头像
     */
    fun updateHeader(url: String, fail: OnFailResult, success: OnSuccessResult) {
        val m = mutableMapOf<String, String>()
        m["small"] = url
        val map = mutableMapOf<String, Map<String, String>>()
        map["avatarUrl"] = m
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.put(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GETUSERINFO)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 修改密码
     */
    fun changePassword(oldPassword: String, newPassword: String, fail: OnFailResult, success: OnSuccessResult) {
        val map = mutableMapOf<String, String>()
        map["oldPassword"] = oldPassword
        map["newPassword"] = newPassword
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_CHANGE_PASSWORD)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 修改手机号码
     */
    fun changePhoneNum(verifyCode: String, token: String, phoneNumber: String, fail: OnFailResult, success: OnSuccessResult) {
        val map = mutableMapOf<String, String>()
        map["verifyCode"] = verifyCode
        map["token"] = token
        map["phoneNumber"] = phoneNumber
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.post(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_CHANGE_PHONE_NUM)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 获取设备数量
     */
    fun getDeviceCount(fail: OnFailResult, success: OnSuccessStrResult) {
        val kalle = Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_GET_DEVICE_COUNT)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 绑定第三方账号
     */
    fun bindOtherAccount(token: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_BIND_OTHER_ACCOUNT)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("token", token)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SessionBean>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 解绑第三方账号
     */
    fun unBindOtherAccount(type: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.get(ReqApi.KHA_UAA_BASE_URL + UserApi.KHA_API_UNBIND_OTHER_ACCOUNT)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("type", type)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SessionBean>(response.succeed()).let { success.success() }
                }
            }
        })
    }
}