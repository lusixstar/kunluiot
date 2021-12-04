package com.kunluiot.sdk.bean.scene

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class SceneLinkBean(
    var conditionList: List<SceneConditionListParam> = listOf(),
    var conditionLogic: String = "",
    val createTime: Long = 0,
    var cronExpr: String = "",
    var desc: String = "",
    val enabled: Boolean = false,
    var iftttTasks: List<SceneLinkedBean> = listOf(),
    var iftttType: String = "",
    val intervalTime: Int = 0,
    val objectId: ObjectId = ObjectId(),
    val pid: String = "",
    val pushMsg: PushMsg = PushMsg(),
    val ruleId: String = "",
    var ruleName: String = "",
    val ruleSort: Int = 0,
    var timeZoneOffset: Int = 0,
    var triggerType: String = "",
    val uid: String = "",
    val updateTime: Long = 0,
) : Parcelable

@Parcelize
@Serializable
data class Condition(
    var conDesc: String = "",
    var ctrlKey: String = "",
    var relation: String = "",
    var customFields: CustomFields = CustomFields(),
    var devTid: String = "",
    var thirdDevice: Boolean = false,
    var triggerParams: List<TriggerParam> = listOf(),
) : Parcelable

@Parcelize
@Serializable
data class IftttTask(
    val customParam: CustomParam = CustomParam(),
    val desc: String = "",
    val params: Params = Params(),
    val taskId: String = "",
    val type: String = "",
) : Parcelable

@Parcelize
@Serializable
data class ObjectId(
    val counter: Int = 0,
    val date: Long = 0,
    val machineIdentifier: Int = 0,
    val processIdentifier: Int = 0,
    val time: Long = 0,
    val timeSecond: Int = 0,
    val timestamp: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class PushMsg(
    val alarm: Boolean = false,
    val pushEnable: Boolean = false,
    val pushTemplateId: String = "",
) : Parcelable

@Parcelize
@Serializable
data class CustomFields(
    val devName: String = "",
    val family_folder: String = "",
    val icon: String = "",
    val mid: String = "",
    val name: String = "",
    var desc: String = "",
) : Parcelable

@Parcelize
@Serializable
data class TriggerParam(
    val left: String = "",
    val `operator`: String = "",
    val right: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class CustomParam(
    val devName: String = "",
    val family_folder: String = "",
    val icon: String = "",
    val mid: String = "",
    val name: String = "",
) : Parcelable

@Parcelize
@Serializable
data class Params(
    val ctrlKey: String = "",
    val `data`: Map<String, String> = mapOf(),
//    val `data`: Data = Data(),
    val devTid: String = "",
    val subDevTid: String = "",
    val time: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class Data(
    val cmdId: Int = 0,
    val sw1: Int = 0,
) : Parcelable

// -------------------------------------------------------

@Parcelize
@Serializable
data class SceneOneKeyBean(
    val createTime: Long = 0,
    val id: SceneOneKeyIdBean = SceneOneKeyIdBean(),
    val name: String = "",
    val pid: String = "",
    val uid: String = "",
    val sceneId: String = "",
    var sceneName: String = "",
    var icon: String = "",
    val showType: String = "",
    val sceneTaskList: List<SceneLinkedBean> = listOf(),
    val templateId: String = "",
    val updateTime: Long = 0,
    //0、表示普通 1、表示回家 2、表示离家 3、表示智能厨房
    var oneKeyType: Int = 0,
) : Parcelable

@Parcelize
@Serializable
data class SceneOneKeyIdBean(
    val counter: Int = 0,
    val date: Long = 0,
    val machineIdentifier: Int = 0,
    val processIdentifier: Int = 0,
    val time: Long = 0,
    val timeSecond: Int = 0,
    val timestamp: Int = 0,
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
//    var cmdArgs: SceneOneKeyCmdArgs = SceneOneKeyCmdArgs(),
    var customParam: SceneOneKeyCustomParam = SceneOneKeyCustomParam(),
) : Parcelable

@Parcelize
@Serializable
data class SceneLinkedParamsBean(
    var ctrlKey: String = "",
    var subDevTid: String = "",
    var `data`: Map<String, String> = mapOf(),
//    var `data`: SceneStackLinkedData? = null,
    var devTid: String = "",
    var sceneId: String = "",
    var time: Int = 0,
): Parcelable

@Parcelize
@Serializable
data class TimeConditionBean(
    var isOn: Boolean = false,
    val time: String = "",
) : Parcelable

@Parcelize
@Serializable
data class AddTimeConditionEvent(
    var triggerType: String? = null,
    var desc: String? = null,
    var cronExpr: String? = null,
    var conditionBean: SceneConditionListParam? = null,
    var timeList: List<TimeConditionBean>? = null,
) : Parcelable

// ------------------------------------------


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
data class SceneObjectIdBean(
    val timestamp: Int = 0,
    val machineIdentifier: Int = 0,
    val processIdentifier: Int = 0,
    val counter: Int = 0,
    val time: Long = 0,
    val date: Long = 0,
    val timeSecond: Int = 0,
)

@Serializable
data class SceneAddOneKeyBean(
    val taskId: String = "",
    val cmdArgs: Map<String, Unit> = mapOf(),
    val customParam: SceneCustomParamBean = SceneCustomParamBean(),
    val time: String = "",
    val iftttId: String = "",
    val enable: String = "",
    val devTid: String = "",
    val ctrlKey: String = "",
    val subDevTid: String = "",
    val thirdPid: String = "",
    val newDesc: String = "",
    val fix: String = "0",
    val updateIndex: Int = -1,
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