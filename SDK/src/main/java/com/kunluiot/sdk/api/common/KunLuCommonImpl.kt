package com.kunluiot.sdk.api.common

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.*
import com.kunluiot.sdk.request.CommonRequestUtil

internal class KunLuCommonImpl : IKunLuCommon {


    /**
     * 下载文件
     * */
    override fun downloadsUrlFile(url: String, fail: OnFailResult, success: OnSuccessStrResult) {
        CommonRequestUtil.downloadsUrlFile(url, fail, success)
    }


    // ----------------------------------------

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

    /**
     * 平台消息列表
     */
    override fun getMessagePlatform(page: Int, size: Int, callback: ICommonMsgListCallback) {
        CommonRequestUtil.getMessagePlatform(page, size, callback)
    }

    /**
     * 设备消息列表
     * */
    override fun getMessageDevice(page: Int, size: Int, callback: ICommonMsgListCallback) {
        CommonRequestUtil.getMessageDevice(page, size, callback)
    }

    /**
     * 设备消息置为已读
     */
    override fun readMessageDevice(id: String, callback: IResultCallback) {
        CommonRequestUtil.readMessageDevice(id, callback)
    }

    /**
     * 设备消息全部置为已读
     * */
    override fun allReadMessageDevice(callback: IResultCallback) {
        CommonRequestUtil.allReadMessageDevice(callback)
    }

    /**
     * 设备消息清空
     */
    override fun emptyMessageDevice(callback: IResultCallback) {
        CommonRequestUtil.emptyMessageDevice(callback)
    }

    /**
     * 绑定的第三方平台列表
     * */
    override fun getBindThirdPlatformList(callback: ICommonThirdPlatformCallback) {
        CommonRequestUtil.getBindThirdPlatformList(callback)
    }




}