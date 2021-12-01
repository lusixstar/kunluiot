package com.kunluiot.sdk.bean.scene

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * User: Chris
 * Date: 2021/12/1
 * Desc:
 */


@Parcelize
@Serializable
data class SceneStackClassBean(
    var desc: String? = null,
    var time: Int? = null,
    var iftttId: String? = null,
    var enable: String? = null,
    var newDesc: String? = null,
    var devTid: String? = null,
    var subDevTid: String? = null,
    var ctrlKey: String? = null,
    var cmdArgs: Map<String, Int>? = null,
    var customParam: SceneStackCustomParam? = null,
) : Parcelable


@Parcelize
@Serializable
data class SceneStackCustomParam(
    var name: String? = null,
    var icon: String? = null,
    var mid: String? = null,
    var family_folder: String? = null,
    var disPlayName: String? = null,
) : Parcelable

@Parcelize
@Serializable
data class SceneTriggerParam(
    var left: String? = null,
    var right: String? = null,
    var `operator`: String? = null,
) : Parcelable

@Parcelize
@Serializable
data class SceneConditionListParam(
    var devTid: String? = null,
    var ctrlKey: String? = null,
    var conDesc: String? = null,
    var relation: String? = null,
    var customFields: SceneOneKeyCustomParam? = null,
    var triggerParams: List<SceneTriggerParam>? = null,
) : Parcelable