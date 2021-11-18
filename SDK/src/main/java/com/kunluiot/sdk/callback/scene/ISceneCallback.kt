package com.kunluiot.sdk.callback.scene

import com.kunluiot.sdk.bean.scene.SceneListBean

/**
 * 场景列表
 * */
interface ISceneListCallback {
    fun onSuccess(bean: List<SceneListBean>)
    fun onError(code: String, error: String)
}
