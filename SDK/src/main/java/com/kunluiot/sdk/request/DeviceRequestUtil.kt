package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.IResultStringCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.device.*
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.KotlinSerializationUtils
import java.util.*

object DeviceRequestUtil {

    /**
     * 设备操作列表
     */
    fun getDeviceOperationList(ppk: String, fail: OnFailResult, success: DeviceProtocolResult) {
        val kalle = Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_DEVICE_OPERATION_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("ppk", ppk)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<DeviceOperationBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 扫码添加设备
     */
    fun scanCodeDevice(bindKey: String, devTid: String, fail: OnFailResult, success: DeviceOneResult) {
        val map = mutableMapOf<String, String>()
        map["bindKey"] = bindKey
        map["devTid"] = devTid
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<DeviceNewBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 删除设备
     */
    fun deleteDevice(delDevTid: String, bindKey: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$delDevTid")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.urlParam("bindKey", bindKey)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 删除子设备
     */
    fun deletesSubDevice(devTid: String, ctrlKey: String, subDevTid: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DELETE_SUB_DEVICE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.urlParam("devTid", devTid).urlParam("ctrlKey", ctrlKey).urlParam("subDevTid", subDevTid)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 设备配网
     */
    fun deviceControl(overtime: Int, mid: String, devTid: String, ctrlKey: String, fail: OnFailResult, success: DeviceConfigGatewayResult) {
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
        val kalle = Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE_CONTROL)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<DeviceConfigGateWayBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 设备产品列表
     */
    fun getDeviceProducts(fail: OnFailResult, success: DeviceProductListResult) {
        val kalle = Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_GET_PRODUCTLIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("filterFlag", true)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<List<DeviceListProductBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 房间中设备列表
     * */
    fun getRoomsDevices(folderId: String, quickOperation: Boolean, fail: OnFailResult, success: DeviceListResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICES)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("quickOperation", quickOperation).param("folderId", folderId)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<List<DeviceNewBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 删除授权设备
     */
    fun deleteAuthorizationDevice(grantor: String, ctrlKey: String, grantee: String, devTid: String, randomToken: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DELETE_AUTHORIZATION_DEVICE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.urlParam("grantor", grantor).urlParam("ctrlKey", ctrlKey).urlParam("grantee", grantee).urlParam("devTid", devTid)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 检查设备固件是否需要升级
     */
    fun checkDeviceIsUpdate(binVer: String, binType: String, binVersion: String, productPublicKey: String, devTid: String, ctrlKey: String, fail: OnFailResult, success: DeviceUpdateResult) {
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
        val kalle = Kalle.post(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_CHECK_DEVICES_UPDATE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<List<DeviceUpdateBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }


    /**
     *  检查协调器版本
     */
    fun checkZigVer(zigOtaBinVer: String, productPublicKey: String, fail: OnFailResult, success: DeviceUpdateResult) {
        val map = mutableMapOf<String, String>()
        map["zigOtaBinVer"] = zigOtaBinVer
        map["productPublicKey"] = productPublicKey
        val maps: MutableList<MutableMap<String, String>> = ArrayList()
        maps.add(map)
        val param = JsonUtils.toJson(maps)
        val kalle = Kalle.post(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_CHECK_ZIG_VER)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<List<DeviceUpdateBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     *  修改设备名称
     */
    fun editDeviceName(deviceName: String, ctrlKey: String, devTid: String, fail: OnFailResult, success: OnSuccessResult) {
        val map = mutableMapOf<String, String>()
        map["deviceName"] = deviceName
        map["ctrlKey"] = ctrlKey
        val param = JsonUtils.toJson(map)
        val kalle = Kalle.patch(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$devTid")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(param))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    //------------------------------------------------------------


    /**
     * 所有设备
     * */
    fun getAllDevicesAct(quickOperation: Boolean, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_GET_PRODUCTLIST).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("quickOperation", quickOperation).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
     * 获取网关
     */
    fun getGateway(fail: OnFailResult,  success: DeviceListResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICES)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<List<DeviceNewBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 获取子设备信息
     */
    fun getSubDevice(ctrlKey: String, subDevTid: String, type: String, quickOperation: Boolean, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICES).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("ctrlKey", ctrlKey).param("subDevTid", subDevTid).param("type", type).param("quickOperation", quickOperation).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
     * 设备配网成功后将设备配置到某个家庭下某个房间
     */
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
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$devTid").addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(param)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
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
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$devTid/$subDevTid").addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(param)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }

    /**
     * 设备列表
     */
    fun getPINCode(ssid: String, fail: OnFailResult, success: DevicePinCodeResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GET_PIN_CODE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("ssid", ssid)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<DevicePinCodeBean>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 获取新配上的设备列表
     */
    fun getNewDeviceList(ssid: String, pinCode: String, callback: IDeviceListCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GET_NEW_DEVICE_LIST).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("ssid", ssid).param("pinCode", pinCode).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
     * 产品说明子页面列表
     */
    fun getProductDescribe(category: String, callback: IDeviceProductDescribeCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_PRODUCT_DESCRIBE).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("category", category).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<List<DeviceProductDescribeBean>>(response.succeed()).let { callback.onSuccess(it) }
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
        Kalle.post(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_DEVICE + "/$ctrlKey/wifi").setHeaders(KunLuHelper.getSign()).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(param)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }

    /**
     * 设备操作模板
     */
    fun getDeviceProtocolTemplate(ppk: String, callback: IResultStringCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + DeviceApi.KHA_API_DEVICE_PROTOCOL_LIST).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).addHeader("X-Hekr-ProdPubKey", ppk).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<Map<String, Any>>(response.succeed()).let { callback.onSuccess(JsonUtils.toJson(it)) }
                }
            }
        })
    }


    /**
     * 获取群控
     */
    fun getGroupsAct(callback: IResultCallback) {
        Kalle.get(ReqApi.KHA_WEB_BASE_URL + DeviceApi.KHA_API_GROUP_ACT).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }
}