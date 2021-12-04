package com.kunluiot.sdk.callback.scene

import com.kunluiot.sdk.bean.scene.*

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

/**
 * 新玩法
 * */
interface ISceneNewPlayCallback {
    fun onSuccess(bean: SceneNewPlayBean)
    fun onError(code: String, error: String)
}
