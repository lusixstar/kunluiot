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
    fun deleteDevice(delDevTid: String, bindKey: String, randomToken: String, bluetooth: Boolean, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 删除子设备
     */
    fun deletesSubDevice(devTid: String, ctrlKey: String, subDevTid: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 设备配网
     */
    fun deviceControl(overtime: Int, mid: String, devTid: String, ctrlKey: String, fail: OnFailResult, success: DeviceConfigGatewayResult)

    // ------------------------------------------------

    /**
     * 删除授权设备
     */
    fun deleteAuthorizationDevice(grantor: String, ctrlKey: String, grantee: String, devTid: String, randomToken: String, callback: IDeviceDeleteCallback)


    /**
     * 所有设备
     * */
    fun getAllDevicesAct(quickOperation: Boolean, callback: IDeviceListCallback)


    /**
     * 房间中设备列表
     * */
    fun getRoomsDevices(folderId: String, quickOperation: Boolean, callback: IDeviceListCallback)

    /**
     * 获取网关
     */
    fun getGateway(quickOperation: Boolean, type: String, callback: IDeviceListCallback)

    /**
     *  获取子设备信息
     */
    fun getSubDevice(ctrlKey: String, subDevTid: String, type: String, quickOperation: Boolean, callback: IDeviceListCallback)

    /**
     * 设备配网成功后将设备配置到某个家庭下某个房间
     * */
    fun deviceConfigFinish(devTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, branchNames: List<String>, anotherNames: List<Map<String, Any>>, callback: IResultCallback)

    /**
     * 子设备配网成功后将设备配置到某个家庭下某个房间
     * */
    fun subDeviceConfigFinish(devTid: String, subDevTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, branchNames: List<String>, anotherNames: List<Map<String, Any>>, callback: IResultCallback)

    /**
     * 获取pinCode
     */
    fun getPINCode(ssid: String, callback: IPinCodeCallback)

    /**
     * 获取新配上的设备列表
     */
    fun getNewDeviceList(ssid: String, pinCode: String, callback: IDeviceListCallback)

    /**
     * 设备产品列表
     */
    fun getDeviceProducts(filterFlag: Boolean, callback: IDeviceListProductCallback)

    /**
     * 产品说明子页面列表
     */
    fun getProductDescribe(category: String, callback: IDeviceProductDescribeCallback)

    /**
     *  扫码添加设备
     */
    fun scanCodeDevice(bindKey: String, devTid: String, callback: IDeviceOneCallback)

    /**
     * 检查设备固件是否需要升级
     */
    fun checkDeviceIsUpdate(binVer: String, binType: String, binVersion: String, productPublicKey: String, devTid: String, ctrlKey: String, callback: IDeviceUpdateCallback)

    /**
     * 设备详情-更换WiFi
     */
    fun switchDeviceWifi(ctrlKey: String, ssid: String, password: String, callback: IResultCallback)

    /**
     * 设备操作模板
     */
    fun getDeviceProtocolTemplate(ppk: String, callback: IResultStringCallback)




    /**
     * 获取群控
     */
    fun getGroupsAct(callback: IResultCallback)
}