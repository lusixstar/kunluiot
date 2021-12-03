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
    var cmdArgs: Map<String, Long>? = null,
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
    var subDevTid: String? = null,
    var relation: String? = null,
    var customFields: SceneOneKeyCustomParam? = null,
    var triggerParams: List<SceneTriggerParam>? = null,
) : Parcelable


// -----------------------------------------------

data class SceneStackLinkedBean(
    var conditionList: List<SceneStackLinkedCondition>? = null,
    var conditionLogic: String? = null,
    var cronExpr: String? = null,
    var desc: String? = null,
    var enabled: Boolean? = null,
    var iftttTasks: List<SceneStackLinkedIftttTask>? = null,
    var iftttType: String? = null,
    var ruleName: String? = null,
    var timeZoneOffset: Int? = null,
    var triggerType: String? = null,
)

data class SceneStackLinkedCondition(
    var conDesc: String? = null,
    var ctrlKey: String? = null,
    var customFields: SceneStackLinkedCustomFields? = null,
    var devTid: String? = null,
    var subDevTid: String? = null,
    var relation: String? = null,
    var triggerParams: List<SceneStackLinkedTriggerParam>? = null,
)

data class SceneStackLinkedIftttTask(
    var customParam: SceneStackLinkedCustomParam? = null,
    var desc: String? = null,
    var type: String? = null,
    var params: SceneStackLinkedParams? = null,
)

data class SceneStackLinkedCustomFields(
    var family_folder: String? = null,
    var icon: String? = null,
    var mid: String? = null,
    var name: String? = null,
)

data class SceneStackLinkedTriggerParam(
    var left: String? = null,
    var `operator`: String? = null,
    var right: String? = null,
)

data class SceneStackLinkedCustomParam(
    var devName: String? = null,
    var family_folder: String? = null,
    var icon: String? = null,
    var mid: String? = null,
    var name: String? = null,
    var sceneId: String? = null,
)

data class SceneStackLinkedParams(
    var ctrlKey: String? = null,
    var subDevTid: String? = null,
    var `data`: Map<String, Any>? = null,
//    var `data`: SceneStackLinkedData? = null,
    var devTid: String? = null,
    var sceneId: String? = null,
    var time: Int? = null,
)

//data class SceneStackLinkedData(
//    var cmdId: Int? = null,
//    var temp_value: Int? = null,
//)


