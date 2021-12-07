package com.example.kiotsdk.adapter.msg

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.ui.msg.MsgListActivity
import com.kunluiot.sdk.bean.common.CommonMsgContentBean


class MsgListAdapter(list: MutableList<CommonMsgContentBean>) : BaseQuickAdapter<CommonMsgContentBean, BaseViewHolder>(R.layout.item_msg_list, list) {

    private var mType = ""

    override fun convert(holder: BaseViewHolder, item: CommonMsgContentBean) {
        holder.setText(R.id.text, if (mType == MsgListActivity.TYPE_DEVICE) item.subject else item.title)
        holder.setText(R.id.sub, item.content)
        holder.setVisible(R.id.red, !item.isRead)
        holder.setTextColor(R.id.sub, if (item.isRead) Color.parseColor("#B3B3B3") else Color.parseColor("#c11920"))
    }

    fun setType(type: String) {
        mType = type
    }
}