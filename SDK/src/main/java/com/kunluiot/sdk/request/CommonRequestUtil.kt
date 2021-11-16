package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.common.CommonProblemBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.ICommonProblemCallback
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
}