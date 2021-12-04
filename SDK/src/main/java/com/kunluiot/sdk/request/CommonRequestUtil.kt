package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.common.CommonMessageListBean
import com.kunluiot.sdk.bean.common.CommonProblemBean
import com.kunluiot.sdk.bean.common.CommonThirdPlatformBean
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.*
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.Callback
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.thirdlib.ws.websocket.util.LogUtil
import com.kunluiot.sdk.util.*
import java.io.File
import java.lang.Exception

object CommonRequestUtil {

    /**
     * 下载JS zip文件
     * */
    fun downloadsUrlFile(url: String, fail: OnFailResult, success: OnSuccessStrResult) {
        if (url.isEmpty()) {
            fail.fail("-1", "downloadsUrlFile url is empty")
            return
        }
        val cacheDir = KunLuHomeSdk.instance.getApp().cacheDir.absolutePath + File.separator + KunLuHelper.CACHE_DIR_NAME + File.separator + KunLuHelper.CACHE_URL_NAME + File.separator + MD5Util.md5(url)
        val indexHtml = cacheDir + File.separator + KunLuHelper.CACHE_INDEX_HTML

        Kalle.Download.get(url).directory(cacheDir).fileName(KunLuHelper.CACHE_ZIP).perform(object : DownloadCallback(KunLuHomeSdk.instance.getApp()) {

            override fun onException(message: String) {
                fail.fail("-1", "Download err1 == $message")
            }

            override fun onFinish(path: String) {
                val isOk = ZipUtils.unzipFile(path)
                if (isOk) {
                    try {
                        FileUtil.insertTextFile(indexHtml, indexHtml, "<script type='text/javascript'>${KunLuHelper.INJECT_JS}</script>", "<script")
                        success.success(indexHtml)
                    } catch (e: Exception) {
                        fail.fail("-2", "Download err1 == ${e.message.toString()}")
                    }
                }
            }
        })
    }

    // -------------------------------------

    /**
     * 常见问题列表
     * */
    fun getCommonProblem(callback: ICommonProblemCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_COMMON_PROBLEM).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<CommonProblemBean>(response.succeed()).let { callback.onSuccess(it) }
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
        Kalle.post(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_FEEDBACK).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(param)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<CommonProblemBean>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     * 平台消息列表
     * */
    fun getMessagePlatform(page: Int, size: Int, callback: ICommonMsgListCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_MESSAGE_PLATFORM).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("page", page).param("size", size).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<CommonMessageListBean>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }

    /**
     * 设备消息列表
     * */
    fun getMessageDevice(page: Int, size: Int, callback: ICommonMsgListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("page", page).param("size", size).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<CommonMessageListBean>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }

    /**
     * 设备消息置为已读
     * */
    fun readMessageDevice(id: String, callback: IResultCallback) {
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE_READ + "/$id").addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     * 设备消息全部置为已读
     * */
    fun allReadMessageDevice(callback: IResultCallback) {
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_MESSAGE_DEVICE_ALL_READ).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     * 设备消息清空
     * */
    fun emptyMessageDevice(callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE_EMPTY).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                    }
                }
            })
    }

    /**
     * 绑定的第三方平台列表
     * */
    fun getBindThirdPlatformList(callback: ICommonThirdPlatformCallback) {
        Kalle.get(ReqApi.KHA_UAA_BASE_URL + CommonApi.KHA_API_BIND_THIRD_PLATFORM_LIST).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<CommonThirdPlatformBean>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }


}