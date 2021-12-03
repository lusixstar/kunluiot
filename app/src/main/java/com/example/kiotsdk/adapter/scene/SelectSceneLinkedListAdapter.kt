package com.example.kiotsdk.adapter.scene

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean


class SelectSceneLinkedListAdapter(list: MutableList<SceneOneKeyBean>) : BaseQuickAdapter<SceneOneKeyBean, BaseViewHolder>(R.layout.item_select_scene, list) {

    override fun convert(holder: BaseViewHolder, item: SceneOneKeyBean) {
        holder.setText(R.id.name, item.sceneName)
    }
}