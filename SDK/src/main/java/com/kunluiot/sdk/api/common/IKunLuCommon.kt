package com.kunluiot.sdk.api.common

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.*

interface IKunLuCommon {

    /**
     * 下载文件
     * */
    fun downloadsUrlFile(url: String, fail: OnFailResult, success: OnSuccessStrResult)

    /**
     * 平台消息列表
     */
    fun getMessagePlatform(page: Int, size: Int, fail: OnFailResult, success: CommonMsgListResult)

    /**
     * 设备消息列表
     * */
    fun getMessageDevice(page: Int, size: Int, fail: OnFailResult, success: CommonMsgListResult)

    /**
     * 设备消息置为已读
     */
    fun readMessageDevice(id: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 平台消息置为已读
     */
    fun readMessagePlatform(id: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 设备消息全部置为已读
     * */
    fun allReadMessageDevice(fail: OnFailResult, success: OnSuccessResult)

    /**
     * 设备消息清空
     */
    fun emptyMessageDevice(fail: OnFailResult, success: OnSuccessResult)

    /**
     * 平台消息清空
     */
    fun emptyMessagePlatform(fail: OnFailResult, success: OnSuccessResult)

    /**
     * 意见反馈
     * */
    fun feedback(content: String, images: List<String>, contact: String, fail: OnFailResult, success: OnSuccessResult)


    //----------------------------------------------

    /**
     * 常见问题列表
     * */
    fun getCommonProblem(callback: ICommonProblemCallback)

    /**
     * 绑定的第三方平台列表
     * */
    fun getBindThirdPlatformList(callback: ICommonThirdPlatformCallback)

}