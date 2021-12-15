package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.CommonMessageListBean
import com.kunluiot.sdk.bean.common.CommonProblemBean
import com.kunluiot.sdk.bean.common.CommonThirdPlatformBean
import com.kunluiot.sdk.callback.common.*
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.*
import java.io.File

object CommonRequestUtil {

    /**
     * 下载JS zip文件
     * */
    fun downloadsUrlFile(url: String, fail: OnFailResult, success: OnSuccessStrResult) {
        if (url.isEmpty()) {
            fail.fail("-1", "downloadsUrlFile url is empty")
            return
        }
        val cacheDir = KunLuHomeSdk.instance.getApp().externalCacheDir?.absolutePath + File.separator + KunLuHelper.CACHE_DIR_NAME + File.separator + KunLuHelper.CACHE_URL_NAME + File.separator + MD5Util.md5(url)
//        val cacheDir = KunLuHomeSdk.instance.getApp().cacheDir.absolutePath + File.separator + KunLuHelper.CACHE_DIR_NAME + File.separator + KunLuHelper.CACHE_URL_NAME + File.separator + MD5Util.md5(url)
        val indexHtml = cacheDir + File.separator + KunLuHelper.CACHE_INDEX_HTML

        val kalle = Kalle.Download.get(url)
        kalle.directory(cacheDir)
        kalle.fileName(KunLuHelper.CACHE_ZIP)
        kalle.perform(object : DownloadCallback(KunLuHomeSdk.instance.getApp()) {

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

    /**
     * 平台消息列表
     * */
    fun getMessagePlatform(page: Int, size: Int, fail: OnFailResult, success: CommonMsgListResult) {
        val kalle = Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_MESSAGE_PLATFORM)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("page", page).param("size", size)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<CommonMessageListBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 设备消息列表
     * */
    fun getMessageDevice(page: Int, size: Int, fail: OnFailResult, success: CommonMsgListResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("page", page).param("size", size)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<CommonMessageListBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 设备消息置为已读
     * */
    fun readMessageDevice(id: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.patch(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE_READ + "/$id")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
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
     * 平台消息置为已读
     * */
    fun readMessagePlatform(id: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.put(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_READ_MSG_FOLDER + "/$id/read")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
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
     * 设备消息全部置为已读
     * */
    fun allReadMessageDevice(fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.patch(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_MESSAGE_DEVICE_ALL_READ)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
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
     * 设备消息清空
     * */
    fun emptyMessageDevice(fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_MESSAGE_DEVICE_EMPTY)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
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
     * 平台消息清空
     * */
    fun emptyMessagePlatform(fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.put(ReqApi.KHA_WEB_BASE_URL + CommonApi.KHA_API_CLEAR_MSG)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
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
     * 意见反馈
     * */
    fun feedback(content: String, images: List<String>, contact: String, fail: OnFailResult, success: OnSuccessResult) {
        var img = ""
        images.forEach { img = "$it,$img" }
        if (img.contains(",")) img = img.substring(0, img.length - 1)
        val map = mutableMapOf<String, Any>()
        map["username"] = contact
        map["title"] = "Android反馈"
        map["content"] = content
        map["images"] = img
        map["contact"] = contact
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.post(ReqApi.KHA_CONSOLE_BASE_URL + CommonApi.KHA_API_FEEDBACK)
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