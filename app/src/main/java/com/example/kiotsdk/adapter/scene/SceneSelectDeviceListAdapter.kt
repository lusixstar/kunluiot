package com.example.kiotsdk.adapter.scene

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.family.FolderBean


class SceneSelectDeviceListAdapter(list: MutableList<FolderBean>, private val gotoNext: (Map<String, DeviceOperationProtocolBean>, DeviceNewBean) -> Unit) : BaseQuickAdapter<FolderBean, BaseViewHolder>(R.layout.item_scene_select_device_list, list) {

    override fun convert(holder: BaseViewHolder, item: FolderBean) {
        holder.setText(R.id.text, "房间名称：${item.folderName}")

        val adapter = SceneSelectDeviceRoomItemAdapter(item.deviceList as MutableList<DeviceNewBean>) { protocol, bean ->  gotoNext.invoke(protocol, bean) }
        val list = holder.getView<RecyclerView>(R.id.list_device)
        list.adapter = adapter
    }
}