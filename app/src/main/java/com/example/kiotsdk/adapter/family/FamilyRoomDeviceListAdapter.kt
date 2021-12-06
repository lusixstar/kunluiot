package com.example.kiotsdk.adapter.family

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceNewBean


class FamilyRoomDeviceListAdapter(list: MutableList<DeviceNewBean>) : BaseQuickAdapter<DeviceNewBean, BaseViewHolder>(R.layout.item_family_room_device_list, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceNewBean) {
        val name = if (item.deviceName.isNotEmpty()) item.deviceName else item.name
        holder.setText(R.id.name, name)

        val img = holder.getView<ImageView>(R.id.img)
        img.load(item.logo) {
            transformations(CircleCropTransformation())
        }
    }
}