package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.device.DeviceListBean
import com.kunluiot.sdk.bean.family.FamilyCreateBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import com.kunluiot.sdk.callback.family.IFamilyListCallback
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils

object DeviceRequestUtil {

    /**
     * 设备列表
     */
    fun list(callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_GET_PRODUCTLIST)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceListBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceListBean>>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess(data.data)
                        }
                    }
                }
            })
    }
}