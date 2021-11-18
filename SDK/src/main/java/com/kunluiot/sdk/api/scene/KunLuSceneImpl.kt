package com.kunluiot.sdk.api.scene

import com.kunluiot.sdk.bean.scene.SceneAddOneKeyBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.scene.ISceneDeleteCallback
import com.kunluiot.sdk.callback.scene.ISceneListCallback
import com.kunluiot.sdk.request.SceneRequestUtil

internal class KunLuSceneImpl : IKunLuScene {

    /**
     * 手动场景列表
     */
    override fun getOneKeySceneList(callback: ISceneListCallback) {
        SceneRequestUtil.getOneKeySceneList(callback)
    }

    /**
     * 新增手动场景
     */
    override fun addOneKeyScene(oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneAddOneKeyBean>, preset: Map<String, String>, templateId: String, callback: IResultCallback) {
        SceneRequestUtil.addOneKeyScene(oneKeyType, icon, sceneName, sceneTaskList, preset, templateId, callback)
    }

    /**
     * 编辑手动场景
     */
    override fun updateOneKeyScene(sceneId: String, oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneAddOneKeyBean>, preset: Map<String, String>, callback: IResultCallback) {
        SceneRequestUtil.updateOneKeyScene(sceneId, oneKeyType, icon, sceneName, sceneTaskList, preset, callback)
    }

    /**
     * 删除手动场景
     * */
    override fun deleteOneKeyScene(sceneId: String, callback: ISceneDeleteCallback) {
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
    override fun getLinkageSceneList(page: Int, size: Int, callback: ISceneListCallback) {
        SceneRequestUtil.getLinkageSceneList(page, size, callback)
    }

    /**
     * 新增联动场景
     * */
    override fun addLinkageScene(bean: SceneAddOneKeyBean, callback: IResultCallback) {
        SceneRequestUtil.addLinkageScene(bean, callback)
    }

}