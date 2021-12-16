package com.kunluiot.sdk.callback.scene

import com.kunluiot.sdk.bean.scene.SceneLinkBeanNew
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
    fun success(bean: List<SceneLinkBeanNew>)
}
