package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.scene.AddTimeConditionListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.bean.AddTimeConditionBean
import com.example.kiotsdk.databinding.ActivitySceneAddTimeConditionEditBinding
import com.example.kiotsdk.util.DemoUtils
import com.example.kiotsdk.widget.SelectHourDialog
import com.example.kiotsdk.widget.SelectTimeDialog
import com.kunluiot.sdk.bean.scene.AddTimeConditionEvent
import com.kunluiot.sdk.bean.scene.SceneCustomFieldsBeanNew
import com.kunluiot.sdk.bean.scene.SceneTriggerBeanNew
import com.kunluiot.sdk.bean.scene.TimeConditionBean
import java.util.*

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneAddTimeConditionActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneAddTimeConditionEditBinding

    private lateinit var mAdapter: AddTimeConditionListAdapter

    private var mStartHour = ""
    private var mStartMinute = ""
    private var mEndHour: String = ""
    private var mEndMinute: String = ""
    private var mTime = ""

    private var mSelectType = 0

    //是否是选择时间段的标志
    private var mIsTimeSlot = false

    private var mTimeList: MutableList<TimeConditionBean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneAddTimeConditionEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mIsTimeSlot = it.getBooleanExtra(TIME_SLOT, false)
            val time = it.getStringExtra(TIME_SELECT) ?: ""
            if (time.isNotEmpty() && time.contains(":") && !mIsTimeSlot) {
                mStartHour = time.substring(0, 2)
                mStartMinute = time.substring(3, 5)
                mTime = "$mStartHour:$mStartMinute"
                mBinding.timeValue.text = mTime
                mTimeList.clear()
                mTimeList.add(TimeConditionBean(true, mTime))
            }
            if (time.isNotEmpty() && time.contains(":") && mIsTimeSlot) {
                when {
                    time.contains("到第二天") && time.length >= 14 -> {
                        mStartHour = time.substring(0, 2)
                        mStartMinute = time.substring(3, 5)
                        mEndHour = time.substring(9, 11)
                        mEndMinute = time.substring(12, 14)
                        mTime = "$mStartHour:$mStartMinute - $mEndHour:$mEndMinute"
                        mBinding.timeValue.text = mTime
                        mTimeList.clear()
                        mTimeList.add(TimeConditionBean(true, mTime))
                    }
                    time.contains("到") && time.length >= 11 -> {
                        mStartHour = time.substring(0, 2)
                        mStartMinute = time.substring(3, 5)
                        mEndHour = time.substring(6, 8)
                        mEndMinute = time.substring(9, 11)
                        mTime = "$mStartHour:$mStartMinute - $mEndHour:$mEndMinute"
                        mBinding.timeValue.text = mTime
                        mTimeList.clear()
                        mTimeList.add(TimeConditionBean(true, mTime))
                    }
                    time.length >= 10 -> {
                        mStartHour = time.substring(0, 2)
                        mStartMinute = time.substring(3, 5)
                        mEndHour = time.substring(5, 7)
                        mEndMinute = time.substring(8, 10)
                        mTime = "$mStartHour:$mStartMinute - $mEndHour:$mEndMinute"
                        mBinding.timeValue.text = mTime
                        mTimeList.clear()
                        mTimeList.add(TimeConditionBean(true, mTime))
                    }
                }
            }
        }

        initView()
        initAdapter()
    }

    private fun initAdapter() {
        val list = mutableListOf<AddTimeConditionBean>()
        list.add(AddTimeConditionBean(id = 1, name = "一"))
        list.add(AddTimeConditionBean(id = 2, name = "二"))
        list.add(AddTimeConditionBean(id = 3, name = "三"))
        list.add(AddTimeConditionBean(id = 4, name = "四"))
        list.add(AddTimeConditionBean(id = 5, name = "五"))
        list.add(AddTimeConditionBean(id = 6, name = "六"))
        list.add(AddTimeConditionBean(id = 7, name = "七"))
        mAdapter = AddTimeConditionListAdapter(list)
        (mBinding.dateList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        val layoutManager = GridLayoutManager(this, 7)
        mBinding.dateList.layoutManager = layoutManager
        mBinding.dateList.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            if (mSelectType == 3) {
                mAdapter.data[position].select = !mAdapter.data[position].select
                adapter.notifyItemChanged(position)
            }
        }
        selectDay(0)
    }

    private fun initView() {
        if (mBinding.timeValue.text.isEmpty()) mBinding.timeValue.text = resources.getString(R.string.please_select_the_time_)
        mBinding.timeLayout.setOnClickListener { if (mIsTimeSlot) showTimeSlotBottomDialog() else showTimeBottomDialog() }
        mBinding.everydayLayout.setOnClickListener { selectDay(mBinding.everydayNext) }
        mBinding.workingDayLayout.setOnClickListener { selectDay(mBinding.workingDayNext) }
        mBinding.weekendLayout.setOnClickListener { selectDay(mBinding.weekendNext) }
        mBinding.customLayout.setOnClickListener { selectDay(mBinding.customNext) }
        mBinding.next.setOnClickListener { addTimeCondition() }
    }

    private fun addTimeCondition() {
        if (mTime.isEmpty()) {
            toastMsg("time is empty")
            return
        }
        if (getWeekName().isEmpty()) {
            toastMsg(resources.getString(R.string.please_select_duplicate_setting))
            return
        }
        val bean = AddTimeConditionEvent()
        bean.triggerType = if (mIsTimeSlot) "REPORT" else "SCHEDULER"

        if (mIsTimeSlot) {
            val cronExprHour = if (Integer.valueOf(mEndHour) == 0) Integer.valueOf(mStartHour).toString() + "-23" else Integer.valueOf(mStartHour).toString() + "-" + (Integer.valueOf(mEndHour) - 1)
            val cronExpr = "0-59 0-59 " + cronExprHour + " ? * " + mAdapter.getSelectAllEngName() + " *"
            mStartMinute = "00"
            mEndMinute = "00"
            val desc: String = if (Integer.valueOf(mStartHour) >= Integer.valueOf(mEndHour)) {
                if (Integer.valueOf(mStartHour) == 0 && Integer.valueOf(mEndHour) == 0) {
                    if (getWeekName() == getString(R.string.every_day)) {
                        getString(R.string.all_day)
                    } else {
                        getString(R.string.all_day) + " (" + getWeekName() + ")"
                    }
                } else {
                    mStartHour + ":" + mStartMinute + getString(R.string.by_the_next_day) + mEndHour + ":" + mEndMinute + " (" + getWeekName() + ")"
                }
            } else {
                mStartHour + ":" + mStartMinute + getString(R.string.reach) + mEndHour + ":" + mEndMinute + " (" + getWeekName() + ")"
            }
            bean.cronExpr = cronExpr
            bean.desc = desc
        } else {
            val rightContent = "00 " + mStartMinute + " " + mStartHour + " ? * " + mAdapter.getSelectAllEngName() + " *"
            var conDesc = ""
            conDesc = conDesc + mStartHour + ":" + mStartMinute + " (" + getWeekName() + ")"

            bean.conDesc = conDesc
            bean.devTid = "time"
            bean.ctrlKey = "time"
            bean.cronExpr = ""

            val triggerParams = ArrayList<SceneTriggerBeanNew>()
            val triggerParamBean = SceneTriggerBeanNew()
            triggerParamBean.left = "cloudTime"
            triggerParamBean.right = rightContent
            triggerParamBean.operator = "=="
            triggerParams.add(triggerParamBean)

            bean.triggerParams = triggerParams

            val customFields = SceneCustomFieldsBeanNew()
            customFields.name = "定时触发"
            customFields.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_delay)
            bean.customFields = customFields

            bean.desc = getString(R.string.all_day)
        }

        setResult(Activity.RESULT_OK, intent.putExtra(TIME_BEAN, bean))
        finish()
    }

    private fun getWeekName(): String {
        var conDesc = ""
        conDesc = when {
            mAdapter.getSelectAllName() == "周一,周二,周三,周四,周五,周六,周日" -> {
                conDesc + getString(R.string.every_day)
            }
            mAdapter.getSelectAllName() == "周一,周二,周三,周四,周五" -> {
                conDesc + getString(R.string.working_day)
            }
            mAdapter.getSelectAllName() == "周六,周日" -> {
                conDesc + getString(R.string.rest_day)
            }
            else -> {
                conDesc + mAdapter.getSelectAllName()
            }
        }
        return conDesc
    }

    private fun selectDay(img: AppCompatImageView) {
        mBinding.everydayNext.visibility = View.INVISIBLE
        mBinding.workingDayNext.visibility = View.INVISIBLE
        mBinding.weekendNext.visibility = View.INVISIBLE
        mBinding.customNext.visibility = View.INVISIBLE
        img.visibility = View.VISIBLE
        when (img.id) {
            mBinding.everydayNext.id -> selectDay(0)
            mBinding.workingDayNext.id -> selectDay(1)
            mBinding.weekendNext.id -> selectDay(2)
            mBinding.customNext.id -> selectDay(3)
        }
    }

    private fun selectDay(index: Int) {
        mSelectType = index
        when (index) {
            0 -> mAdapter.selectEveryDay()
            1 -> mAdapter.selectWorkingDay()
            2 -> mAdapter.selectWeekend()
            3 -> mAdapter.selectCustomDay()
        }
    }

    private fun showTimeSlotBottomDialog() {
        val dialog = SelectHourDialog(this)
        dialog.show()
        dialog.setTitleName(getString(R.string.select_the_time))
        dialog.setStartHour("$mStartHour:$mStartMinute")
        dialog.setEndHour("$mEndHour:$mEndMinute")
        dialog.setOnDialogClickListener(object : SelectHourDialog.OnDialogClickListener {
            override fun onFinishClick(startHour: String, endHour: String) {
                mStartHour = startHour.substring(0, 2)
                mStartMinute = "00"
                mEndHour = endHour.substring(0, 2)
                mEndMinute = "00"
                mTime = "$mStartHour:$mStartMinute - $mEndHour:$mEndMinute"
                mBinding.timeValue.text = mTime
                mTimeList.clear()
                mTimeList.add(TimeConditionBean(true, mTime))
            }

            override fun onDismissClick() {

            }
        })
    }

    private fun showTimeBottomDialog() {
        val minutes = ArrayList<String>()
        val seconds = ArrayList<String>()
        for (i in 0..23) {
            minutes.add(if (i < 10) "0$i" else "" + i)
        }
        for (i in 0..59) {
            seconds.add(if (i < 10) "0$i" else "" + i)
        }
        val dialog = SelectTimeDialog(this)
        dialog.show()
        val current: String = mBinding.timeValue.text.toString()
        dialog.setMinuteData(minutes)
        dialog.setSecondData(seconds)
        if (current.isNotEmpty() && current.contains(":")) {
            val times = current.split(":").toTypedArray()
            dialog.setMinute(times[0])
            dialog.setSecond(times[1])
        } else {
            dialog.setMinute("00")
            dialog.setSecond("00")
        }
        dialog.setTitleName(resources.getString(R.string.select_the_time))
        dialog.setOnDialogClickListener(object : SelectTimeDialog.OnDialogClickListener {
            override fun onFinishClick(hour: String, minute: String) {
                mStartHour = hour
                mStartMinute = minute
                mTime = "$mStartHour:$mStartMinute"
                mBinding.timeValue.text = mTime
                mTimeList.clear()
                mTimeList.add(TimeConditionBean(true, mTime))
            }

            override fun onDismissClick() {}
        })
    }

    companion object {
        const val TIME_BEAN = "time_bean"
        const val TIME_SLOT = "time_slot"
        const val TIME_SELECT = "time_select"
    }
}