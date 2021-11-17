package com.example.kiotsdk.adapter.device

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.ui.device.DeviceGateWayActivity
import com.example.kiotsdk.ui.device.DeviceListActivity
import com.example.kiotsdk.ui.device.DeviceSetWifiActivity
import com.kunluiot.sdk.bean.device.DeviceListProductBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import org.jetbrains.anko.startActivity


class DeviceProductListAdapter(list: MutableList<DeviceListProductBean>) : BaseQuickAdapter<DeviceListProductBean, BaseViewHolder>(R.layout.item_device_product_list, list) {

    private var mApModel = false

    override fun convert(holder: BaseViewHolder, item: DeviceListProductBean) {
        holder.setText(R.id.text, item.categorySelfName)

        val listView = holder.getView<RecyclerView>(R.id.item_list)
        val manager = GridLayoutManager(listView.context, 4)
        val adapter = DeviceProductItemAdapter(mutableListOf())
        listView.layoutManager = manager
        listView.adapter = adapter
        adapter.data.clear()
        adapter.addData(item.products)

        adapter.setOnItemClickListener { ada, _, position ->
            val bean = ada.data[position] as DeviceProductsBean
            if (bean.bindType == DeviceListActivity.NET_TYPE_WIFI) {
                listView.context.startActivity<DeviceSetWifiActivity>(DeviceSetWifiActivity.BEAN to bean, DeviceSetWifiActivity.NET_TYPE_AP to mApModel)
            } else {
                listView.context.startActivity<DeviceGateWayActivity>(DeviceGateWayActivity.BEAN to bean)
            }
        }
    }

    fun setApType(model: Boolean) {
        mApModel = model
    }
}