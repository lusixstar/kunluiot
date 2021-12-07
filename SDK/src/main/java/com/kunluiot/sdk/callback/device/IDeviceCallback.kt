package com.kunluiot.sdk.callback.device

import com.kunluiot.sdk.bean.device.*

/**
 * 设备网关配网
 * */
fun interface DeviceConfigGatewayResult {
    fun success(info: DeviceConfigGateWayBean)
}

/**
 * 设备协议回调
 * */
fun interface DeviceProtocolResult {
    fun success(info: DeviceOperationBean)
}

/**
 * 单个设备信息
 * */
fun interface DeviceOneResult {
    fun success(info: DeviceNewBean)
}

/**
 * 设备列表信息
 * */
fun interface DeviceListResult {
    fun success(info: List<DeviceNewBean>)
}

/**
 * 设备产品列表信息
 * */
fun interface DeviceProductListResult {
    fun success(info: List<DeviceListProductBean>)
}

/**
 * 设备升级
 */
fun interface DeviceUpdateResult {
    fun success(info: List<DeviceUpdateBean>)
}

// ------------------------------------------

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
 * 产品说明子页面
 * */
interface IDeviceProductDescribeCallback {
    fun onSuccess(bean: List<DeviceProductDescribeBean>)
    fun onError(code: String, error: String)
}


