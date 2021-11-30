package com.kunluiot.sdk.api.scene

import com.kunluiot.sdk.bean.scene.SceneAddOneKeyBean
import com.kunluiot.sdk.bean.scene.SceneLinkSortBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeySortBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.scene.*

interface IKunLuScene {

    /**
     * 手动场景列表
     */
    fun getOneKeySceneList(fail: OnFailResult, success: SceneListResult)

    /**
     * 获取预设情景面板
     */
    fun getSceneTemplate(fail: OnFailResult, success: SceneListResult)

    // ----------------------------------------------------

    /**
     * 新增手动场景
     */
    fun addOneKeyScene(oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneLinkedBean>, preset: Map<String, String>, templateId: String, callback: IResultCallback)

    /**
     * 编辑手动场景
     */
    fun updateOneKeyScene(sceneId: String, oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneLinkedBean>, preset: Map<String, String>, callback: IResultCallback)

    /**
     * 删除手动场景
     * */
    fun deleteOneKeyScene(sceneId: String, callback: ISceneDeleteCallback)

    /**
     * 删除使用中的手动场景
     * */
    fun deleteUseOneKeyScene(sceneId: String, iftttIds: String, callback: IResultCallback)

    /**
     * 联动场景列表
     */
    fun getLinkageSceneList(page: Int, size: Int, fail: OnFailResult, success: SceneLinkedListResult)

    /**
     * 新增联动场景
     * */
    fun addLinkageScene(bean: SceneAddOneKeyBean, callback: IResultCallback)

    /**
     * 编辑联动场景
     * */
    fun updateLinkageScene(ruleId: String, enable: Boolean, data: SceneAddOneKeyBean, callback: IResultCallback)

    /**
     * 删除联动场景
     * */
    fun deleteLinkageScene(ruleId: String, callback: IResultCallback)

    /**
     * 删除已使用的联动场景
     * */
    fun deleteUseLinkageScene(ruleId: String, randomToken: String, callback: IResultCallback)

    /**
     * 新玩法列表
     */
    fun getNewPlay(callback: ISceneNewPlayCallback)

    /**
     * 更新一键场景排序
     * */
    fun updateOneKeySort(list: List<SceneOneKeySortBean>, callback: IResultCallback)

    /**
     * 更新联动场景排序
     * */
    fun updateLinkSceneSort(list: List<SceneLinkSortBean>, callback: IResultCallback)

}