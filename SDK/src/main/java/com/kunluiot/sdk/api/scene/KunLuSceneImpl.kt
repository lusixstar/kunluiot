package com.kunluiot.sdk.api.scene

import com.kunluiot.sdk.callback.scene.ISceneListCallback
import com.kunluiot.sdk.request.SceneRequestUtil

internal class KunLuSceneImpl : IKunLuScene {

    /**
     * 手动场景列表
     */
    override fun getOneKeySceneList(callback: ISceneListCallback) {
        SceneRequestUtil.getOneKeySceneList(callback)
    }


}