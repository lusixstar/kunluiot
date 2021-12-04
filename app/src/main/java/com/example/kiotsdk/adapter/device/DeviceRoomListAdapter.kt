package com.example.kiotsdk.adapter.device

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.family.FolderBean


class DeviceRoomListAdapter(list: MutableList<FolderBean>, private val delete: (DeviceNewBean) -> Unit) : BaseQuickAdapter<FolderBean, BaseViewHolder>(R.layout.item_room_list, list) {

    override fun convert(holder: BaseViewHolder, item: FolderBean) {
        holder.setText(R.id.text, "房间名称：${item.folderName}")

        val adapter = DeviceRoomItemAdapter(item.deviceList as MutableList<DeviceNewBean>)
        val list = holder.getView<RecyclerView>(R.id.list_device)
        list.adapter = adapter

        adapter.setOnItemClickListener { ada, _, position ->
            val bean = ada.getItem(position) as DeviceNewBean
            delete(bean)
        }
    }
}