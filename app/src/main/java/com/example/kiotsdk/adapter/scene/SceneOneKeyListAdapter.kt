package com.example.kiotsdk.adapter.scene

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean


class SceneOneKeyListAdapter(list: MutableList<SceneOneKeyBean>) : BaseQuickAdapter<SceneOneKeyBean, BaseViewHolder>(R.layout.item_scene_one_key, list) {

    override fun convert(holder: BaseViewHolder, item: SceneOneKeyBean) {

        if (item.name.isNotEmpty()) {
            holder.setText(R.id.name, item.name)
        } else {
            holder.setText(R.id.name, item.sceneName)
        }

        val img = holder.getView<AppCompatImageView>(R.id.logo)
        if (item.icon.isNotEmpty()) {
            val imageBase64 = "data:image/png;base64,"
            if (item.icon.contains(imageBase64)) {
                img.setImageBitmap(DemoUtils.base64CodeToBitmap(item.icon.substring(imageBase64.length, item.icon.length)))
            } else {
                img.setImageResource(R.mipmap.ic_scene_11)
            }
        } else {
            img.setImageResource(R.mipmap.ic_scene_11)
        }
    }
}