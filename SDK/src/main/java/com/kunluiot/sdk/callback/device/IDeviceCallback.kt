package com.kunluiot.sdk.callback.device

import com.kunluiot.sdk.bean.device.DeviceListBean

/**
 * 设备列表
 * */
interface IDeviceListCallback {
    fun onSuccess(bean: List<DeviceListBean>)
    fun onError(code: String, error: String)
}