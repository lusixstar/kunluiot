package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBean
import com.kunluiot.sdk.bean.common.CommonThirdPlatformBean
import com.kunluiot.sdk.bean.device.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.IResultStringCallback
import com.kunluiot.sdk.callback.device.*
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.KotlinSerializationUtils
import java.util.*

object DeviceRequestUtil {

    /**
     * 所有设备
     * */
    fun getAllDevicesAct(quickOperation: Boolean, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_GET_PRODUCTLIST)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("quickOperation", quickOperation)
            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceNewBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceNewBean>>, String>) {
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

    /**
     * 房间中设备列表
     * */
    fun getRoomsDevices(folderId: String, quickOperation: Boolean, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_DEVICES)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("quickOperation", quickOperation)
            .param("folderId", folderId)
            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceNewBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceNewBean>>, String>) {
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

    /**
     * 获取网关
     */
//    fun getGateway(quickOperation: Boolean, type: String, callback: IDeviceListCallback) {
//        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICES)
//            .setHeaders(KunLuHelper.getSign())
//            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
//            .param("quickOperation", quickOperation)
//            .param("type", type)
//            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceNewBean>>>(KunLuHomeSdk.instance.getApp()) {
//                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceNewBean>>, String>) {
//                    val failed = response.failed()
//                    if (!failed.isNullOrEmpty()) {
//                        callback.onError(response.code().toString(), failed)
//                    } else {
//                        val data = response.succeed()
//                        if (data.status != 200) {
//                            callback.onError(data.status.toString(), data.message)
//                        } else {
//                            callback.onSuccess(data.data)
//                        }
//                    }
//                }
//            })
//    }

    fun getGateway(quickOperation: Boolean, type: String, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICES)
//            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("quickOperation", quickOperation)
            .param("type", type)
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<List<DeviceNewBean>>(response.succeed()).let { callback.onSuccess(it) }
                    }
                }
            })
    }

    /**
     * 获取子设备信息
     */
    fun getSubDevice(ctrlKey: String, subDevTid: String, type: String, quickOperation: Boolean, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICES)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("ctrlKey", ctrlKey)
            .param("subDevTid", subDevTid)
            .param("type", type)
            .param("quickOperation", quickOperation)
            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceNewBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceNewBean>>, String>) {
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

    /**
     * 设备配网
     */
    fun deviceControl(overtime: Int, mid: String, devTid: String, ctrlKey: String, callback: IConfigNetworkCallback) {
        val deviceList = ArrayList<Map<String, Any>>()
        val mapDevice = HashMap<String, Any>()
        mapDevice["devTid"] = devTid
        mapDevice["ctrlKey"] = ctrlKey
        mapDevice["subDevTid"] = ""
        deviceList.add(mapDevice)

        val mapData = HashMap<String, Any>()
        mapData["cmdId"] = 2
        mapData["subMid"] = mid
        mapData["overtime"] = overtime

        val map = HashMap<String, Any>()
        map["data"] = mapData
        map["deviceList"] = deviceList

        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE_CONTROL)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<List<ConfigNetworkBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<ConfigNetworkBean>>, String>) {
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

    /**
     * 设备配网成功后将设备配置到某个家庭下某个房间
     */
//    fun deviceConfigFinish(devTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, branchNames: List<String>, anotherNames: List<Map<String, Any>>, callback: IResultCallback) {
//        val map = HashMap<String, Any>()
//        map["ctrlKey"] = ctrlKey
//        map["deviceName"] = deviceName
//        map["familyId"] = familyId
//        map["folderId"] = folderId
//        if (!branchNames.isNullOrEmpty()) {
//            map["branchNames"] = branchNames
//            map["anotherNames"] = anotherNames
//        }
//        val param = JsonUtils.toJson(map)
//        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$devTid")
//            .setHeaders(KunLuHelper.getSign())
//            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
//            .body(JsonBody(param))
//            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
//                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
//                    val failed = response.failed()
//                    if (!failed.isNullOrEmpty()) {
//                        callback.onError(response.code().toString(), failed)
//                    } else {
//                        val data = response.succeed()
//                        if (data.status != 200) {
//                            callback.onError(data.status.toString(), data.message)
//                        } else {
//                            callback.onSuccess()
//                        }
//                    }
//                }
//            })
//    }

    fun deviceConfigFinish(devTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, branchNames: List<String>, anotherNames: List<Map<String, Any>>, callback: IResultCallback) {
        val map = HashMap<String, Any>()
        map["ctrlKey"] = ctrlKey
        map["deviceName"] = deviceName
        map["familyId"] = familyId
        map["folderId"] = folderId
        if (!branchNames.isNullOrEmpty()) {
            map["branchNames"] = branchNames
            map["anotherNames"] = anotherNames
        }
        val param = JsonUtils.toJson(map)
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$devTid")
//            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let {
                            callback.onSuccess()
                        }
                    }
                }
            })
    }

    /**
     * 子设备配网成功后将设备配置到某个家庭下某个房间
     */
    fun subDeviceConfigFinish(devTid: String, subDevTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, branchNames: List<String>, anotherNames: List<Map<String, Any>>, callback: IResultCallback) {
        val map = HashMap<String, Any>()
        map["ctrlKey"] = ctrlKey
        map["deviceName"] = deviceName
        map["familyId"] = familyId
        map["folderId"] = folderId
        if (!branchNames.isNullOrEmpty()) {
            map["branchNames"] = branchNames
            map["anotherNames"] = anotherNames
        }
        val param = JsonUtils.toJson(map)
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$devTid/$subDevTid")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess()
                        }
                    }
                }
            })
    }

    /**
     * 设备列表
     */
//    fun getPINCode(ssid: String, callback: IPinCodeCallback) {
//        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GET_PIN_CODE)
//            .setHeaders(KunLuHelper.getSign())
//            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
//            .param("ssid", ssid)
//            .perform(object : KunLuNetCallback<BaseRespBean<DevicePinCodeBean>>(KunLuHomeSdk.instance.getApp()) {
//                override fun onResponse(response: SimpleResponse<BaseRespBean<DevicePinCodeBean>, String>) {
//                    val failed = response.failed()
//                    if (!failed.isNullOrEmpty()) {
//                        callback.onError(response.code().toString(), failed)
//                    } else {
//                        val data = response.succeed()
//                        if (data.status != 200) {
//                            callback.onError(data.status.toString(), data.message)
//                        } else {
//                            callback.onSuccess(data.data)
//                        }
//                    }
//                }
//            })
//    }

    /**
     * 设备列表
     */
    fun getPINCode(ssid: String, callback: IPinCodeCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GET_PIN_CODE)
//            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("ssid", ssid)
            .perform(object : KunLuNetCallback<DevicePinCodeBean>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<DevicePinCodeBean, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()

                            callback.onSuccess(data)

                    }
                }
            })
    }

    /**
     * 获取新配上的设备列表
     */
//    fun getNewDeviceList(ssid: String, pinCode: String, callback: IDeviceListCallback) {
//        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GET_NEW_DEVICE_LIST)
//            .setHeaders(KunLuHelper.getSign())
//            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
//            .param("ssid", ssid)
//            .param("pinCode", pinCode)
//            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceNewBean>>>(KunLuHomeSdk.instance.getApp()) {
//                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceNewBean>>, String>) {
//                    val failed = response.failed()
//                    if (!failed.isNullOrEmpty()) {
//                        callback.onError(response.code().toString(), failed)
//                    } else {
//                        val data = response.succeed()
//                        if (data.status != 200) {
//                            callback.onError(data.status.toString(), data.message)
//                        } else {
//                            callback.onSuccess(data.data)
//                        }
//                    }
//                }
//            })
//    }

    fun getNewDeviceList(ssid: String, pinCode: String, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GET_NEW_DEVICE_LIST)
//            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("ssid", ssid)
            .param("pinCode", pinCode)
            .perform(object : KunLuNetCallback<List<DeviceNewBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<List<DeviceNewBean>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()

                            callback.onSuccess(data)

                    }
                }
            })
    }

    /**
     * 设备产品列表
     */
//    fun getDeviceProducts(filterFlag: Boolean, callback: IDeviceListProductCallback) {
//        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_GET_PRODUCTLIST)
//            .setHeaders(KunLuHelper.getSign())
//            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
//            .param("filterFlag", filterFlag)
//            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceListProductBean>>>(KunLuHomeSdk.instance.getApp()) {
//                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceListProductBean>>, String>) {
//                    val failed = response.failed()
//                    if (!failed.isNullOrEmpty()) {
//                        callback.onError(response.code().toString(), failed)
//                    } else {
//                        val data = response.succeed()
//                        if (data.status != 200) {
//                            callback.onError(data.status.toString(), data.message)
//                        } else {
//                            callback.onSuccess(data.data)
//                        }
//                    }
//                }
//            })
//    }

    /**
     * 设备产品列表
     */
    fun getDeviceProducts(filterFlag: Boolean, callback: IDeviceListProductCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_GET_PRODUCTLIST)
//            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("filterFlag", filterFlag)
            .param("pid", "00000000000")
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<List<DeviceListProductBean>>(response.succeed()).let {
                            callback.onSuccess(it)
                        }
                    }
                }
            })
    }

    /**
     * 产品说明子页面列表
     */
    fun getProductDescribe(category: String, callback: IDeviceProductDescribeCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_PRODUCT_DESCRIBE)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("category", category)
            .perform(object : KunLuNetCallback<BaseRespBean<List<DeviceProductDescribeBean>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<List<DeviceProductDescribeBean>>, String>) {
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

    /**
     * 扫码添加设备
     */
//    fun scanCodeDevice(bindKey: String, devTid: String, callback: IDeviceOneCallback) {
//        val map = mutableMapOf<String, String>()
//        map["bindKey"] = bindKey
//        map["devTid"] = devTid
//        val param = JsonUtils.toJson(map)
//        Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE)
////            .setHeaders(KunLuHelper.getSign())
//            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
//            .body(JsonBody(param))
//            .perform(object : KunLuNetCallback<BaseRespBean<DeviceNewBean>>(KunLuHomeSdk.instance.getApp()) {
//                override fun onResponse(response: SimpleResponse<BaseRespBean<DeviceNewBean>, String>) {
//                    try {
//                        val failed = response.failed()
//                        if (!failed.isNullOrEmpty()) {
//                            callback.onError(response.code().toString(), failed)
//                        } else {
//                            val data = response.succeed()
//                            if (data.status != 200) {
//                                callback.onError(data.status.toString(), data.message)
//                            } else {
//                                callback.onSuccess(data.data)
//                            }
//                        }
//                    } catch (e: Exception) {
//                        LogUtils.e("exception == ${e.message.toString()}")
//                        callback.onError("-1", e.message.toString())
//                    }
//                }
//            })
//    }

    fun scanCodeDevice(bindKey: String, devTid: String, callback: IDeviceOneCallback) {
        val map = mutableMapOf<String, String>()
        map["bindKey"] = bindKey
        map["devTid"] = devTid
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE)
//            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<String, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        KotlinSerializationUtils.getJsonData<DeviceNewBean>(response.succeed()).let {
                            callback.onSuccess(it)
                        }
                    }
                }
            })
    }

    /**
     * 检查设备固件是否需要升级
     */
    fun checkDeviceIsUpdate(binVer: String, binType: String, binVersion: String, productPublicKey: String, devTid: String, ctrlKey: String, callback: IDeviceUpdateCallback) {
        val map = mutableMapOf<String, String>()
        map["binVer"] = binVer
        map["binType"] = binType
        map["binVersion"] = binVersion
        map["productPublicKey"] = productPublicKey
        map["devTid"] = devTid
        map["ctrlKey"] = ctrlKey
        val maps: MutableList<MutableMap<String, String>> = ArrayList()
        maps.add(map)
        val param = JsonUtils.toJson(maps)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<DeviceUpdateBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<DeviceUpdateBean>, String>) {
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

    /**
     * 设备详情-更换WiFi
     */
    fun switchDeviceWifi(ctrlKey: String, ssid: String, password: String, callback: IResultCallback) {
        val map = mutableMapOf<String, String>()
        map["ssid"] = ssid
        map["password"] = password
        val param = JsonUtils.toJson(map)
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$ctrlKey/wifi")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .body(JsonBody(param))
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess()
                        }
                    }
                }
            })
    }

    /**
     * 设备操作列表
     */
    fun getDeviceOperationList(ppk: String, callback: IDeviceOperationCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_DEVICE_OPERATION_LIST)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("ppk", ppk)
            .perform(object : KunLuNetCallback<BaseRespBean<DeviceOperationBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<DeviceOperationBean>, String>) {
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

    /**
     * 设备操作模板
     */
    fun getDeviceProtocolTemplate(ppk: String, callback: IResultStringCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_DEVICE_PROTOCOL_LIST)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .addHeader("X-Hekr-ProdPubKey", ppk)
            .perform(object : KunLuNetCallback<BaseRespBean<Map<String, Any>>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Map<String, Any>>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            val json = JsonUtils.toJson(data.data)
                            callback.onSuccess(json)
                        }
                    }
                }
            })
    }

    /**
     * 删除设备
     */
    fun deleteDevice(delDevTid: String, bindKey: String, randomToken: String, bluetooth: Boolean, callback: IDeviceDeleteCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$delDevTid")
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("bindKey", bindKey)
            .param("randomToken", randomToken)
            .param("bluetooth", bluetooth)
            .perform(object : KunLuNetCallback<BaseRespBean<DeviceDeleteBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<DeviceDeleteBean>, String>) {
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

    /**
     * 删除授权设备
     */
    fun deleteAuthorizationDevice(grantor: String, ctrlKey: String, grantee: String, devTid: String, randomToken: String, callback: IDeviceDeleteCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DELETE_AUTHORIZATION_DEVICE)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("grantor", grantor)
            .param("ctrlKey", ctrlKey)
            .param("grantee", grantee)
            .param("devTid", devTid)
            .param("randomToken", randomToken)
            .perform(object : KunLuNetCallback<BaseRespBean<DeviceDeleteBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<DeviceDeleteBean>, String>) {
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

    /**
     * 删除子设备
     */
    fun deletesSubDevice(devTid: String, ctrlKey: String, subDevTid: String, callback: IDeviceDeleteCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DELETE_SUB_DEVICE)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .param("devTid", devTid)
            .param("ctrlKey", ctrlKey)
            .param("subDevTid", subDevTid)
            .perform(object : KunLuNetCallback<BaseRespBean<DeviceDeleteBean>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<DeviceDeleteBean>, String>) {
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

    /**
     * 获取群控
     */
    fun getGroupsAct(callback: IResultCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GROUP_ACT)
            .setHeaders(KunLuHelper.getSign())
            .addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
            .perform(object : KunLuNetCallback<BaseRespBean<Any>>(KunLuHomeSdk.instance.getApp()) {
                override fun onResponse(response: SimpleResponse<BaseRespBean<Any>, String>) {
                    val failed = response.failed()
                    if (!failed.isNullOrEmpty()) {
                        callback.onError(response.code().toString(), failed)
                    } else {
                        val data = response.succeed()
                        if (data.status != 200) {
                            callback.onError(data.status.toString(), data.message)
                        } else {
                            callback.onSuccess()
                        }
                    }
                }
            })
    }
}