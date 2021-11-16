package com.kunluiot.sdk.api.common

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.ICommonProblemCallback
import com.kunluiot.sdk.request.CommonRequestUtil

internal class KunLuCommonImpl : IKunLuCommon {

    /**
     * 常见问题列表
     * */
    override fun getCommonProblem(callback: ICommonProblemCallback) {
        CommonRequestUtil.getCommonProblem(callback)
    }

    /**
     * 意见反馈
     * */
    override fun feedback(username: String, title: String, content: String, images: String, contact: String, callback: IResultCallback) {
        CommonRequestUtil.feedback(username, title, content, images, contact, callback)
    }
}