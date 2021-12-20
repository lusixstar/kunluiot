package com.example.kiotsdk.adapter.scene

import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean


class SceneSelectDeviceRoomItemAdapter(list: MutableList<DeviceNewBean>, private val gotoNext: (Map<String, DeviceOperationProtocolBean>, DeviceNewBean) -> Unit) : BaseQuickAdapter<DeviceNewBean, BaseViewHolder>(R.layout.item_scene_select_device_item, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceNewBean) {

        val text = holder.getView<TextView>(R.id.text)
        val next = holder.getView<TextView>(R.id.next)
        val name = if (item.deviceName.isNotEmpty()) item.deviceName else item.name
        if (item.devType == "GATEWAY") {
            holder.setText(R.id.text, "网关设备名称：$name")
        } else {
            holder.setText(R.id.text, "设备名称：$name")
        }
        getProtocol(item.productPublicKey, text, next, holder.itemView, item)
    }

    private fun getProtocol(productPublicKey: String, text: TextView, next: TextView, itemView: View, item: DeviceNewBean) {
        KunLuHomeSdk.deviceImpl.getDeviceOperationList(productPublicKey, { _, _ -> }, { info ->
            text.setTextColor(text.context.resources.getColor(R.color.picture_color_light_grey))
            next.isGone = true
            if (!info.protocol.isNullOrEmpty()) {
//                XLog.e(info)
                info.protocol.forEach { (_, u) ->
                    if (!u.fields.isNullOrEmpty() && u.usedForIFTTT) {
                        text.setTextColor(text.context.resources.getColor(R.color.black))
                        next.isGone = false
                        itemView.setOnClickListener { gotoNext.invoke(info.protocol, item) }
                    }
                }
            }
        })
    }
}