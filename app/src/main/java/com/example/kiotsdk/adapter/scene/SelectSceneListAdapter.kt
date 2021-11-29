package com.example.kiotsdk.adapter.scene

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.scene.SceneLinkBean


class SelectSceneListAdapter(list: MutableList<SceneLinkBean>) : BaseQuickAdapter<SceneLinkBean, BaseViewHolder>(R.layout.item_select_scene, list) {

    override fun convert(holder: BaseViewHolder, item: SceneLinkBean) {
        holder.setText(R.id.name, item.ruleName)
    }
}