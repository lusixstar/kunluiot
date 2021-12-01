package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.scene.SceneDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneLinkedEditOrAddBinding
import com.kunluiot.sdk.bean.scene.AddTimeConditionEvent
import com.kunluiot.sdk.bean.scene.SceneLinkBean

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneLinkedAddOrEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneLinkedEditOrAddBinding

    private var mIsAdd = false
    private var mBean: SceneLinkBean = SceneLinkBean()

    private var mSceneName = ""

    //判断触发类型 定时触发：SCHEDULER  设备触发：REPORT
    private var mTriggerType = ""
    private var selectedWeekDay = ""


    private lateinit var mAdapter: SceneDeviceListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneLinkedEditOrAddBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        getIntentData()

        mBinding.titleLayout.setOnClickListener { gotoEditName.launch(Intent(this, SceneNameEditActivity::class.java).putExtra(SceneNameEditActivity.NAME, mSceneName)) }
        mBinding.addCondition.setOnClickListener { gotoAddCondition.launch(Intent(this, SceneAddConditionActivity::class.java)) }
        mBinding.timeLayout.setOnClickListener { gotoTime.launch(Intent(this, SceneAddTimeConditionActivity::class.java)) }
        mBinding.dateLayout.setOnClickListener { gotoDate.launch(Intent(this, SceneAddTimeConditionActivity::class.java).putExtra(SceneAddTimeConditionActivity.TIME_SLOT, true)) }
    }

    private val gotoEditName = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            mSceneName = it.data?.getStringExtra(SceneNameEditActivity.NAME) ?: ""
            mBinding.titleValue.text = "${mSceneName}"
        }
    }

    private val gotoTime = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bean = it.data?.getParcelableExtra(SceneAddTimeConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
            mTriggerType = bean.triggerType ?: ""
            selectedWeekDay = ""
            val timeTrigger = if (mTriggerType == "SCHEDULER") bean.conditionBean?.conDesc else ""
            mBinding.timeValue.text = timeTrigger
        }
    }

    private val gotoDate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bean = it.data?.getParcelableExtra(SceneAddTimeConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
            mTriggerType = bean.triggerType ?: ""
            selectedWeekDay = ""
            val deviceTrigger = if (mTriggerType == "SCHEDULER") "" else bean.desc
            mBinding.dateValue.text = deviceTrigger
        }
    }

    private val gotoAddCondition = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val time = it.data?.getStringExtra(SceneAddConditionActivity.TIME) ?: ""
            if (time == SceneAddConditionActivity.TIME) {
                val bean = it.data?.getParcelableExtra(SceneAddConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
                mTriggerType = bean.triggerType ?: ""
                selectedWeekDay = ""
                val timeTrigger = if (mTriggerType == "SCHEDULER") bean.conditionBean?.conDesc else ""
                val deviceTrigger = if (mTriggerType == "SCHEDULER") "" else bean.desc
                mBinding.timeValue.text = timeTrigger
                XLog.e("bean == $bean")
                XLog.e("timeTrigger == $timeTrigger")
                XLog.e("deviceTrigger == $deviceTrigger")
            }
        }
    }

    private fun getIntentData() {
        intent?.let {
            mIsAdd = it.getBooleanExtra(IS_ADD, false)
            if (!mIsAdd) mBean = it.getParcelableExtra(BEAN) ?: SceneLinkBean()
            if (mIsAdd) mBinding.toolBar.title = "创建${resources.getString(R.string.scene_linked)}${resources.getString(R.string.scene)}"
            else mBinding.toolBar.title = "编辑${resources.getString(R.string.scene_linked)}${resources.getString(R.string.scene)}"
            if (!mIsAdd) {
                mSceneName = mBean.ruleName
                mTriggerType = mBean.triggerType
                mBinding.titleValue.text = "$mSceneName   >"
                val timeTrigger = if (mTriggerType == "SCHEDULER") mBean.conditionList.first().conDesc else ""
                val deviceTrigger = if (mTriggerType == "SCHEDULER") "" else mBean.desc

            }
        }
    }

    companion object {
        const val IS_ADD = "is_add"
        const val BEAN = "bean"
    }
}