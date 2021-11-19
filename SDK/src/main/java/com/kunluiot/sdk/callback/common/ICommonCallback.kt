package com.kunluiot.sdk.callback.common

import com.kunluiot.sdk.bean.common.CommonMessageListBean
import com.kunluiot.sdk.bean.common.CommonProblemBean
import com.kunluiot.sdk.bean.common.CommonThirdPlatformBean

/**
 * 常见问题列表
 * */
interface ICommonProblemCallback {
    fun onSuccess(bean: CommonProblemBean)
    fun onError(code: String, error: String)
}

/**
 * 平台消息列表
 * */
interface ICommonMsgListCallback {
    fun onSuccess(bean: CommonMessageListBean)
    fun onError(code: String, error: String)
}

/**
 * 绑定的第三方平台
 * */
interface ICommonThirdPlatformCallback {
    fun onSuccess(bean: CommonThirdPlatformBean)
    fun onError(code: String, error: String)
}
