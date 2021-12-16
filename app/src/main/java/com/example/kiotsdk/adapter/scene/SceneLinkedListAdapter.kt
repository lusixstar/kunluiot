package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.scene.SceneLinkBeanNew


class SceneLinkedListAdapter(list: MutableList<SceneLinkBeanNew>) : BaseQuickAdapter<SceneLinkBeanNew, BaseViewHolder>(R.layout.item_scene_linked, list) {

    override fun convert(holder: BaseViewHolder, item: SceneLinkBeanNew) {

        holder.setText(R.id.name, item.ruleName)

        var desc = ""
        if (item.triggerType == "SCHEDULER") {
            for (conditionItem in item.conditionList) {
                desc = conditionItem.customFields.name + conditionItem.conDesc
            }
        } else {
            for (conditionItem in item.conditionList) {
                val subDesc: String = conditionItem.conDesc
                desc = conditionItem.customFields.name + subDesc
            }
        }
        holder.setText(R.id.content, desc)

        val img = holder.getView<AppCompatImageView>(R.id.off)
        if (item.enabled) {
            img.setImageResource(R.mipmap.ic_fragment_device_switch_on)
        } else {
            img.setImageResource(R.mipmap.ic_fragment_device_switch_off)
        }
    }
}