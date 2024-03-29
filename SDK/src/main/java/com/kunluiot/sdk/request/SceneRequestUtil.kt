package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseRespBeanTwo
import com.kunluiot.sdk.bean.device.DeviceDeleteBean
import com.kunluiot.sdk.bean.scene.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.device.DeviceDeleteResult
import com.kunluiot.sdk.callback.scene.SceneLinkedListResult
import com.kunluiot.sdk.callback.scene.SceneListResult
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.KotlinSerializationUtils
import java.util.*

object SceneRequestUtil {


    /**
     * 手动场景列表
     * */
    fun getOneKeySceneList(fail: OnFailResult, success: SceneListResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    if (!response.succeed().startsWith("[") || !response.succeed().endsWith("]")) {
                        KotlinSerializationUtils.getJsonData<BaseRespBeanTwo>(response.succeed()).let {  fail.fail(it.status.toString(), it.message) }
                        return
                    }
                    KotlinSerializationUtils.getJsonData<List<SceneOneKeyBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 获取预设情景面板
     */
    fun getSceneTemplate(fail: OnFailResult, success: SceneListResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_GET_SCENE_TEMPLATE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    if (!response.succeed().startsWith("[") || !response.succeed().endsWith("]")) {
                        KotlinSerializationUtils.getJsonData<BaseRespBeanTwo>(response.succeed()).let {  fail.fail(it.status.toString(), it.message) }
                        return
                    }
                    KotlinSerializationUtils.getJsonData<List<SceneOneKeyBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 手动场景列表
     * */
    fun addOneKeyScene(icon: String, sceneName: String, sceneTaskList: List<SceneOneKeyTaskListBean>, fail: OnFailResult, success: OnSuccessResult) {

        val list = mutableListOf<SceneStackClassBean>()
        sceneTaskList.forEach {
            val bean = SceneStackClassBean()

            val item = SceneStackCustomParam()
            if (it.customParam.name.isNotEmpty()) item.name = it.customParam.name
            if (it.customParam.icon.isNotEmpty()) item.icon = it.customParam.icon
            if (it.customParam.mid.isNotEmpty()) item.mid = it.customParam.mid
            if (it.customParam.devName.isNotEmpty()) item.devName = it.customParam.devName
            if (it.customParam.family_folder.isNotEmpty()) item.family_folder = it.customParam.family_folder
            if (it.customParam.desc.isNotEmpty()) item.desc = it.customParam.desc
            bean.customParam = item

            if (it.time.isNotEmpty()) bean.time = it.time.toInt()
            if (it.desc.isNotEmpty()) bean.desc = it.desc
            if (it.devTid.isNotEmpty()) bean.devTid = it.devTid
            if (it.subDevTid.isNotEmpty()) bean.subDevTid = it.subDevTid
            if (it.ctrlKey.isNotEmpty()) bean.ctrlKey = it.ctrlKey
            if (it.iftttId.isNotEmpty()) bean.iftttId = it.iftttId
            if (it.enable.isNotEmpty()) bean.enable = it.enable
            val map = mutableMapOf<String, Long>()
            it.cmdArgs.forEach { (t, u) ->
                map[t] = u.toLong()
            }
            if (!map.isNullOrEmpty()) bean.cmdArgs = map
            list.add(bean)
        }

        val map = HashMap<String, Any>()
        map["sceneName"] = sceneName
        map["sceneTaskList"] = list
        map["icon"] = icon
        val json: String = JsonUtils.toJson(map)

        val kalle = Kalle.post(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(json))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneOneKeyBean>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 编辑手动场景
     * */
    fun updateOneKeyScene(sceneId: String, icon: String, sceneName: String, sceneTaskList: List<SceneOneKeyTaskListBean>, fail: OnFailResult, success: OnSuccessResult) {

        val list = mutableListOf<SceneStackClassBean>()
        sceneTaskList.forEach {
            val bean = SceneStackClassBean()

            val item = SceneStackCustomParam()
            if (it.customParam.name.isNotEmpty()) item.name = it.customParam.name
            if (it.customParam.icon.isNotEmpty()) item.icon = it.customParam.icon
            if (it.customParam.mid.isNotEmpty()) item.mid = it.customParam.mid
            if (it.customParam.devName.isNotEmpty()) item.devName = it.customParam.devName
            if (it.customParam.family_folder.isNotEmpty()) item.family_folder = it.customParam.family_folder
            if (it.customParam.desc.isNotEmpty()) item.desc = it.customParam.desc
            bean.customParam = item

            if (it.time.isNotEmpty()) bean.time = it.time.toInt()
            if (it.desc.isNotEmpty()) bean.desc = it.desc
            if (it.devTid.isNotEmpty()) bean.devTid = it.devTid
            if (it.subDevTid.isNotEmpty()) bean.subDevTid = it.subDevTid
            if (it.ctrlKey.isNotEmpty()) bean.ctrlKey = it.ctrlKey
            if (it.iftttId.isNotEmpty()) bean.iftttId = it.iftttId
            if (it.enable.isNotEmpty()) bean.enable = it.enable
            val map = mutableMapOf<String, Long>()
            it.cmdArgs.forEach { (t, u) ->
                map[t] = u.toLong()
            }
            if (!map.isNullOrEmpty()) bean.cmdArgs = map
            list.add(bean)
        }

        val map = HashMap<String, Any>()
        map["sceneId"] = sceneId
        map["sceneName"] = sceneName
        map["icon"] = icon
        map["sceneTaskList"] = list

        val json: String = JsonUtils.toJson(map)
        val kalle = Kalle.patch(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST + "/$sceneId")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(json))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneOneKeyBean>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 删除手动场景
     * */
    fun deleteOneKeyScene(sceneId: String, fail: OnFailResult, success: OnSuccessResult) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST + "/$sceneId")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 删除使用中的手动场景
     * */
    fun deleteUseOneKeyScene(sceneId: String, iftttIds: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST + "/$sceneId/delete/use").addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("iftttIds", iftttIds).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }

    /**
     * 获取联动场景列表
     * */
    fun getLinkageSceneList(fail: OnFailResult, success: SceneLinkedListResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_LINKAGE_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("page", 0).param("size", 999)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    if (!response.succeed().startsWith("[") || !response.succeed().endsWith("]")) {
                        KotlinSerializationUtils.getJsonData<BaseRespBeanTwo>(response.succeed()).let {  fail.fail(it.status.toString(), it.message) }
                        return
                    }
                    KotlinSerializationUtils.getJsonData<List<SceneLinkBeanNew>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 新增联动场景
     * */
    fun addLinkageScene(bean: SceneLinkBeanNew, fail: OnFailResult, success: OnSuccessResult) {

        val info = SceneStackLinkedBean()
        info.timeZoneOffset = bean.timeZoneOffset
        info.ruleName = bean.ruleName
        info.iftttType = bean.iftttType
        info.enabled = bean.enabled
        info.conditionLogic = bean.conditionLogic
        info.cronExpr = bean.cronExpr
        info.desc = bean.desc
        info.triggerType = bean.triggerType

        if (!bean.iftttTasks.isNullOrEmpty()) {
            SceneIftttTasksListBeanNew
            val iftttTasks = mutableListOf<SceneStackLinkedIftttTask>()
            bean.iftttTasks.forEach { ifttt ->
                val cp = SceneStackLinkedCustomParam()
                if (ifttt.customParam.name.isNotEmpty()) cp.name = ifttt.customParam.name
                if (ifttt.customParam.icon.isNotEmpty()) cp.icon = ifttt.customParam.icon
                if (ifttt.customParam.mid.isNotEmpty()) cp.mid = ifttt.customParam.mid
                if (ifttt.customParam.devName.isNotEmpty()) cp.devName = ifttt.customParam.devName
                if (ifttt.customParam.family_folder.isNotEmpty()) cp.family_folder = ifttt.customParam.family_folder
                val p = SceneStackLinkedParams()
                if (ifttt.params.time != "") p.time = ifttt.params.time.toInt()
                if (ifttt.params.devTid.isNotEmpty()) p.devTid = ifttt.params.devTid
                if (ifttt.params.ctrlKey.isNotEmpty()) p.ctrlKey = ifttt.params.ctrlKey
                if (ifttt.params.subDevTid.isNotEmpty()) p.subDevTid = ifttt.params.subDevTid
                if (ifttt.params.sceneId.isNotEmpty()) p.sceneId = ifttt.params.sceneId
                if (!ifttt.params.data.isNullOrEmpty()) {
                    val da = mutableMapOf<String, Any>()
                    ifttt.params.data.forEach { (t, u) ->
                        if (u.matches(Regex("^[0-9]$"))) {
                            da[t] = u.toLong()
                        } else {
                            da[t] = u
                        }
                    }
                    p.data = da
                }
                val b = SceneStackLinkedIftttTask()
                b.desc = ifttt.desc
                b.customParam = cp
                b.params = p
                b.type = ifttt.type
                iftttTasks.add(b)
            }
            info.iftttTasks = iftttTasks
        }

        if (!bean.conditionList.isNullOrEmpty()) {
            val conditionList = mutableListOf<SceneStackLinkedCondition>()
            bean.conditionList.forEach { cond ->
                val sct = SceneStackLinkedCondition()
                if (cond.devTid.isNotEmpty()) sct.devTid = cond.devTid
                if (cond.ctrlKey.isNotEmpty()) sct.ctrlKey = cond.ctrlKey
                if (cond.subDevTid.isNotEmpty()) sct.subDevTid = cond.subDevTid
                if (cond.conDesc.isNotEmpty()) sct.conDesc = cond.conDesc
                if (cond.relation.isNotEmpty()) sct.relation = cond.relation
                val cf = SceneStackLinkedCustomFields()
                cond.customFields.let { condcf ->
                    if (condcf.name.isNotEmpty()) cf.name = condcf.name
                    if (condcf.mid.isNotEmpty()) cf.mid = condcf.mid
                    if (condcf.icon.isNotEmpty()) cf.icon = condcf.icon
                    if (condcf.family_folder.isNotEmpty()) cf.family_folder = condcf.family_folder
                }
                sct.customFields = cf
                if (!cond.triggerParams.isNullOrEmpty()) {
                    val stpList = mutableListOf<SceneStackLinkedTriggerParam>()
                    cond.triggerParams.forEach { condstp ->
                        val stp = SceneStackLinkedTriggerParam()
                        condstp.left.let { stp.left = condstp.left }
                        condstp.right.let { stp.right = condstp.right }
                        condstp.operator.let { stp.operator = condstp.operator }
                        stpList.add(stp)
                    }
                    sct.triggerParams = stpList
                }
                conditionList.add(sct)
            }
            info.conditionList = conditionList
        }

        val json: String = JsonUtils.toJson(info)

        val kalle = Kalle.post(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_LINKAGE_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(json))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneLinkBeanNew>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 编辑联动场景
     * */
    fun updateLinkageScene(ruleId: String, enable: Boolean, bean: SceneLinkBeanNew, fail: OnFailResult, success: OnSuccessResult) {

        val info = SceneStackLinkedBean()
        info.timeZoneOffset = bean.timeZoneOffset
        info.ruleName = bean.ruleName
        info.iftttType = bean.iftttType
        info.enabled = bean.enabled
        info.conditionLogic = bean.conditionLogic
        info.cronExpr = bean.cronExpr
        info.desc = bean.desc
        info.triggerType = bean.triggerType

        if (!bean.iftttTasks.isNullOrEmpty()) {
            val iftttTasks = mutableListOf<SceneStackLinkedIftttTask>()
            bean.iftttTasks.forEach { ifttt ->
                val cp = SceneStackLinkedCustomParam()
                if (ifttt.customParam.name.isNotEmpty()) cp.name = ifttt.customParam.name
//                if (ifttt.customParam.icon.isNotEmpty()) cp.icon = ifttt.customParam.icon
                if (ifttt.customParam.mid.isNotEmpty()) cp.mid = ifttt.customParam.mid
                if (ifttt.customParam.devName.isNotEmpty()) cp.devName = ifttt.customParam.devName
                if (ifttt.customParam.family_folder.isNotEmpty()) cp.family_folder = ifttt.customParam.family_folder
                val p = SceneStackLinkedParams()
                if (ifttt.params.time != "") p.time = ifttt.params.time.toInt()
                if (ifttt.params.sceneId.isNotEmpty()) p.sceneId = ifttt.params.sceneId
                if (ifttt.params.devTid.isNotEmpty()) p.devTid = ifttt.params.devTid
                if (ifttt.params.ctrlKey.isNotEmpty()) p.ctrlKey = ifttt.params.ctrlKey
                if (ifttt.params.subDevTid.isNotEmpty()) p.subDevTid = ifttt.params.subDevTid
                if (!ifttt.params.data.isNullOrEmpty()) {
                    val da = mutableMapOf<String, Any>()
                    ifttt.params.data.forEach { (t, u) ->
                        if (u.matches(Regex("^[0-9]$"))) {
                            da[t] = u.toLong()
                        } else {
                            da[t] = u
                        }
                    }
                    p.data = da
                }
                val b = SceneStackLinkedIftttTask()
                b.desc = ifttt.desc
                b.customParam = cp
                b.params = p
                b.type = ifttt.type
                iftttTasks.add(b)
            }
            info.iftttTasks = iftttTasks
        }

        if (!bean.conditionList.isNullOrEmpty()) {
            val conditionList = mutableListOf<SceneStackLinkedCondition>()
            bean.conditionList.forEach { cond ->
                val sct = SceneStackLinkedCondition()
                if (cond.devTid.isNotEmpty()) sct.devTid = cond.devTid
                if (cond.ctrlKey.isNotEmpty()) sct.ctrlKey = cond.ctrlKey
                if (cond.subDevTid.isNotEmpty()) sct.subDevTid = cond.subDevTid
                if (cond.conDesc.isNotEmpty()) sct.conDesc = cond.conDesc
                if (cond.relation.isNotEmpty()) sct.relation = cond.relation
                val cf = SceneStackLinkedCustomFields()
                cond.customFields.let { condcf ->
                    if (condcf.name.isNotEmpty()) cf.name = condcf.name
                    if (condcf.mid.isNotEmpty()) cf.mid = condcf.mid
//                    if (condcf.icon.isNotEmpty()) cf.icon = condcf.icon
                    if (condcf.family_folder.isNotEmpty()) cf.family_folder = condcf.family_folder
                }
                sct.customFields = cf
                if (!cond.triggerParams.isNullOrEmpty()) {
                    val stpList = mutableListOf<SceneStackLinkedTriggerParam>()
                    cond.triggerParams.forEach { condstp ->
                        val stp = SceneStackLinkedTriggerParam()
                        condstp.left.let { stp.left = condstp.left }
                        condstp.right.let { stp.right = condstp.right }
                        condstp.operator.let { stp.operator = condstp.operator }
                        stpList.add(stp)
                    }
                    sct.triggerParams = stpList
                }
                conditionList.add(sct)
            }
            info.conditionList = conditionList
        }

        val json: String = JsonUtils.toJson(info)
        val kalle = Kalle.put(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_LINKAGE_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.urlParam("ruleId", ruleId)
        kalle.urlParam("enable", enable)
        kalle.body(JsonBody(json))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneLinkBeanNew>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 删除联动场景
     * */
    fun deleteLinkageScene(ruleId: String, randomToken: String, fail: OnFailResult, success: DeviceDeleteResult) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_DELETE_LINKAGE_SCENE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.urlParam("ruleId", ruleId)
        if (randomToken.isNotEmpty()) kalle.urlParam("randomToken", randomToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    if (response.succeed().isEmpty()) {
                        KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { success.success(DeviceDeleteBean()) }
                    } else {
                        KotlinSerializationUtils.getJsonData<DeviceDeleteBean>(response.succeed()).let { success.success(it) }
                    }
                }
            }
        })
    }

    /**
     * 删除已使用的联动场景
     * */
    fun deleteUseLinkageScene(ruleId: String, randomToken: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_DELETE_LINKAGE_SCENE).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("ruleId", ruleId).param("randomToken", randomToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }

    /**
     * 更新一键场景排序
     * */
    fun updateOneKeySort(list: List<SceneOneKeySortBean>, callback: IResultCallback) {
        val map: MutableMap<String, Any> = HashMap()
        map["requestList"] = list
        val json = JsonUtils.toJson(map)
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_UPDATE_ONEKEY_SCENE_SORT).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(json)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }

    /**
     * 更新联动场景排序
     * */
    fun updateLinkSceneSort(list: List<SceneLinkSortBean>, callback: IResultCallback) {
        val map: MutableMap<String, Any> = HashMap()
        map["requestList"] = list
        val json = JsonUtils.toJson(map)
        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_UPDATE_LINK_SCENE_SORT).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(json)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<String>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }
}