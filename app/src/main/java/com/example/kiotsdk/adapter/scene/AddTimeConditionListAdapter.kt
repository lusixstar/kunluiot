package com.example.kiotsdk.adapter.scene

import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.kiotsdk.R
import com.example.kiotsdk.bean.AddTimeConditionBean
import java.util.*


class AddTimeConditionListAdapter(list: MutableList<AddTimeConditionBean>) : BaseQuickAdapter<AddTimeConditionBean, BaseViewHolder>(R.layout.item_add_time_condition, list) {

    override fun convert(holder: BaseViewHolder, item: AddTimeConditionBean) {

        val cl = holder.getView<ConstraintLayout>(R.id.item)
        if (item.select) {
            cl.setBackgroundResource(R.drawable.circular_blue)
        } else {
            cl.setBackgroundResource(R.drawable.circular_gary)
        }
        holder.setText(R.id.tv_time, item.name)
    }

    /**
     * 每天
     */
    fun selectEveryDay() {
        for (i in 0 until data.size) {
            data[i].select = true
        }
        notifyItemRangeChanged(0, data.size)
    }

    fun selectWorkingDay() {
        cancelSelect()
        for (i in 0..4) {
            data[i].select = true
        }
        notifyItemRangeChanged(0, data.size)
    }

    fun selectWeekend() {
        cancelSelect()
        for (i in 5..6) {
            data[i].select = true
        }
        notifyItemRangeChanged(0, data.size)
    }

    fun selectCustomDay() {
        cancelSelect()
        notifyItemRangeChanged(0, data.size)
    }

    private fun cancelSelect() {
        for (i in 0 until data.size) {
            data[i].select = false
        }
    }

    fun getSelectAllEngName(): String {
        var selectName = ""
        for (i in 0 until data.size) {
            if (data[i].select) {
                if (selectName.isNotEmpty()) {
                    selectName = "$selectName,"
                }
                selectName += getDayEngName(data[i].id)
            }
        }
        return selectName
    }

    private fun getDayEngName(id: Int): String? {
        val map: MutableMap<Int, String> = HashMap()
        map[1] = "MON"
        map[2] = "TUE"
        map[3] = "WED"
        map[4] = "THU"
        map[5] = "FRI"
        map[6] = "SAT"
        map[7] = "SUN"
        return map[id]
    }

    fun getSelectAllName(): String {
        var selectName = ""
        for (i in 0 until data.size) {
            if (data[i].select) {
                if (selectName.isNotEmpty()) {
                    selectName = "$selectName,"
                }
                selectName += getDayName(data[i].id)
            }
        }
        return selectName
    }

    private fun getDayName(id: Int): String? {
        val map: MutableMap<Int, String> = HashMap()
        map[1] = "周一"
        map[2] = "周二"
        map[3] = "周三"
        map[4] = "周四"
        map[5] = "周五"
        map[6] = "周六"
        map[7] = "周日"
        return map[id]
    }
}