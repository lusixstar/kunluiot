package com.example.kiotsdk.adapter.scene

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.scene.SceneLinkBeanNew


class SelectSceneListAdapter(list: MutableList<SceneLinkBeanNew>) : BaseQuickAdapter<SceneLinkBeanNew, BaseViewHolder>(R.layout.item_select_scene, list) {

    override fun convert(holder: BaseViewHolder, item: SceneLinkBeanNew) {
        holder.setText(R.id.name, item.ruleName)
    }
}