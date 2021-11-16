package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.callback.device.INewDeviceCallback
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

    /**
     * 获取新配上的设备列表
     */
    override fun getNewDeviceList(ssid: String, pinCode: String, callback: INewDeviceCallback) {
        DeviceRequestUtil.getNewDeviceList(ssid, pinCode, callback)
    }

    /**
     * 获取网关
     */
    override fun getGateway(quickOperation: Boolean, type: String, callback: INewDeviceCallback) {
        DeviceRequestUtil.getGateway(quickOperation, type, callback)
    }

    /**
     * 设备配网
     */
    override fun deviceControl(overtime: Int, mid: String, devTid: String, ctrlKey: String, callback: IResultCallback) {
        DeviceRequestUtil.deviceControl(overtime, mid, devTid, ctrlKey, callback)
    }
}