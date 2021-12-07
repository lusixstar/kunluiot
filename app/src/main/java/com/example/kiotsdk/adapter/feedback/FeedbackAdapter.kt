package com.example.kiotsdk.adapter.feedback

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.bean.FeedbackImgBean


class FeedbackAdapter(list: MutableList<FeedbackImgBean>) : BaseQuickAdapter<FeedbackImgBean, BaseViewHolder>(R.layout.item_feedback_list, list) {

    override fun convert(holder: BaseViewHolder, item: FeedbackImgBean) {
        if (item.type == "add") {
            holder.setVisible(R.id.cancel, false)
            holder.setImageResource(R.id.img, R.mipmap.ic_add_img)
        } else {
            holder.setVisible(R.id.cancel, true)
            val img = holder.getView<AppCompatImageView>(R.id.img)
            Glide.with(holder.itemView.context).load(item.url).centerCrop().into(img)
        }
    }
}