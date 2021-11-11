package com.example.kiotsdk.adapter.family

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.family.FamilyCreateBean


class FamilyListAdapter(list : MutableList<FamilyCreateBean>) : BaseQuickAdapter<FamilyCreateBean, BaseViewHolder>(R.layout.item_family_list, list) {

    override fun convert(holder: BaseViewHolder, item: FamilyCreateBean) {
        holder.setText(R.id.text, item.familyName)

        if (item.current) {
            holder.setGone(R.id.img, false)
        } else {
            holder.setGone(R.id.img, true)
        }
    }
}