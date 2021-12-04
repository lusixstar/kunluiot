package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.bean.scene.SceneLinkBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean


class SceneDeviceListAdapter(list: MutableList<SceneLinkedBean>) : BaseQuickAdapter<SceneLinkedBean, BaseViewHolder>(R.layout.item_scene_devices, list) {

    override fun convert(holder: BaseViewHolder, item: SceneLinkedBean) {

        if (item.customParam.disPlayName.isNotEmpty()) {
            holder.setText(R.id.name, item.customParam.disPlayName)
        } else {
            holder.setText(R.id.name, item.customParam.name)
        }

        val img = holder.getView<AppCompatImageView>(R.id.icon)
        if (item.customParam.icon.isNotEmpty()) {
            val imageIcon: String = item.customParam.icon
            val imageBase64 = "data:image/png;base64,"
            if (imageIcon.substring(0, imageBase64.length) == imageBase64) {
                img.setImageBitmap(DemoUtils.base64CodeToBitmap(imageIcon.substring(imageBase64.length, imageIcon.length)))
            } else {
                img.setImageResource(R.mipmap.ic_scene_select_scene)
            }
        }

        if (item.newDesc.isNotEmpty()) {
            holder.setText(R.id.content, item.newDesc)
        } else {
            if (item.customParam.desc.isNotEmpty()) {
                holder.setText(R.id.content, item.customParam.desc)
            } else {
                holder.setText(R.id.content, item.desc)
            }
        }
    }
}