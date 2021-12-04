package com.example.kiotsdk.adapter.operation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceOperationFieldsBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean


class OperationFieldsListAdapter(list: MutableList<DeviceOperationFieldsBean>) : BaseQuickAdapter<DeviceOperationFieldsBean, BaseViewHolder>(R.layout.item_operation_fields_list, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceOperationFieldsBean) {
        holder.setText(R.id.text, item.desc)
    }
}