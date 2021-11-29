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


class SceneSelectDeviceRoomItemAdapter(list: MutableList<DeviceNewBean>, private val gotoNext: (Map<String, DeviceOperationProtocolBean>) -> Unit) : BaseQuickAdapter<DeviceNewBean, BaseViewHolder>(R.layout.item_scene_select_device_item, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceNewBean) {

        val text = holder.getView<TextView>(R.id.text)
        val next = holder.getView<TextView>(R.id.next)

        holder.setText(R.id.text, "设备名称：${item.deviceName}")
        getProtocol(item.productPublicKey, holder.layoutPosition, text, next, holder.itemView)
    }

    private fun getProtocol(productPublicKey: String, layoutPosition: Int, text: TextView, next: TextView, itemView: View) {
        KunLuHomeSdk.deviceImpl.getDeviceOperationList(productPublicKey, { _, _ -> }, { info ->
            if (!info.protocol.isNullOrEmpty()) {
                XLog.e("device operation == $info")
                XLog.e("device layoutPosition == $layoutPosition")
                text.setTextColor(text.context.resources.getColor(R.color.black))
                next.isGone = false
                itemView.setOnClickListener { gotoNext.invoke(info.protocol) }
            } else {
                text.setTextColor(text.context.resources.getColor(R.color.picture_color_light_grey))
                next.isGone = true
            }
        })
    }
}