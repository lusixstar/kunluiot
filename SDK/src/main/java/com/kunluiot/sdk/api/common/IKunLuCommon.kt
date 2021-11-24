package com.kunluiot.sdk.api.common

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.ICommonMsgListCallback
import com.kunluiot.sdk.callback.common.ICommonProblemCallback
import com.kunluiot.sdk.callback.common.ICommonThirdPlatformCallback

interface IKunLuCommon {

    /**
     * 常见问题列表
     * */
    fun getCommonProblem(callback: ICommonProblemCallback)

    /**
     * 意见反馈
     * */
    fun feedback(username: String, title: String, content: String, images: String, contact: String, callback: IResultCallback)

    /**
     * 平台消息列表
     */
    fun getMessagePlatform(page: Int, size: Int, callback: ICommonMsgListCallback)

    /**
     * 设备消息列表
     * */
    fun getMessageDevice(page: Int, size: Int, callback: ICommonMsgListCallback)

    /**
     * 设备消息置为已读
     */
    fun readMessageDevice(id: String, callback: IResultCallback)

    /**
     * 设备消息全部置为已读
     * */
    fun allReadMessageDevice(callback: IResultCallback)

    /**
     * 设备消息清空
     */
    fun emptyMessageDevice(callback: IResultCallback)

    /**
     * 绑定的第三方平台列表
     * */
    fun getBindThirdPlatformList(callback: ICommonThirdPlatformCallback)

    /**
     * 下载文件
     * */
    fun downloadsUrlFile(url: String)
}