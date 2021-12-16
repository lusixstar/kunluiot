package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.bean.scene.SceneIftttTasksListBeanNew


class SceneDeviceListAdapter(list: MutableList<SceneIftttTasksListBeanNew>) : BaseQuickAdapter<SceneIftttTasksListBeanNew, BaseViewHolder>(R.layout.item_scene_devices, list) {

    override fun convert(holder: BaseViewHolder, item: SceneIftttTasksListBeanNew) {

        holder.setText(R.id.name, item.customParam.name)

        val img = holder.getView<AppCompatImageView>(R.id.icon)
        if (item.customParam.icon.isNotEmpty()) {
            val imageIcon: String = item.customParam.icon
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

        if (item.customParam.desc.isNotEmpty()) {
            holder.setText(R.id.content, item.customParam.desc)
        } else {
            holder.setText(R.id.content, item.desc)
        }
    }
}