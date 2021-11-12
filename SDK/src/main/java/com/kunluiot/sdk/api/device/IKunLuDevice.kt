package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.device.IDeviceListCallback

interface IKunLuDevice {

    /**
     * 设备列表
     */
    fun list(callback: IDeviceListCallback)
}