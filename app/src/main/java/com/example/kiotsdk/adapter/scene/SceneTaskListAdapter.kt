package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.bean.scene.SceneOneKeyTaskListBean


class SceneTaskListAdapter(list: MutableList<SceneOneKeyTaskListBean>) : BaseQuickAdapter<SceneOneKeyTaskListBean, BaseViewHolder>(R.layout.item_scene_devices, list) {

    override fun convert(holder: BaseViewHolder, item: SceneOneKeyTaskListBean) {

        holder.setText(R.id.name, item.customParam.name)

        val img = holder.getView<AppCompatImageView>(R.id.icon)
        if (item.customParam.icon.isNotEmpty()) {
            val imageIcon: String = item.customParam.icon
            val imageBase64 = "data:image/png;base64,"
            if (imageIcon.contains(imageBase64)) {
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