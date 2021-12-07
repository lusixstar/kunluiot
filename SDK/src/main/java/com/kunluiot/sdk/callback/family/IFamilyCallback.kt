package com.kunluiot.sdk.callback.family

import com.kunluiot.sdk.bean.device.DeviceFrameBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean

/**
 * 家庭列表
 * */
fun interface FamilyListResult {
    fun success(info: List<FamilyBean>)
}

/**
 * 房间列表
 * */
fun interface RoomListResult {
    fun success(info: List<FolderBean>)
}

/**
 * 家庭详情
 * */
fun interface FamilyOneResult {
    fun success(info: FamilyBean)
}

/**
 * 家庭房间排序
 * */
interface IFamilyRoomSortCallback {
    fun onSuccess(bean: List<String>)
    fun onError(code: String, error: String)
}

/**
 * 获取设备上报下发帧
 * */
interface IFamilyRoomDeviceFrameCallback {
    fun onSuccess(bean: List<DeviceFrameBean>)
    fun onError(code: String, error: String)
}