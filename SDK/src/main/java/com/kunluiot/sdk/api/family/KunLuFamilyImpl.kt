package com.kunluiot.sdk.api.family

import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.family.*
import com.kunluiot.sdk.request.FamilyRequestUtil

internal class KunLuFamilyImpl : IKunLuFamily {

    /**
     * 家庭列表
     */
    override fun getFamilyList(fail: OnFailResult, success: FamilyListResult) {
        FamilyRequestUtil.getFamilyList(fail, success)
    }

    /**
     * 房间列表并且返回房间下的所有设备
     */
    override fun getRoomsDevice(familyId: String, filterFlag: Boolean, page: Int, size: Int, fail: OnFailResult, success: RoomListResult) {
        FamilyRequestUtil.getRoomsDevice(familyId, filterFlag, page, size, fail, success)
    }


    /**
     * 删除家庭
     */
    override fun delete(familyId: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.delete(familyId, fail, success)
    }

    /**
     * 添加家庭
     */
    override fun addFamily(name: String, area: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.addFamily(name, area, fail, success)
    }

    /**
     * 家庭详情
     */
    override fun getFamilyDetails(familyId: String, fail: OnFailResult, success: FamilyOneResult) {
        FamilyRequestUtil.getFamilyDetails(familyId, fail, success)
    }

    /**
     * 更新家庭信息
     */
    override fun update(familyId: String, name: String, city: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.update(familyId, name, city, fail, success)
    }

    /**
     * 添加房间
     */
    override fun addRooms(familyId: String, folderName: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.addRooms(familyId, folderName, fail, success)
    }

    /**
     * 房间列表
     */
    override fun getRooms(familyId: String, page: Int, size: Int, fail: OnFailResult, success: RoomListResult) {
        FamilyRequestUtil.getRooms(familyId, page, size, fail, success)
    }

    /**
     * 删除房间
     */
    override fun deleteRoom(folderId: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.deleteRoom(folderId, fail, success)
    }

    /**
     * 修改房间信息
     */
    override fun updateRoomInfo(folderId: String, folderName: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.updateRoomInfo(folderId, folderName, fail, success)
    }

    /**
     * 移动房间设备
     */
    override fun moveRoomDevice(folderId: String, devTid: String, ctrlKey: String, subDevTid: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.moveRoomDevice(folderId, devTid, ctrlKey, subDevTid, fail, success)
    }

    /**
     * 添加家庭成员
     */
    override fun addFamilyMember(familyId: String, phoneNumber: String, name: String, gender: String, type: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.addFamilyMember(familyId, phoneNumber, name, gender, type, fail, success)
    }

    /**
     * 删除家庭成员
     */
    override fun deleteFamilyMember(familyId: String, uid: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.deleteFamilyMember(familyId, uid, fail, success)
    }

    /**
     * 修改家庭成员名称和头像
     */
    override fun updateFamilyMemberInfo(familyId: String, name: String, gender: String, uid: String, fail: OnFailResult, success: OnSuccessResult) {
        FamilyRequestUtil.updateFamilyMemberInfo(familyId, name, gender, uid, fail, success)
    }


    //-----------------------------------------------------


    /**
     * 家庭成员设备授权
     */
    override fun updateMemberCtrlKeys(familyId: String, uid: String, ctrlKeys: List<String>, callback: IResultCallback) {
        FamilyRequestUtil.updateMemberCtrlKeys(familyId, uid, ctrlKeys, callback)
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