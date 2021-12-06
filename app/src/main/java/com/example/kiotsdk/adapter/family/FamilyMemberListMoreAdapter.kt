package com.example.kiotsdk.adapter.family

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.kunluiot.sdk.bean.family.FamilyMemberMapBean


class FamilyMemberListMoreAdapter(list: MutableList<FamilyMemberMapBean>) : BaseQuickAdapter<FamilyMemberMapBean, BaseViewHolder>(R.layout.item_family_member_more_list, list) {

    override fun convert(holder: BaseViewHolder, item: FamilyMemberMapBean) {
        holder.setText(R.id.name, item.name)

        if (item.gender.isNotEmpty()) {
            when (item.gender) {
                "add" -> holder.setImageResource(R.id.img, R.mipmap.ic_family_details_add)
                "man" -> holder.setImageResource(R.id.img, R.mipmap.ic_member_man)
                "woman" -> holder.setImageResource(R.id.img, R.mipmap.ic_member_woman)
                "children" -> holder.setImageResource(R.id.img, R.mipmap.ic_member_children)
                else -> holder.setImageResource(R.id.img, R.mipmap.ic_default_head)
            }
        } else {
            holder.setImageResource(R.id.img, R.mipmap.ic_default_head)
        }
    }
}