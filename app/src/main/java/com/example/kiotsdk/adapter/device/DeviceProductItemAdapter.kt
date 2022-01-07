package com.example.kiotsdk.adapter.device

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.device.DeviceProductsBean


class DeviceProductItemAdapter(list: MutableList<DeviceProductsBean>) : BaseQuickAdapter<DeviceProductsBean, BaseViewHolder>(R.layout.item_device_product_sub_item, list) {

    override fun convert(holder: BaseViewHolder, item: DeviceProductsBean) {
        holder.setText(R.id.text, item.name)

        val img = holder.getView<ImageView>(R.id.img)
        img.load(item.logo) {
            transformations(CircleCropTransformation())
        }
    }
}