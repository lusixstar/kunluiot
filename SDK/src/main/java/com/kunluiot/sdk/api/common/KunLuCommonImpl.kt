package com.kunluiot.sdk.api.common

import com.kunluiot.sdk.callback.common.*
import com.kunluiot.sdk.request.CommonRequestUtil

internal class KunLuCommonImpl : IKunLuCommon {


    /**
     * 下载文件
     * */
    override fun downloadsUrlFile(url: String, fail: OnFailResult, success: OnSuccessStrResult) {
        CommonRequestUtil.downloadsUrlFile(url, fail, success)
    }

    /**
     * 平台消息列表
     */
    override fun getMessagePlatform(page: Int, size: Int, fail: OnFailResult, success: CommonMsgListResult) {
        CommonRequestUtil.getMessagePlatform(page, size, fail, success)
    }

    /**
     * 设备消息列表
     * */
    override fun getMessageDevice(page: Int, size: Int, fail: OnFailResult, success: CommonMsgListResult) {
        CommonRequestUtil.getMessageDevice(page, size, fail, success)
    }

    /**
     * 设备消息置为已读
     */
    override fun readMessageDevice(id: String, fail: OnFailResult, success: OnSuccessResult) {
        CommonRequestUtil.readMessageDevice(id, fail, success)
    }

    /**
     * 平台消息置为已读
     */
    override fun readMessagePlatform(id: String, fail: OnFailResult, success: OnSuccessResult) {
        CommonRequestUtil.readMessagePlatform(id, fail, success)
    }

    /**
     * 设备消息全部置为已读
     * */
    override fun allReadMessageDevice(fail: OnFailResult, success: OnSuccessResult) {
        CommonRequestUtil.allReadMessageDevice(fail, success)
    }

    /**
     * 设备消息清空
     */
    override fun emptyMessageDevice(fail: OnFailResult, success: OnSuccessResult) {
        CommonRequestUtil.emptyMessageDevice(fail, success)
    }

    /**
     * 平台消息清空
     */
    override fun emptyMessagePlatform(fail: OnFailResult, success: OnSuccessResult) {
        CommonRequestUtil.emptyMessagePlatform(fail, success)
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
    override fun feedback(username: String, title: String, content: String, images: String, contact: String, fail: OnFailResult, success: OnSuccessResult) {
        CommonRequestUtil.feedback(username, title, content, images, contact, fail, success)
    }


    /**
     * 绑定的第三方平台列表
     * */
    override fun getBindThirdPlatformList(callback: ICommonThirdPlatformCallback) {
        CommonRequestUtil.getBindThirdPlatformList(callback)
    }


}