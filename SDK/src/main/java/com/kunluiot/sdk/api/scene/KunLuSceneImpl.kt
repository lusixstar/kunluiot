package com.kunluiot.sdk.api.scene

import com.kunluiot.sdk.bean.scene.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.scene.*
import com.kunluiot.sdk.request.SceneRequestUtil

internal class KunLuSceneImpl : IKunLuScene {

    /**
     * 手动场景列表
     */
    override fun getOneKeySceneList(fail: OnFailResult, success: SceneListResult) {
        SceneRequestUtil.getOneKeySceneList(fail, success)
    }

    /**
     * 获取预设情景面板
     */
    override fun getSceneTemplate(fail: OnFailResult, success: SceneListResult) {
        SceneRequestUtil.getSceneTemplate(fail, success)
    }

    // -----------------------------------------------------------------


    /**
     * 新增手动场景
     */
    override fun addOneKeyScene(oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneLinkedBean>, preset: Map<String, String>, templateId: String, callback: IResultCallback) {
        SceneRequestUtil.addOneKeyScene(oneKeyType, icon, sceneName, sceneTaskList, preset, templateId, callback)
    }

    /**
     * 编辑手动场景
     */
    override fun updateOneKeyScene(sceneId: String, oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneLinkedBean>, preset: Map<String, String>, callback: IResultCallback) {
        SceneRequestUtil.updateOneKeyScene(sceneId, oneKeyType, icon, sceneName, sceneTaskList, preset, callback)
    }

    /**
     * 删除手动场景
     * */
    override fun deleteOneKeyScene(sceneId: String, callback: IResultCallback) {
        SceneRequestUtil.deleteOneKeyScene(sceneId, callback)
    }

    /**
     * 删除使用中的手动场景
     * */
    override fun deleteUseOneKeyScene(sceneId: String, iftttIds: String, callback: IResultCallback) {
        SceneRequestUtil.deleteUseOneKeyScene(sceneId, iftttIds, callback)
    }

    /**
     * 联动场景列表
     */
    override fun getLinkageSceneList(page: Int, size: Int, fail: OnFailResult, success: SceneLinkedListResult) {
        SceneRequestUtil.getLinkageSceneList(page, size, fail, success)
    }

    /**
     * 新增联动场景
     * */
    override fun addLinkageScene(bean: SceneLinkBean, fail: OnFailResult, success: OnSuccessResult) {
        SceneRequestUtil.addLinkageScene(bean, fail, success)
    }

    /**
     * 编辑联动场景
     * */
    override fun updateLinkageScene(ruleId: String, enable: Boolean, data: SceneLinkBean, fail: OnFailResult, success: OnSuccessResult) {
        SceneRequestUtil.updateLinkageScene(ruleId, enable, data, fail, success)
    }

    /**
     * 删除联动场景
     * */
    override fun deleteLinkageScene(ruleId: String, callback: IResultCallback) {
        SceneRequestUtil.deleteLinkageScene(ruleId, callback)
    }

    /**
     * 删除已使用的联动场景
     * */
    override fun deleteUseLinkageScene(ruleId: String, randomToken: String, callback: IResultCallback) {
        SceneRequestUtil.deleteUseLinkageScene(ruleId, randomToken, callback)
    }

    /**
     * 新玩法列表
     */
    override fun getNewPlay(callback: ISceneNewPlayCallback) {
        SceneRequestUtil.getNewPlay(callback)
    }

    /**
     * 更新一键场景排序
     * */
    override fun updateOneKeySort(list: List<SceneOneKeySortBean>, callback: IResultCallback) {
        SceneRequestUtil.updateOneKeySort(list, callback)
    }

    /**
     * 更新联动场景排序
     * */
    override fun updateLinkSceneSort(list: List<SceneLinkSortBean>, callback: IResultCallback) {
        SceneRequestUtil.updateLinkSceneSort(list, callback)
    }

}