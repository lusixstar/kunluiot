package com.kunluiot.sdk.callback.device

import com.kunluiot.sdk.bean.device.DeviceListBean
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DevicePinCodeBean

/**
 * 设备列表
 * */
interface IDeviceListCallback {
    fun onSuccess(bean: List<DeviceListBean>)
    fun onError(code: String, error: String)
}

/**
 * 获取pinCode
 * */
interface IPinCodeCallback {
    fun onSuccess(bean: DevicePinCodeBean)
    fun onError(code: String, error: String)
}

/**
 * 获取新设备列表
 * */
interface INewDeviceCallback {
    fun onSuccess(bean: List<DeviceNewBean>)
    fun onError(code: String, error: String)
}