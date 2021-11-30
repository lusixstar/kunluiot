package com.example.kiotsdk.adapter.operation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean


class OperationListAdapter(list: MutableList<DeviceOperationProtocolBean>) : BaseQuickAdapter<DeviceOperationProtocolBean, BaseViewHolder>(R.layout.item_operation_list, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceOperationProtocolBean) {
        holder.setText(R.id.text, item.desc)
    }
}