package com.kunluiot.sdk.callback.scene

import com.kunluiot.sdk.bean.scene.SceneLinkBean
import com.kunluiot.sdk.bean.scene.SceneNewPlayBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean

/**
 * 场景列表
 * */
fun interface SceneListResult {
    fun success(bean: List<SceneOneKeyBean>)
}

/**
 * 场景列表
 * */
fun interface SceneLinkedListResult {
    fun success(bean: List<SceneLinkBean>)
}

// -------------------------------------

/**
 * 新玩法
 * */
interface ISceneNewPlayCallback {
    fun onSuccess(bean: SceneNewPlayBean)
    fun onError(code: String, error: String)
}
