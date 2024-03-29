package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.IResultStringCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.device.*

interface IKunLuDevice {

    /**
     * 设备操作列表
     */
    fun getDeviceOperationList(ppk: String, fail: OnFailResult, success: DeviceProtocolResult)

    /**
     * 删除设备
     */
    fun deleteDevice(delDevTid: String, bindKey: String, randomToken: String, fail: OnFailResult, success: DeviceDeleteResult)

    /**
     * 删除子设备
     */
    fun deletesSubDevice(devTid: String, ctrlKey: String, subDevTid: String, randomToken: String, fail: OnFailResult, success: DeviceDeleteResult)

    /**
     * 删除授权设备
     */
    fun deleteAuthorizationDevice(grantor: String, ctrlKey: String, grantee: String, devTid: String, randomToken: String, fail: OnFailResult, success: DeviceDeleteResult)


    /**
     * 设备配网
     */
    fun deviceControl(overtime: Int, mid: String, devTid: String, ctrlKey: String, fail: OnFailResult, success: DeviceConfigGatewayResult)

    /**
     *  扫码添加设备
     */
    fun scanCodeDevice(bindKey: String, devTid: String, fail: OnFailResult, success: DeviceOneResult)

    /**
     * 设备产品列表
     */
    fun getDeviceProducts(fail: OnFailResult, success: DeviceProductListResult)

    /**
     * 房间中设备列表
     * */
    fun getRoomsDevices(folderId: String, quickOperation: Boolean, fail: OnFailResult, success: DeviceListResult)

    /**
     * 检查设备固件是否需要升级
     */
    fun checkDeviceIsUpdate(binVer: String, binType: String, binVersion: String, productPublicKey: String, devTid: String, ctrlKey: String, fail: OnFailResult, success: DeviceUpdateResult)

    /**
     * 检查协调器版本
     */
    fun checkZigVer(zigOtaBinVer: String, productPublicKey: String, fail: OnFailResult, success: DeviceUpdateResult)

    /**
     * 修改设备名称
     */
    fun editDeviceName(deviceName: String, ctrlKey: String, devTid: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 修改子设备名称
     */
    fun editSubDeviceName(deviceName: String, devTid: String, ctrlKey: String, devSubTid: String, fail: OnFailResult, success: OnSuccessResult)

    // ------------------------------------------------


    /**
     * 所有设备
     * */
    fun getAllDevicesAct(quickOperation: Boolean, callback: IDeviceListCallback)


    /**
     * 获取网关
     */
    fun getGateway(fail: OnFailResult, success: DeviceListResult)

    /**
     *  获取子设备信息
     */
    fun getSubDevice(ctrlKey: String, subDevTid: String, type: String, quickOperation: Boolean, callback: IDeviceListCallback)

    /**
     * 设备配网成功后将设备配置到某个家庭下某个房间
     * */
    fun deviceConfigFinish(devTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 子设备配网成功后将设备配置到某个家庭下某个房间
     * */
    fun subDeviceConfigFinish(devTid: String, subDevTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 获取pinCode
     */
    fun getPINCode(ssid: String, fail: OnFailResult, success: DevicePinCodeResult)

    /**
     * 获取新配上的设备列表
     */
    fun getNewDeviceList(ssid: String, pinCode: String, callback: IDeviceListCallback)


    /**
     * 产品说明子页面列表
     */
    fun getProductDescribe(category: String, callback: IDeviceProductDescribeCallback)


    /**
     * 设备详情-更换WiFi
     */
    fun switchDeviceWifi(ctrlKey: String, ssid: String, password: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 每隔3秒轮询查询更换WiFi状态
     */
    fun pollingDeviceWifi(ctrlKey: String, fail: OnFailResult, success: DeviceChangeWifiResult)

    /**
     * 设备操作模板
     */
    fun getDeviceProtocolTemplate(ppk: String, callback: IResultStringCallback)


    /**
     * 获取群控
     */
    fun getGroupsAct(callback: IResultCallback)
}