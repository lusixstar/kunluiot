package com.example.kiotsdk.bean

/**
 * User: Chris
 * Date: 2021/11/29
 * Desc:
 */

data class SceneIconBean(
    var select: Boolean = false,
    val id: Int = 0,
)

data class AddTimeConditionBean(
    var select: Boolean = false,
    val id: Int = 0,
    val name: String = "",
)

data class TriggerModeBean(
    var key: String = "",
    var value: String = "",
    var select: Boolean = false,
)

data class FeedbackImgBean(
    var type: String = "",
    var url: String = "",
)