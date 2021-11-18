package com.kunluiot.sdk.api.scene

import com.kunluiot.sdk.bean.scene.SceneAddOneKeyBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.scene.ISceneDeleteCallback
import com.kunluiot.sdk.callback.scene.ISceneListCallback

interface IKunLuScene {

    /**
     * 手动场景列表
     */
    fun getOneKeySceneList(callback: ISceneListCallback)

    /**
     * 新增手动场景
     */
    fun addOneKeyScene(oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneAddOneKeyBean>, preset: Map<String, String>, templateId: String, callback: IResultCallback)

    /**
     * 编辑手动场景
     */
    fun updateOneKeyScene(sceneId: String, oneKeyType: Int, icon: String, sceneName: String, sceneTaskList: List<SceneAddOneKeyBean>, preset: Map<String, String>, callback: IResultCallback)

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
    fun getLinkageSceneList(page: Int, size: Int, callback: ISceneListCallback)

    /**
     * 新增联动场景
     * */
    fun addLinkageScene(bean: SceneAddOneKeyBean, callback: IResultCallback)

}