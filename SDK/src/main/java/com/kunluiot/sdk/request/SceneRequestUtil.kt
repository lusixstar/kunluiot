package com.kunluiot.sdk.request

import com.elvishew.xlog.XLog
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.scene.ISceneNewPlayCallback
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
                    KotlinSerializationUtils.getJsonData<List<SceneOneKeyBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    // ----------------------------------------------------------

    /**
     * 手动场景列表
     * */
    fun addOneKeyScene(oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneLinkedBean>, preset: Map<String, String>, templateId: String, callback: IResultCallback) {

        val list = mutableListOf<SceneStackClassBean>()
        sceneTaskList.forEach {
            val bean = SceneStackClassBean()
            val item = SceneStackCustomParam()
            item.name = it.customParam.name
            item.icon = it.customParam.icon
            if (it.time != 0 && it.customParam.name.isNotEmpty()) {
                bean.time = it.time
            } else if (it.iftttId.isNotEmpty() && it.customParam.name.isNotEmpty()) {
                bean.enable = it.enable
                bean.iftttId = it.iftttId
            } else {
                item.mid = it.customParam.mid
                item.family_folder = it.customParam.family_folder
                if (it.devTid.isNotEmpty()) bean.devTid = it.devTid
                if (it.subDevTid.isNotEmpty()) bean.subDevTid = it.subDevTid
                if (it.ctrlKey.isNotEmpty()) bean.ctrlKey = it.ctrlKey
                bean.newDesc = it.newDesc
                val map = mutableMapOf<String, Long>()
                it.cmdArgs.forEach { (t, u) ->
                    map[t] = u.toLong()
                }
                if (map.isNullOrEmpty()) bean.cmdArgs = map
            }
            bean.customParam = item
            bean.desc = it.desc
            list.add(bean)
        }

        val map = HashMap<String, Any>()
        map["sceneName"] = sceneName
        if (templateId.isNotEmpty()) map["templateId"] = templateId
        map["sceneTaskList"] = list
        if (icon.isNotEmpty()) map["icon"] = icon
        if (oneKeyType != 0) map["oneKeyType"] = oneKeyType
        if (!preset.isNullOrEmpty()) {
            preset.forEach { (t, u) -> map[t] = u }
        }
        val json: String = JsonUtils.toJson(map)

        val kalle = Kalle.post(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(json))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneLinkedBean>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }

    /**
     * 编辑手动场景
     * */
    fun updateOneKeyScene(sceneId: String, oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneLinkedBean>, preset: Map<String, String>, callback: IResultCallback) {

        val list = mutableListOf<SceneStackClassBean>()
        sceneTaskList.forEach {
            val bean = SceneStackClassBean()
            val item = SceneStackCustomParam()
            item.name = it.customParam.name
            item.icon = it.customParam.icon
            if (it.time != 0 && it.customParam.name.isNotEmpty()) {
                bean.time = it.time
            } else if (it.iftttId.isNotEmpty() && it.customParam.name.isNotEmpty()) {
                bean.enable = it.enable
                bean.iftttId = it.iftttId
            } else {
                item.mid = it.customParam.mid
                item.family_folder = it.customParam.family_folder
                if (it.devTid.isNotEmpty()) bean.devTid = it.devTid
                if (it.subDevTid.isNotEmpty()) bean.subDevTid = it.subDevTid
                if (it.ctrlKey.isNotEmpty()) bean.ctrlKey = it.ctrlKey
                bean.newDesc = it.newDesc
                val map = mutableMapOf<String, Long>()
                it.cmdArgs.forEach { (t, u) ->
                    map[t] = u.toLong()
                }
                if (map.isNullOrEmpty()) bean.cmdArgs = map
            }
            bean.customParam = item
            bean.desc = it.desc
            list.add(bean)
        }

        val map = HashMap<String, Any>()
        map["sceneId"] = sceneId
        map["sceneName"] = sceneName
        if (icon.isNotEmpty()) map["icon"] = icon
        if (oneKeyType != 0) map["oneKeyType"] = oneKeyType
        map["sceneTaskList"] = list

        if (!preset.isNullOrEmpty()) {
            preset.forEach { (t, u) -> map[t] = u }
        }
        val json: String = JsonUtils.toJson(map)
        val kalle = Kalle.patch(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST + "/$sceneId")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.body(JsonBody(json))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneLinkedBean>(response.succeed()).let { callback.onSuccess() }
                }
            }
        })
    }

    /**
     * 删除手动场景
     * */
    fun deleteOneKeyScene(sceneId: String, callback: IResultCallback) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST + "/$sceneId")
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
    fun getLinkageSceneList(page: Int, size: Int, fail: OnFailResult, success: SceneLinkedListResult) {
        val kalle = Kalle.get(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_LINKAGE_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.param("page", page).param("size", size)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<List<SceneLinkBean>>(response.succeed()).let { success.success(it) }
                }
            }
        })
    }

    /**
     * 新增联动场景
     * */
    fun addLinkageScene(bean: SceneLinkBean, fail: OnFailResult, success: OnSuccessResult) {

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
                if (ifttt.customParam.icon.isNotEmpty()) cp.icon = ifttt.customParam.icon
                if (ifttt.customParam.mid.isNotEmpty()) cp.mid = ifttt.customParam.mid
                if (ifttt.customParam.devName.isNotEmpty()) cp.devName = ifttt.customParam.devName
                if (ifttt.customParam.family_folder.isNotEmpty()) cp.family_folder = ifttt.customParam.family_folder
                val p = SceneStackLinkedParams()
                if (ifttt.time != 0) p.time = ifttt.time
                if (ifttt.iftttId.isNotEmpty()) p.sceneId = ifttt.iftttId
                if (ifttt.devTid.isNotEmpty()) p.devTid = ifttt.devTid
                if (ifttt.ctrlKey.isNotEmpty()) p.ctrlKey = ifttt.ctrlKey
                if (ifttt.subDevTid.isNotEmpty()) p.subDevTid = ifttt.subDevTid
                if (!ifttt.cmdArgsLink.isNullOrEmpty()) {
                    val da = mutableMapOf<String, Long>()
                    ifttt.cmdArgsLink.forEach { (t, u) -> da[t] = u.toLong() }
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
                cond.devTid?.let { sct.devTid = cond.devTid }
                cond.ctrlKey?.let { sct.ctrlKey = cond.ctrlKey }
                cond.conDesc?.let { sct.conDesc = cond.conDesc }
                cond.relation?.let { sct.relation = cond.relation }
                val cf = SceneStackLinkedCustomFields()
                cond.customFields?.let { condcf ->
                    if (condcf.name.isNotEmpty()) cf.name = condcf.name
                    if (condcf.mid.isNotEmpty()) cf.mid = condcf.mid
                    if (condcf.icon.isNotEmpty()) cf.icon = condcf.icon
                    if (condcf.family_folder.isNotEmpty()) cf.family_folder = condcf.family_folder
                }
                sct.customFields = cf
                if (!cond.triggerParams.isNullOrEmpty()) {
                    val stpList = mutableListOf<SceneStackLinkedTriggerParam>()
                    cond.triggerParams!!.forEach { condstp ->
                        val stp = SceneStackLinkedTriggerParam()
                        condstp.left?.let { stp.left = condstp.left }
                        condstp.right?.let { stp.right = condstp.right }
                        condstp.operator?.let { stp.operator = condstp.operator }
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
                    KotlinSerializationUtils.getJsonData<SceneLinkBean>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 编辑联动场景
     * */
    fun updateLinkageScene(ruleId: String, enable: Boolean, bean: SceneLinkBean, fail: OnFailResult, success: OnSuccessResult) {

        val info = SceneStackLinkedBean()
        info.timeZoneOffset = bean.timeZoneOffset
        info.ruleName = bean.ruleName
        info.iftttType = bean.iftttType
        info.enabled = enable
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
                if (ifttt.params.time != 0) p.time = ifttt.params.time
                if (ifttt.params.sceneId.isNotEmpty()) p.sceneId = ifttt.params.sceneId
                if (ifttt.params.devTid.isNotEmpty()) p.devTid = ifttt.params.devTid
                if (ifttt.params.ctrlKey.isNotEmpty()) p.ctrlKey = ifttt.params.ctrlKey
                if (ifttt.params.subDevTid.isNotEmpty()) p.subDevTid = ifttt.params.subDevTid
                if (!ifttt.params.data.isNullOrEmpty()) {
                    val da = mutableMapOf<String, String>()
                    ifttt.params.data.forEach { (t, u) -> da[t] = u }
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
                cond.devTid?.let { sct.devTid = cond.devTid }
                cond.ctrlKey?.let { sct.ctrlKey = cond.ctrlKey }
                cond.conDesc?.let { sct.conDesc = cond.conDesc }
                cond.relation?.let { sct.relation = cond.relation }
                val cf = SceneStackLinkedCustomFields()
                cond.customFields?.let { condcf ->
                    if (condcf.name.isNotEmpty()) cf.name = condcf.name
                    if (condcf.mid.isNotEmpty()) cf.mid = condcf.mid
//                    if (condcf.icon.isNotEmpty()) cf.icon = condcf.icon
                    if (condcf.family_folder.isNotEmpty()) cf.family_folder = condcf.family_folder
                }
                sct.customFields = cf
                if (!cond.triggerParams.isNullOrEmpty()) {
                    val stpList = mutableListOf<SceneStackLinkedTriggerParam>()
                    cond.triggerParams!!.forEach { condstp ->
                        val stp = SceneStackLinkedTriggerParam()
                        condstp.left?.let { stp.left = condstp.left }
                        condstp.right?.let { stp.right = condstp.right }
                        condstp.operator?.let { stp.operator = condstp.operator }
                        stpList.add(stp)
                    }
                    sct.triggerParams = stpList
                }
                conditionList.add(sct)
            }
            info.conditionList = conditionList
        }

        val json: String = JsonUtils.toJson(info)
        XLog.e("bean == $bean")
        XLog.e("json == $json")
        val kalle = Kalle.put(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_LINKAGE_SCENE_LIST)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.urlParam("ruleId", ruleId)
        kalle.body(JsonBody(json))
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    fail.fail(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneLinkBean>(response.succeed()).let { success.success() }
                }
            }
        })
    }

    /**
     * 删除联动场景
     * */
    fun deleteLinkageScene(ruleId: String, callback: IResultCallback) {
        val kalle = Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_DELETE_LINKAGE_SCENE)
        kalle.addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken)
        kalle.urlParam("ruleId", ruleId)
        kalle.perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
     * 新玩法列表
     * */
    fun getNewPlay(callback: ISceneNewPlayCallback) {
        Kalle.get(ReqApi.KHA_CONSOLE_BASE_URL + SceneApi.KHA_API_NEW_PLAY).setHeaders(KunLuHelper.getSign()).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
            override fun onResponse(response: SimpleResponse<String, String>) {
                val failed = response.failed()
                if (!failed.isNullOrEmpty()) {
                    callback.onError(response.code().toString(), failed)
                } else {
                    KotlinSerializationUtils.getJsonData<SceneNewPlayBean>(response.succeed()).let { callback.onSuccess(it) }
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