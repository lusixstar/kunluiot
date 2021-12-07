package com.kunluiot.sdk.api.device

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.IResultStringCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.device.*
import com.kunluiot.sdk.request.DeviceRequestUtil

internal class KunLuDeviceImpl : IKunLuDevice {

    /**
     * 设备操作列表
     */
    override fun getDeviceOperationList(ppk: String, fail: OnFailResult, success: DeviceProtocolResult) {
        DeviceRequestUtil.getDeviceOperationList(ppk, fail, success)
    }

    /**
     * 删除设备
     */
    override fun deleteDevice(delDevTid: String, bindKey: String, randomToken: String, bluetooth: Boolean, fail: OnFailResult, success: OnSuccessResult) {
        DeviceRequestUtil.deleteDevice(delDevTid, bindKey, randomToken, bluetooth, fail, success)
    }

    /**
     * 删除子设备
     */
    override fun deletesSubDevice(devTid: String, ctrlKey: String, subDevTid: String, fail: OnFailResult, success: OnSuccessResult) {
        DeviceRequestUtil.deletesSubDevice(devTid, ctrlKey, subDevTid, fail, success)
    }

    /**
     * 设备配网
     */
    override fun deviceControl(overtime: Int, mid: String, devTid: String, ctrlKey: String, fail: OnFailResult, success: DeviceConfigGatewayResult) {
        DeviceRequestUtil.deviceControl(overtime, mid, devTid, ctrlKey, fail, success)
    }

    /**
     *  扫码添加设备
     */
    override fun scanCodeDevice(bindKey: String, devTid: String, fail: OnFailResult, success: DeviceOneResult) {
        DeviceRequestUtil.scanCodeDevice(bindKey, devTid, fail, success)
    }

    /**
     * 房间中设备列表
     * */
    override fun getRoomsDevices(folderId: String, quickOperation: Boolean, fail: OnFailResult, success: DeviceListResult) {
        DeviceRequestUtil.getRoomsDevices(folderId, quickOperation, fail, success)
    }

    /**
     * 删除授权设备
     */
    override fun deleteAuthorizationDevice(grantor: String, ctrlKey: String, grantee: String, devTid: String, randomToken: String, fail: OnFailResult, success: OnSuccessResult) {
        DeviceRequestUtil.deleteAuthorizationDevice(grantor, ctrlKey, grantee, devTid, randomToken, fail, success)
    }

    /**
     * 检查设备固件是否需要升级
     */
    override fun checkDeviceIsUpdate(binVer: String, binType: String, binVersion: String, productPublicKey: String, devTid: String, ctrlKey: String, fail: OnFailResult, success: DeviceUpdateResult) {
        DeviceRequestUtil.checkDeviceIsUpdate(binVer, binType, binVersion, productPublicKey, devTid, ctrlKey, fail, success)
    }


    //-----------------------------------------------

    /**
     * 所有设备
     * */
    override fun getAllDevicesAct(quickOperation: Boolean, callback: IDeviceListCallback) {
        DeviceRequestUtil.getAllDevicesAct(quickOperation, callback)
    }


    /**
     * 获取网关
     */
    override fun getGateway(quickOperation: Boolean, type: String, callback: IDeviceListCallback) {
        DeviceRequestUtil.getGateway(quickOperation, type, callback)
    }

    /**
     *  获取子设备信息
     */
    override fun getSubDevice(ctrlKey: String, subDevTid: String, type: String, quickOperation: Boolean, callback: IDeviceListCallback) {
        DeviceRequestUtil.getSubDevice(ctrlKey, subDevTid, type, quickOperation, callback)
    }


    /**
     * 设备配网成功后将设备配置到某个家庭下某个房间
     * */
    override fun deviceConfigFinish(devTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, branchNames: List<String>, anotherNames: List<Map<String, Any>>, callback: IResultCallback) {
        DeviceRequestUtil.deviceConfigFinish(devTid, ctrlKey, deviceName, familyId, folderId, branchNames, anotherNames, callback)
    }

    /**
     * 子设备配网成功后将设备配置到某个家庭下某个房间
     * */
    override fun subDeviceConfigFinish(devTid: String, subDevTid: String, ctrlKey: String, deviceName: String, familyId: String, folderId: String, branchNames: List<String>, anotherNames: List<Map<String, Any>>, callback: IResultCallback) {
        DeviceRequestUtil.subDeviceConfigFinish(devTid, subDevTid, ctrlKey, deviceName, familyId, folderId, branchNames, anotherNames, callback)
    }

    /**
     * 获取pinCode
     */
    override fun getPINCode(ssid: String, callback: IPinCodeCallback) {
        DeviceRequestUtil.getPINCode(ssid, callback)
    }

    /**
     * 获取新配上的设备列表
     */
    override fun getNewDeviceList(ssid: String, pinCode: String, callback: IDeviceListCallback) {
        DeviceRequestUtil.getNewDeviceList(ssid, pinCode, callback)
    }

    /**
     * 设备产品列表
     */
    override fun getDeviceProducts(filterFlag: Boolean, fail: OnFailResult, success: DeviceProductListResult) {
        DeviceRequestUtil.getDeviceProducts(filterFlag, fail, success)
    }

    /**
     * 产品说明子页面列表
     */
    override fun getProductDescribe(category: String, callback: IDeviceProductDescribeCallback) {
        DeviceRequestUtil.getProductDescribe(category, callback)
    }


    /**
     * 设备详情-更换WiFi
     */
    override fun switchDeviceWifi(ctrlKey: String, ssid: String, password: String, callback: IResultCallback) {
        DeviceRequestUtil.switchDeviceWifi(ctrlKey, ssid, password, callback)
    }

    /**
     * 设备操作模板
     */
    override fun getDeviceProtocolTemplate(ppk: String, callback: IResultStringCallback) {
        DeviceRequestUtil.getDeviceProtocolTemplate(ppk, callback)
    }


    /**
     * 获取群控
     */
    override fun getGroupsAct(callback: IResultCallback) {
        DeviceRequestUtil.getGroupsAct(callback)
    }
}