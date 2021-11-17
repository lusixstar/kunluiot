package com.kunluiot.sdk.api.family

import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.*
import com.kunluiot.sdk.request.FamilyRequestUtil

internal class KunLuFamilyImpl : IKunLuFamily {

    /**
     * 家庭列表
     */
    override fun getFamilyList(callback: IFamilyListCallback) {
        FamilyRequestUtil.getFamilyList(callback)
    }

    /**
     * 家庭详情
     */
    override fun getFamilyDetails(familyId: String, callback: IFamilyDetailsCallback) {
        FamilyRequestUtil.getFamilyDetails(familyId, callback)
    }

    /**
     * 删除家庭
     */
    override fun delete(familyId: String, callback: IResultCallback) {
        FamilyRequestUtil.delete(familyId, callback)
    }

    /**
     * 更新家庭信息
     */
    override fun update(familyId: String, name: String, city: String, callback: IResultCallback) {
        FamilyRequestUtil.update(familyId, name, city, callback)
    }

    /**
     * 添加家庭
     */
    override fun addFamily(name: String, area: String, callback: ICreateFamilyCallback) {
        FamilyRequestUtil.addFamily(name, area, callback)
    }

    /**
     * 添加家庭成员
     */
    override fun addFamilyMember(familyId: String, phoneNumber: String, name: String, gender: String, type: String, callback: IResultCallback) {
        FamilyRequestUtil.addFamilyMember(familyId, phoneNumber, name, gender, type, callback)
    }

    /**
     * 删除家庭成员
     */
    override fun deleteFamilyMember(familyId: String, uid: String, callback: IResultCallback) {
        FamilyRequestUtil.deleteFamilyMember(familyId, uid, callback)
    }

    /**
     * 修改家庭成员名称和头像
     */
    override fun updateFamilyMemberInfo(familyId: String, name: String, gender: String, uid: String, callback: IResultCallback) {
        FamilyRequestUtil.updateFamilyMemberInfo(familyId, name, gender, uid, callback)
    }

    /**
     * 家庭成员设备授权
     */
    override fun updateMemberCtrlKeys(familyId: String, uid: String, ctrlKeys: List<String>, callback: IResultCallback) {
        FamilyRequestUtil.updateMemberCtrlKeys(familyId, uid, ctrlKeys, callback)
    }

    /**
     * 房间列表
     */
    override fun getRooms(familyId: String, page: Int, size: Int, callback: IFamilyRoomListCallback) {
        FamilyRequestUtil.getRooms(familyId, page, size, callback)
    }

    /**
     * 房间列表并且返回房间下的所有设备
     */
    override fun getRoomsDevice(familyId: String, filterFlag: Boolean, page: Int, size: Int, callback: IFamilyRoomListCallback) {
        FamilyRequestUtil.getRoomsDevice(familyId, filterFlag, page, size, callback)
    }

    /**
     * 添加房间
     */
    override fun addRooms(familyId: String, folderName: String, callback: IResultCallback) {
        FamilyRequestUtil.addRooms(familyId, folderName, callback)
    }

    /**
     * 修改房间信息
     */
    override fun updateRoomInfo(folderId: String, folderName: String, callback: IResultCallback) {
        FamilyRequestUtil.updateRoomInfo(folderId, folderName, callback)
    }

    /**
     * 删除房间
     */
    override fun deleteRoom(folderId: String, callback: IResultCallback) {
        FamilyRequestUtil.deleteRoom(folderId, callback)
    }

    /**
     * 移动房间设备
     */
    override fun moveRoomDevice(folderId: String, devTid: String, ctrlKey: String, subDevTid: String, callback: IResultCallback) {
        FamilyRequestUtil.moveRoomDevice(folderId, devTid, ctrlKey, subDevTid, callback)
    }

    /**
     * 房间排序
     */
    override fun sortRoom(sortFolders: List<FolderBean>, callback: IFamilyRoomSortCallback) {
        FamilyRequestUtil.sortRoom(sortFolders, callback)
    }

    /**
     * 获取设备上报下发帧
     */
    override fun getDeviceFrame(familyId: String, callback: IFamilyRoomDeviceFrameCallback) {
        FamilyRequestUtil.getDeviceFrame(familyId, callback)
    }
}