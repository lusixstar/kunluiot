package com.example.kiotsdk.adapter.family

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.family.FolderBean


class FamilyRoomListAdapter(list: MutableList<FolderBean>, private val move: (DeviceNewBean) -> Unit) : BaseQuickAdapter<FolderBean, BaseViewHolder>(R.layout.item_family_room_list, list) {

    override fun convert(holder: BaseViewHolder, item: FolderBean) {
        holder.setText(R.id.name, item.folderName)
        var num = 0
        if (!item.deviceList.isNullOrEmpty()) {
            num = item.deviceList.size
        }
        holder.setText(R.id.sub, "$num 个设备")
        if (item.folderName != "root") {
            holder.setGone(R.id.img, false)
        } else {
            holder.setGone(R.id.img, true)
        }

        val list = holder.getView<RecyclerView>(R.id.device_list)
        val adapter = FamilyRoomDeviceListAdapter(item.deviceList.toMutableList())
        adapter.addChildClickViewIds(R.id.move)
        (list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        list.adapter = adapter
        adapter.setOnItemChildClickListener { ada, view, position ->
            val bean = ada.getItem(position) as DeviceNewBean
            if (view.id == R.id.move) {
                move.invoke(bean)
            }
        }
    }
}