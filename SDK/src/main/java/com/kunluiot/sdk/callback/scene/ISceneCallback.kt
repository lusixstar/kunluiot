package com.kunluiot.sdk.callback.scene

import com.kunluiot.sdk.bean.scene.SceneDeleteBean
import com.kunluiot.sdk.bean.scene.SceneListBean

/**
 * 场景列表
 * */
interface ISceneListCallback {
    fun onSuccess(bean: List<SceneListBean>)
    fun onError(code: String, error: String)
}

/**
 * 删除场景
 * */
interface ISceneDeleteCallback {
    fun onSuccess(bean: SceneDeleteBean)
    fun onError(code: String, error: String)
}
