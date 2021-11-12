package com.example.kiotsdk.adapter.device

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceProductTabBean


class DeviceTabListAdapter(list : MutableList<DeviceProductTabBean>) : BaseQuickAdapter<DeviceProductTabBean, BaseViewHolder>(R.layout.item_device_tab_list, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceProductTabBean) {
        holder.setText(R.id.text, item.categoryName)

        if (item.select) {
            holder.setTextColor(R.id.text,  ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.setBackgroundColor(R.id.item,  ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.setTextColor(R.id.text,  ContextCompat.getColor(holder.itemView.context, R.color.black))
            holder.setBackgroundColor(R.id.item,  ContextCompat.getColor(holder.itemView.context, R.color.picture_color_light_grey))
        }
    }

    fun selectPosition(position: Int) {
        data.forEach { it.select = false }
        data[position].select = true
        notifyItemRangeChanged(0, data.size)
    }
}