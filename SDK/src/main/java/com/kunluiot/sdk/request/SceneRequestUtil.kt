package com.kunluiot.sdk.request

import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.scene.ISceneNewPlayCallback
import com.kunluiot.sdk.callback.scene.SceneLinkedListResult
import com.kunluiot.sdk.callback.scene.SceneListResult
import com.kunluiot.sdk.thirdlib.kalle.JsonBody
import com.kunluiot.sdk.thirdlib.kalle.Kalle
import com.kunluiot.sdk.thirdlib.kalle.simple.SimpleResponse
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.KotlinSerializationUtils
import org.json.JSONArray
import org.json.JSONObject
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
                bean.cmdArgs = it.cmdArgsM
            }
            bean.customParam = item
            bean.desc = it.desc
            list.add(bean)
        }

        val map = HashMap<String, Any>()
        map["sceneName"] = sceneName
        if (templateId.isNotEmpty()) map["templateId"] = templateId
        map["sceneTaskList"] = list
        if (templateId.isNotEmpty()) map["icon"] = icon
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
        val map = HashMap<String, Any>()
        map["sceneId"] = sceneId
        map["sceneName"] = sceneName
        map["sceneTaskList"] = sceneTaskList
        map["icon"] = icon
        map["oneKeyType"] = oneKeyType

        if (!preset.isNullOrEmpty()) {
            preset.forEach { (t, u) -> map[t] = u }
        }
        var json: String = JsonUtils.toJson(map)
        try {
            val jsonObject = JSONObject(json)
            val sceneTaskLists = jsonObject.getJSONArray("sceneTaskList")
            val sceneTaskLists2 = JSONArray()
            for (i in 0 until sceneTaskLists.length()) {
                val jb = sceneTaskLists.getJSONObject(i)
                val cmdArgs = jb.getJSONObject("cmdArgs")
                val cmdArgs2 = JSONObject()
                val keys = cmdArgs.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val value = cmdArgs[key].toString()
                    if (value.matches(Regex("^[0-9]*$"))) {
                        cmdArgs2.put(key, value.toInt())
                    } else {
                        cmdArgs2.put(key, value)
                    }
                }
                jb.put("cmdArgs", cmdArgs2)
                sceneTaskLists2.put(jb)
            }
            jsonObject.put("sceneTaskList", sceneTaskLists2)
            json = jsonObject.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Kalle.patch(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST + "/$sceneId").addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(json)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
     * 删除手动场景
     * */
    fun deleteOneKeyScene(sceneId: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_ONE_KEY_SCENE_LIST + "/$sceneId").addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
    fun addLinkageScene(bean: SceneAddOneKeyBean, callback: IResultCallback) {

        var json = JsonUtils.toJson(bean)
        try {
            val jsonObject = JSONObject(json)
            val conditionList = jsonObject.getJSONArray("conditionList")
            val conditionList1 = JSONArray()
            for (k in 0 until conditionList.length()) {
                val obj = conditionList.getJSONObject(k)
                val jsonArray = obj.getJSONArray("triggerParams")
                val jsonArray1 = JSONArray()
                for (i in 0 until jsonArray.length()) {
                    val jb = jsonArray.getJSONObject(i)
                    val right = jb.getString("right")
                    if (right.matches(Regex("^[0-9]*$"))) {
                        jb.put("right", right.toInt())
                    }
                    jsonArray1.put(jb)
                }
                obj.put("triggerParams", jsonArray1)
                conditionList1.put(obj)
            }
            jsonObject.put("conditionList", conditionList1)
            json = jsonObject.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Kalle.post(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_LINKAGE_SCENE_LIST).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).body(JsonBody(json)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
     * 编辑联动场景
     * */
    fun updateLinkageScene(ruleId: String, enable: Boolean, bean: SceneAddOneKeyBean, callback: IResultCallback) {

        var json = JsonUtils.toJson(bean)
        try {
            val jsonObject = JSONObject(json)
            val conditionList = jsonObject.getJSONArray("conditionList")
            val conditionList1 = JSONArray()
            for (k in 0 until conditionList.length()) {
                val obj = conditionList.getJSONObject(k)
                val jsonArray = obj.getJSONArray("triggerParams")
                val jsonArray1 = JSONArray()
                for (i in 0 until jsonArray.length()) {
                    val jb = jsonArray.getJSONObject(i)
                    val right = jb.getString("right")
                    if (right.matches(Regex("^[0-9]*$"))) {
                        jb.put("right", right.toInt())
                    }
                    jsonArray1.put(jb)
                }
                obj.put("triggerParams", jsonArray1)
                conditionList1.put(obj)
            }
            jsonObject.put("conditionList", conditionList1)
            json = jsonObject.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Kalle.put(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_LINKAGE_SCENE_LIST).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("ruleId", ruleId).param("enable", enable).body(JsonBody(json)).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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
     * 删除联动场景
     * */
    fun deleteLinkageScene(ruleId: String, callback: IResultCallback) {
        Kalle.delete(ReqApi.KHA_WEB_BASE_URL + SceneApi.KHA_API_DELETE_LINKAGE_SCENE).setHeaders(KunLuHelper.getSign()).addHeader("authorization", "Bearer " + KunLuHomeSdk.instance.getSessionBean()?.accessToken).param("ruleId", ruleId).perform(object : KunLuNetCallback<String>(KunLuHomeSdk.instance.getApp()) {
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