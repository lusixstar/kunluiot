package com.kunluiot.sdk.bean.scene

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SceneLinkBeanNew(
    val uid: String = "",
    val pid: String = "",
    val ruleId: String = "",
    var ruleName: String = "",
    val ruleSort: Int = 0,
    var iftttType: String = "",
    var triggerType: String = "",
    var conditionLogic: String = "",
    var desc: String = "",
    val enabled: Boolean = false,
    var timeZoneOffset: Int = 0,
    var cronExpr: String = "",
    val intervalTime: Int = 0,
    var conditionList: List<SceneConditionListBeanNew> = listOf(),
    var iftttTasks: List<SceneIftttTasksListBeanNew> = listOf(),
) : Parcelable

@Parcelize
@Serializable
data class SceneIftttTasksListBeanNew(
    var type: String = "",
    var desc: String = "",
    var taskId: String = "",
    var params: SceneIftttTasksParamBeanNew = SceneIftttTasksParamBeanNew(),
    var customParam: SceneCustomFieldsBeanNew = SceneCustomFieldsBeanNew(),
) : Parcelable

@Parcelize
@Serializable
data class SceneConditionListBeanNew(
    var devTid: String = "",
    var ctrlKey: String = "",
    var subDevTid: String = "",
    var conDesc: String = "",
    var relation: String = "",
    var triggerParams: List<SceneTriggerBeanNew> = listOf(),
    var customFields: SceneCustomFieldsBeanNew = SceneCustomFieldsBeanNew(),
) : Parcelable


@Parcelize
@Serializable
data class SceneTriggerBeanNew(
    var left: String = "",
    var right: String = "",
    var `operator`: String = "",
) : Parcelable


@Parcelize
@Serializable
data class SceneCustomFieldsBeanNew(
    var mid: String = "",
    var desc: String = "",
    var name: String = "",
    var icon: String = "",
    var time: String = "",
    var devName: String = "",
    var family_folder: String = "",
) : Parcelable

@Parcelize
@Serializable
data class SceneIftttTasksParamBeanNew(
    var data: Map<String, String> = mapOf(),
    var subDevTid: String = "",
    var devTid: String = "",
    var ctrlKey: String = "",
    var sceneId: String = "",
    var time: String = "",
) : Parcelable

@Parcelize
@Serializable
data class AddTimeConditionEvent(
    var triggerType: String? = null,
    var conDesc: String? = null,
    var devTid: String? = null,
    var ctrlKey: String? = null,
    var cronExpr: String? = null,
    var desc: String? = null,
    var subDevTid: String? = null,
    var triggerParams: List<SceneTriggerBeanNew>? = null,
    var customFields: SceneCustomFieldsBeanNew? = null,
) : Parcelable

// -------------------------------------------------------

@Parcelize
@Serializable
data class SceneOneKeyBean(
    val sceneName: String = "",
    val sceneId: String = "",
    val icon: String = "",
    val pid: String = "",
    val uid: String = "",
    val sceneSort: Int = 0,
    val showType: String = "",
    val desc: String = "",
    val sceneTaskList: List<SceneOneKeyTaskListBean> = listOf(),
    val templateId: String = "",
    var oneKeyType: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class SceneOneKeyTaskListBean(
    var taskId: String = "",
    var desc: String = "",
    var devTid: String = "",
    var ctrlKey: String = "",
    var subDevTid: String = "",
    var iftttId: String = "",
    var enable: String = "",
    var time: String = "",
    var customParam: SceneCustomFieldsBeanNew = SceneCustomFieldsBeanNew(),
    var cmdArgs: Map<String, String> = mapOf(),
) : Parcelable

@Parcelize
@Serializable
data class SceneOneKeyCustomParam(
    var icon: String = "",
    var mid: String = "",
    var name: String = "",
    var time: Int = 0,
    var family_folder: String = "",
    val taskId: String = "",
    val disPlayName: String = "",
    var devName: String = "",
    val desc: String = "",
) : Parcelable

@Parcelize
@Serializable
data class SceneLinkedBean(
    var taskId: String = "",
    var type: String = "",
    var time: Int = 0,
    var iftttId: String = "",
    var enable: String = "",
    var devTid: String = "",
    var ctrlKey: String = "",
    var subDevTid: String = "",
    var thirdPid: String = "",
    var newDesc: String = "",
    var desc: String = "",
    var params: SceneLinkedParamsBean = SceneLinkedParamsBean(),
    var cmdArgs: Map<String, String> = mapOf(),
    var cmdArgsLink: Map<String, String> = mapOf(),
    var customParam: SceneOneKeyCustomParam = SceneOneKeyCustomParam(),
) : Parcelable

@Parcelize
@Serializable
data class SceneLinkedParamsBean(
    var ctrlKey: String = "",
    var subDevTid: String = "",
    var `data`: Map<String, String> = mapOf(),
    var devTid: String = "",
    var sceneId: String = "",
    var time: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class TimeConditionBean(
    var isOn: Boolean = false,
    val time: String = "",
) : Parcelable



@Serializable
data class SceneListBean(
    val sceneId: String = "",
    val sceneName: String = "",
    val uid: String = "",
    val desc: String = "",
    val showType: String = "",
    val templateId: String = "",
    val name: String = "",
)

@Serializable
data class SceneCustomParamBean(
    val name: String = "",
    val icon: String = "",
    val mid: String = "",
    val family_folder: String = "",
    val taskId: String = "",
    val disPlayName: String = "",
    val devName: String = "",
    val desc: String = "",
)

@Serializable
data class SceneDeleteBean(
    val ruleId: String = "",
    val ruleName: String = "",
    val uid: String = "",
    val type: String = "",
)

@Serializable
data class SceneNewPlayBean(
    val state: Int = 0,
    val data: SceneNewPlayItemBean = SceneNewPlayItemBean(),
)

@Serializable
data class SceneNewPlayItemBean(
    val last: Boolean = false,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val first: Boolean = false,
    val numberOfElements: Int = 0,
    val size: Int = 0,
    val number: Int = 0,
    val page: Int = 0,
    val content: List<SceneListBean> = listOf(),
)

@Serializable
data class SceneOneKeySortBean(
    val sceneSort: Int = 0,
    val showType: String = "",
    val sceneId: String = "",
)

@Serializable
data class SceneLinkSortBean(
    val ruleId: String = "",
    val ruleSort: Int = 0,
)