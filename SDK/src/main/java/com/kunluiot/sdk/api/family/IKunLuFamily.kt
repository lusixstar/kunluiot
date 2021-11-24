package com.kunluiot.sdk.api.family

import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.family.*

interface IKunLuFamily {

    /**
     * 获取家庭列表
     */
    fun getFamilyList(fail: OnFailResult, success: FamilyListResult)

    /**
     * 房间列表并且返回房间下的所有设备
     */
    fun getRoomsDevice(familyId: String, filterFlag: Boolean, page: Int, size: Int, fail: OnFailResult, success: RoomListResult)





    //-----------------------------------------------------

    /**
     * 获取家庭详情
     */
    fun getFamilyDetails(familyId: String, callback: IFamilyDetailsCallback)

    /**
     * 删除家庭
     */
    fun delete(familyId: String, callback: IResultCallback)

    /**
     * 更新家庭信息
     */
    fun update(familyId: String, name: String, city: String, callback: IResultCallback)

    /**
     * 添加家庭
     */
    fun addFamily(name: String, area: String, callback: ICreateFamilyCallback)

    /**
     * 添加家庭成员
     */
    fun addFamilyMember(familyId: String, phoneNumber: String, name: String, gender: String, type: String, callback: IResultCallback)

    /**
     * 删除家庭成员
     */
    fun deleteFamilyMember(familyId: String, uid: String, callback: IResultCallback)

    /**
     * 修改家庭成员名称和头像
     */
    fun updateFamilyMemberInfo(familyId: String, name: String, gender: String, uid: String, callback: IResultCallback)

    /**
     * 家庭成员设备授权
     */
    fun updateMemberCtrlKeys(familyId: String, uid: String, ctrlKeys: List<String>, callback: IResultCallback)

    /**
     * 房间列表
     */
    fun getRooms(familyId: String, page: Int, size: Int, callback: IFamilyRoomListCallback)

    /**
     * 添加房间
     */
    fun addRooms(familyId: String, folderName: String, callback: IResultCallback)

    /**
     * 修改房间信息
     */
    fun updateRoomInfo(folderId: String, folderName: String, callback: IResultCallback)

    /**
     * 删除房间
     */
    fun deleteRoom(folderId: String, callback: IResultCallback)

    /**
     * 移动房间设备
     */
    fun moveRoomDevice(folderId: String, devTid: String, ctrlKey: String, subDevTid: String, callback: IResultCallback)

    /**
     * 房间排序
     */
    fun sortRoom(sortFolders: List<FolderBean>, callback: IFamilyRoomSortCallback)

    /**
     * 获取设备上报下发帧
     */
    fun getDeviceFrame(familyId: String, callback: IFamilyRoomDeviceFrameCallback)

}