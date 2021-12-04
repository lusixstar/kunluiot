package com.kunluiot.sdk.bean.family

import com.kunluiot.sdk.bean.device.DeviceNewBean
import kotlinx.serialization.Serializable

/**
 * 家庭信息
 * */
@Serializable
data class FamilyBean(
    val contact: String = "",
    val detailAddress: String = "",
    val deviceNum: Int = 0,
    val familyId: String = "",
    val familyName: String = "",
    val familySort: Int = 0,
    val folderList: List<FolderBean> = listOf(),
    val name: String = "",
    val roomNum: Int = 0,
    val uid: String = "",
    var current: Boolean = false,
)



// ------------------------------------

@Serializable
data class FolderBean(
    val defaultFolder: Boolean = false,
    val familyId: String = "",
    val folderId: String = "",
    val folderName: String = "",
    val folderSort: Int = 0,
    val deviceList: List<DeviceNewBean> = listOf(),
)