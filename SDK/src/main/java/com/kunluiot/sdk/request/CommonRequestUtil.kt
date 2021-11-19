package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.common.CommonMessageListBean
import com.kunluiot.sdk.bean.common.CommonProblemBean
import com.kunluiot.sdk.bean.common.CommonThirdPlatformBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.ICommonMsgListCallback
import com.kunluiot.sdk.callback.common.ICommonProblemCallback
import com.kunluiot.sdk.callback.common.ICommonThirdPlatformCallback
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils

object CommonRequestUtil {

    /**
     * 常见问题列表
     * */
    fun getCommonProblem(callback: ICommonProblemCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_COMMON_PROBLEM)
            .setHeaders(KunLuHelper.getSign())
            .perform(object : KunLuNetCallback<BaseRespBean<CommonProblemBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<CommonProblemBean>, String>) {
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
     * 意见反馈
     * */
    fun feedback(username: String, title: String, content: String, images: String, contact: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["username"] = username
        map["title"] = title
        map["content"] = content
        map["images"] = images
        map["contact"] = contact
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_FEEDBACK)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<CommonProblemBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<CommonProblemBean>, String>) {
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
     * 平台消息列表
     * */
    fun getMessagePlatform(page: Int, size: Int, callback: ICommonMsgListCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_MESSAGE_PLATFORM)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("page", page)
            .param("size", size)
            .perform(object : KunLuNetCallback<BaseRespBean<CommonMessageListBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<CommonMessageListBean>, String>) {
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
     * 设备消息列表
     * */
    fun getMessageDevice(page: Int, size: Int, callback: ICommonMsgListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("page", page)
            .param("size", size)
            .perform(object : KunLuNetCallback<BaseRespBean<CommonMessageListBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<CommonMessageListBean>, String>) {
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
     * 设备消息置为已读
     * */
    fun readMessageDevice(id: String, callback: IResultCallback) {
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE_READ + "/$id")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
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
     * 设备消息全部置为已读
     * */
    fun allReadMessageDevice(callback: IResultCallback) {
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_MESSAGE_DEVICE_ALL_READ)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
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
     * 设备消息清空
     * */
    fun emptyMessageDevice(callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE_EMPTY)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
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
     * 绑定的第三方平台列表
     * */
    fun getBindThirdPlatformList(callback: ICommonThirdPlatformCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + CommonApi.KHA_API_BIND_THIRD_PLATFORM_LIST)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<CommonThirdPlatformBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<CommonThirdPlatformBean>, String>) {
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
}