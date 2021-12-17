package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.bean.scene.SceneConditionListBeanNew


class SceneConditionListAdapter(list: MutableList<SceneConditionListBeanNew>) : BaseQuickAdapter<SceneConditionListBeanNew, BaseViewHolder>(R.layout.item_scene_condition, list) {

    override fun convert(holder: BaseViewHolder, item: SceneConditionListBeanNew) {

        holder.setText(R.id.name, item.customFields.name)
        holder.setText(R.id.content, item.conDesc)

        val img = holder.getView<AppCompatImageView>(R.id.icon)
        item.customFields.icon.let { icon ->
            if (icon.isNotEmpty()) {
                val imageIcon: String = icon
                val imageBase64 = "data:image/png;base64,"
                if (imageIcon.substring(0, imageBase64.length) == imageBase64) {
                    img.setImageBitmap(DemoUtils.base64CodeToBitmap(imageIcon.substring(imageBase64.length, imageIcon.length)))
                } else if (imageIcon.startsWith("http") || imageIcon.startsWith("https")) {
                    img.load(imageIcon) {
                        transformations(CircleCropTransformation())
                    }
                } else {
                    img.setImageResource(R.mipmap.ic_scene_select_scene)
                }
            }
        }
    }
}