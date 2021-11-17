package com.kunluiot.sdk.callback.device

import com.kunluiot.sdk.bean.device.*

/**
 * 设备列表
 * */
interface IDeviceListCallback {
    fun onSuccess(bean: List<DeviceNewBean>)
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
 * 设备产品列表
 * */
interface IDeviceListProductCallback {
    fun onSuccess(bean: List<DeviceListProductBean>)
    fun onError(code: String, error: String)
}

/**
 * 产品说明子页面
 * */
interface IDeviceProductDescribeCallback {
    fun onSuccess(bean: List<DeviceProductDescribeBean>)
    fun onError(code: String, error: String)
}

/**
 * 设备配网
 * */
interface IConfigNetworkCallback {
    fun onSuccess(bean: List<ConfigNetworkBean>)
    fun onError(code: String, error: String)
}




///**
// * 获取单个设备
// * */
//interface IOneDeviceCallback {
//    fun onSuccess(bean: DeviceNewBean)
//    fun onError(code: String, error: String)
//}

