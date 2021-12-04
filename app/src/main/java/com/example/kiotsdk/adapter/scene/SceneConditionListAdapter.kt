package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.bean.scene.SceneConditionListParam


class SceneConditionListAdapter(list: MutableList<SceneConditionListParam>) : BaseQuickAdapter<SceneConditionListParam, BaseViewHolder>(R.layout.item_scene_condition, list) {

    override fun convert(holder: BaseViewHolder, item: SceneConditionListParam) {

        item.customFields?.name?.let { holder.setText(R.id.name, it) }
        item.conDesc?.let { holder.setText(R.id.content, it) }

        val img = holder.getView<AppCompatImageView>(R.id.icon)
        item.customFields?.icon?.let { icon ->
            if (icon.isNotEmpty()) {
                val imageIcon: String = icon
                val imageBase64 = "data:image/png;base64,"
                if (imageIcon.substring(0, imageBase64.length) == imageBase64) {
                    img.setImageBitmap(DemoUtils.base64CodeToBitmap(imageIcon.substring(imageBase64.length, imageIcon.length)))
                } else {
                    img.setImageResource(R.mipmap.ic_scene_select_scene)
                }
            }
        }
    }
}