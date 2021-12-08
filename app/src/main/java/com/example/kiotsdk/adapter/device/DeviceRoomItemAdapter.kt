package com.example.kiotsdk.adapter.device

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceNewBean


class DeviceRoomItemAdapter(list: MutableList<DeviceNewBean>) : BaseQuickAdapter<DeviceNewBean, BaseViewHolder>(R.layout.item_room_item, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceNewBean) {
        val name = if (item.deviceName.isNotEmpty()) item.deviceName else item.name
        holder.setText(R.id.text, "设备名称：$name")

        if (item.online) {
            holder.setText(R.id.next, "在线   >")
        } else {
            holder.setText(R.id.next, "离线   >")
        }
    }
}