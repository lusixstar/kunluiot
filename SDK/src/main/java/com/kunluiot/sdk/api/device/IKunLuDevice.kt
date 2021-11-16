package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.callback.device.INewDeviceCallback
import com.kunluiot.sdk.callback.device.IOneDeviceCallback
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

    /**
     * 获取新配上的设备列表
     */
    fun getNewDeviceList(ssid: String, pinCode: String, callback: INewDeviceCallback)

    /**
     * 获取网关
     */
    fun getGateway(quickOperation: Boolean, type: String, callback: INewDeviceCallback)

    /**
     * 设备配网
     */
    fun deviceControl(overtime: Int, mid: String, devTid: String, ctrlKey: String, callback: IResultCallback)

    /**
     *  获取子设备信息
     */
    fun getSubDevice(ctrlKey: String, subDevTid: String, type: String, quickOperation: Boolean, callback: INewDeviceCallback)

    /**
     *  扫码添加设备
     */
    fun scanCodeDevice(bindKey: String, devTid: String, callback: IOneDeviceCallback)
}