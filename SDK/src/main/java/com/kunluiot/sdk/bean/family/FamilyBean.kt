package com.kunluiot.sdk.bean.family

/**
 * 创建家庭
 * */
data class FamilyCreateBean(
    val contact: String = "",
    val detailAddress: String = "",
    val deviceNum: Int = 0,
    val familyId: String = "",
    val familyName: String = "",
    val familySort: Int = 0,
    val folderList: List<Folder> = listOf(),
    val name: String = "",
    val roomNum: Int = 0,
    val uid: String = "",
    var current: Boolean = false,
)

data class Folder(
    val defaultFolder: Boolean = false,
    val familyId: String = "",
    val folderId: String = "",
    val folderName: String = "",
    val folderSort: Int = 0,
)