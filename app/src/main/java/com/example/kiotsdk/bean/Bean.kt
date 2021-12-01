package com.example.kiotsdk.bean

import com.kunluiot.sdk.bean.scene.SceneConditionListParam

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
    val name: String = ""
)

