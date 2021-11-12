package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.request.DeviceRequestUtil

internal class KunLuDeviceImpl : IKunLuDevice {

    /**
     * 设备列表
     */
    override fun list(callback: IDeviceListCallback) {
        DeviceRequestUtil.list(callback)
    }
}