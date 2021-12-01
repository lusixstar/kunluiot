package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.bean.scene.SceneLinkBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean


class SceneLinkedListAdapter(list: MutableList<SceneLinkBean>) : BaseQuickAdapter<SceneLinkBean, BaseViewHolder>(R.layout.item_scene_linked, list) {

    override fun convert(holder: BaseViewHolder, item: SceneLinkBean) {

        holder.setText(R.id.name, item.ruleName)
        holder.setText(R.id.content, item.desc)

        val img = holder.getView<AppCompatImageView>(R.id.off)
        if (item.enabled) {
            img.setImageResource(R.mipmap.ic_fragment_device_switch_on)
        } else {
            img.setImageResource(R.mipmap.ic_fragment_device_switch_on)
        }
    }
}