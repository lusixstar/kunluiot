package com.example.kiotsdk.adapter.scene

import android.widget.FrameLayout
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.bean.SceneIconBean


class SceneOneKeyIconListAdapter(list: MutableList<SceneIconBean>) : BaseQuickAdapter<SceneIconBean, BaseViewHolder>(R.layout.item_scene_one_key_icon, list) {

    override fun convert(holder: BaseViewHolder, item: SceneIconBean) {

        val flIcon = holder.getView<FrameLayout>(R.id.fl_icon)
        if (item.select) {
            flIcon.setBackgroundResource(R.drawable.bg_select_icon)
        } else {
            flIcon.setBackgroundResource(R.drawable.bg_un_select_icon);
        }
        val ivIcon = holder.getView<ImageView>(R.id.iv_icon)
        ivIcon.setImageResource(item.id)
    }
}