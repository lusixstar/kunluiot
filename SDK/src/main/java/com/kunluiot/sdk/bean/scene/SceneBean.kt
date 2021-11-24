package com.kunluiot.sdk.bean.scene

import kotlinx.serialization.Serializable

@Serializable
data class SceneListBean(
    val objectId: SceneObjectIdBean = SceneObjectIdBean(),
    val sceneId: String = "",
    val sceneName: String = "",
    val uid: String = "",
    val pid: String = "",
    val desc: String = "",
    val showType: String = "",
    val sceneSort: Int = 0,
    val createTime: Long = 0,
    val updateTime: String = "",
    val templateId: String = "",
    val name: String = "",
    val sceneTaskList: List<SceneAddOneKeyBean> = listOf(),
    val conditionList: List<SceneAddOneKeyBean> = listOf(),
    val iftttTasks: List<SceneAddOneKeyBean> = listOf(),
    val icon: String = "",
    val oneKeyType: Int = 0,
    val triggerDevTid: String = "",
    val triggerCtrlKey: String = "",
    val taskDevTid: String = "",
    val taskCtrlKey: String = "",
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