package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.callback.device.IPinCodeCallback

interface IKunLuDevice {

    /**
     * 设备列表
     */
    fun list(callback: IDeviceListCallback)

    /**
     * 获取pinCode
     */
    fun getPINCode(ssid: String, callback: IPinCodeCallback)
}