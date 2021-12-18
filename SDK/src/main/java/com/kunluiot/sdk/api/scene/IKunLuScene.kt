package com.kunluiot.sdk.api.scene

import com.kunluiot.sdk.bean.device.DeviceDeleteBean
import com.kunluiot.sdk.bean.scene.*
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.OnFailResult
import com.kunluiot.sdk.callback.common.OnSuccessResult
import com.kunluiot.sdk.callback.device.DeviceDeleteResult
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

    /**
     * 新增手动场景
     */
    fun addOneKeyScene(icon: String, sceneName: String, sceneTaskList: List<SceneOneKeyTaskListBean>, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 编辑手动场景
     */
    fun updateOneKeyScene(sceneId: String, icon: String, sceneName: String, sceneTaskList: List<SceneOneKeyTaskListBean>, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 删除手动场景
     * */
    fun deleteOneKeyScene(sceneId: String, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 删除使用中的手动场景
     * */
    fun deleteUseOneKeyScene(sceneId: String, iftttIds: String, callback: IResultCallback)

    /**
     * 联动场景列表
     */
    fun getLinkageSceneList(fail: OnFailResult, success: SceneLinkedListResult)

    /**
     * 新增联动场景
     * */
    fun addLinkageScene(bean: SceneLinkBeanNew, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 编辑联动场景
     * */
    fun updateLinkageScene(ruleId: String, enable: Boolean, data: SceneLinkBeanNew, fail: OnFailResult, success: OnSuccessResult)

    /**
     * 删除联动场景
     * */
    fun deleteLinkageScene(ruleId: String, randomToken: String, fail: OnFailResult, success: DeviceDeleteResult)

    /**
     * 删除已使用的联动场景
     * */
    fun deleteUseLinkageScene(ruleId: String, randomToken: String, callback: IResultCallback)

    /**
     * 更新一键场景排序
     * */
    fun updateOneKeySort(list: List<SceneOneKeySortBean>, callback: IResultCallback)

    /**
     * 更新联动场景排序
     * */
    fun updateLinkSceneSort(list: List<SceneLinkSortBean>, callback: IResultCallback)

}