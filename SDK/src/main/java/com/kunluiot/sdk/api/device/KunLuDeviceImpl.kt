package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.callback.device.IPinCodeCallback
import com.kunluiot.sdk.request.DeviceRequestUtil

internal class KunLuDeviceImpl : IKunLuDevice {

    /**
     * 设备列表
     */
    override fun list(callback: IDeviceListCallback) {
        DeviceRequestUtil.list(callback)
    }

    /**
     * 获取pinCode
     */
    override fun getPINCode(ssid: String, callback: IPinCodeCallback) {
        DeviceRequestUtil.getPINCode(ssid, callback)
    }
}