package com.kunluiot.sdk.api.common

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.ICommonProblemCallback

interface IKunLuCommon {

    /**
     * 常见问题列表
     * */
    fun getCommonProblem(callback: ICommonProblemCallback)

    /**
     * 意见反馈
     * */
    fun feedback(username: String, title: String, content: String, images: String, contact: String, callback: IResultCallback)
}