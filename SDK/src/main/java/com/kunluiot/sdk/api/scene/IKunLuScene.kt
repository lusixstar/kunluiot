package com.kunluiot.sdk.api.scene

import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.scene.ISceneListCallback

interface IKunLuScene {

    /**
     * 手动场景列表
     */
    fun getOneKeySceneList(callback: ISceneListCallback)

    /**
     * 新增手动场景
     */
    fun addOneKeyScene(callback: IResultCallback)
}