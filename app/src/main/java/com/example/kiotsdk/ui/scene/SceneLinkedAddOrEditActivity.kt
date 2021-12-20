package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffSceneIftttTasksListCallback
import com.example.kiotsdk.adapter.diff.DiffSceneLinkedConditionListCallback
import com.example.kiotsdk.adapter.scene.SceneConditionListAdapter
import com.example.kiotsdk.adapter.scene.SceneDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.bean.TriggerModeBean
import com.example.kiotsdk.databinding.ActivitySceneLinkedEditOrAddBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.AddTimeConditionEvent
import com.kunluiot.sdk.bean.scene.SceneConditionListBeanNew
import com.kunluiot.sdk.bean.scene.SceneIftttTasksListBeanNew
import com.kunluiot.sdk.bean.scene.SceneLinkBeanNew
import org.jetbrains.anko.alert
import org.jetbrains.anko.selector

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneLinkedAddOrEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneLinkedEditOrAddBinding

    private var mIsAdd = false
    private var mBean: SceneLinkBeanNew = SceneLinkBeanNew()

    private var mSceneName = ""

    //判断触发类型 定时触发：SCHEDULER  设备触发：REPORT
    private var mTriggerType = ""

    //选择触发方式选中数据
    private var mCurrTriggerMode: TriggerModeBean = TriggerModeBean()

    private lateinit var mAdapter: SceneDeviceListAdapter
    private lateinit var mConditionAdapter: SceneConditionListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneLinkedEditOrAddBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        getIntentData()

        mBinding.titleLayout.setOnClickListener { gotoEditName.launch(Intent(this, SceneNameEditActivity::class.java).putExtra(SceneNameEditActivity.NAME, mSceneName)) }
        mBinding.timeLayout.setOnClickListener {
            gotoTime.launch(Intent(this, SceneAddTimeConditionActivity::class.java).putExtra(SceneAddTimeConditionActivity.TIME_SELECT, mBinding.timeValue.text))
        }
        mBinding.dateLayout.setOnClickListener { gotoDate.launch(Intent(this, SceneAddTimeConditionActivity::class.java).putExtra(SceneAddTimeConditionActivity.TIME_SLOT, true)) }
        mBinding.modeLayout.setOnClickListener { showTriggerModeDialog() }
        mBinding.addCondition.setOnClickListener { clickAddCondition() }
        mBinding.addDevices.setOnClickListener { gotoAddDevices.launch(Intent(this, SelectExecutionActionActivity::class.java)) }
        mBinding.next.setOnClickListener { gotoNext() }

        initAdapter()
    }

    private fun gotoNext() {
        if (mBean.ruleName.isEmpty()) {
            toastMsg("name is empty")
            return
        }
        if (mTriggerType == "REPORT") {
            if (mBean.cronExpr.isEmpty()) {
                toastMsg("请选择生效时间段")
                return
            }
        }
        if (mBean.conditionList.isNullOrEmpty()) {
            toastMsg("请添加条件")
            return
        }
        if (mAdapter.data.isNullOrEmpty()) {
            toastMsg("请添加执行设备")
            return
        }
        if (mIsAdd) {
            KunLuHomeSdk.sceneImpl.addLinkageScene(mBean, { code, msg -> toastErrorMsg(code, msg) }, {
                toastMsg("success")
                setResult(Activity.RESULT_OK)
                finish()
            })
        } else {
            KunLuHomeSdk.sceneImpl.updateLinkageScene(mBean.ruleId, mBean.enabled, mBean, { code, msg -> toastErrorMsg(code, msg) }, {
                toastMsg("success")
                setResult(Activity.RESULT_OK)
                finish()
            })
        }
    }

    private fun initAdapter() {
        mAdapter = SceneDeviceListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffSceneIftttTasksListCallback())
        mBinding.deviceList.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            alert("是否删除") {
                positiveButton("确定") { dialog ->
                    mAdapter.removeAt(position)
                    mBean.iftttTasks = mAdapter.data
                    dialog.dismiss()
                }
            }.show()
        }
        mConditionAdapter = SceneConditionListAdapter(arrayListOf())
        mConditionAdapter.setDiffCallback(DiffSceneLinkedConditionListCallback())
        mBinding.conditionList.adapter = mConditionAdapter
        mConditionAdapter.setOnItemClickListener { _, _, position ->
            alert("是否删除") {
                positiveButton("确定") { dialog ->
                    mConditionAdapter.removeAt(position)
                    if (mConditionAdapter.data.isEmpty()) mTriggerType = ""
                    mBean.conditionList = mConditionAdapter.data
                    dialog.dismiss()
                }
            }.show()
        }
        if (!mBean.conditionList.isNullOrEmpty()) mConditionAdapter.setDiffNewData(mBean.conditionList.toMutableList())
        if (!mBean.iftttTasks.isNullOrEmpty()) mAdapter.setDiffNewData(mBean.iftttTasks.toMutableList())
    }

    private val gotoAddDevices = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val delay = it.data?.getStringExtra(SelectExecutionActionActivity.DELAY) ?: ""
            if (delay.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.DELAY_BEAN) ?: SceneIftttTasksListBeanNew()
                mAdapter.data.add(mAdapter.data.size, bean)
                mAdapter.notifyItemInserted(mAdapter.data.size)
                mBean.iftttTasks = mAdapter.data
            }
            val scene = it.data?.getStringExtra(SelectExecutionActionActivity.SCENE_LINK) ?: ""
            if (scene.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.SCENE_LINK_BEAN) ?: SceneIftttTasksListBeanNew()
                mAdapter.data.add(mAdapter.data.size, bean)
                mAdapter.notifyItemInserted(mAdapter.data.size)
                mBean.iftttTasks = mAdapter.data
            }
            val device = it.data?.getStringExtra(SelectExecutionActionActivity.DEVICE) ?: ""
            if (device.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.DEVICE_BEAN) ?: SceneIftttTasksListBeanNew()
                mAdapter.data.add(mAdapter.data.size, bean)
                mAdapter.notifyItemInserted(mAdapter.data.size)
                mBean.iftttTasks = mAdapter.data
            }
        }
    }

    private fun clickAddCondition() {
        if (mTriggerType.isEmpty()) {
            gotoAddCondition.launch(Intent(this, SceneAddConditionActivity::class.java))
        } else if (mTriggerType == "REPORT") {
            val keyList = mConditionAdapter.data.map { it.customFields.name }.toTypedArray()
            gotoAddConditionDevice.launch(Intent(this, SceneSelectConditionDevicesActivity::class.java).putExtra(SceneSelectConditionDevicesActivity.HAS_ADD_LIST, keyList))
        }
    }

    private val gotoAddConditionDevice = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val device = it.data?.getStringExtra(SceneAddConditionActivity.DEVICE) ?: ""
            if (device == SceneAddConditionActivity.DEVICE) {
                val bean = it.data?.getParcelableExtra(SceneAddConditionActivity.DEVICE_BEAN) ?: SceneConditionListBeanNew()
                mConditionAdapter.data.add(mConditionAdapter.data.size, bean)
                mConditionAdapter.notifyItemInserted(mConditionAdapter.data.size)
                mBean.conditionList = mConditionAdapter.data
            }
        }
    }

    private fun showTriggerModeDialog() {
        val listData = mutableListOf<TriggerModeBean>()
        listData.add(TriggerModeBean("OR", getString(R.string.any_of_the_following_conditions_are_satisfied), false))
        listData.add(TriggerModeBean("AND", getString(R.string.all_of_the_following_conditions_are_met), false))
        val names = listOf(getString(R.string.any_of_the_following_conditions_are_satisfied), getString(R.string.all_of_the_following_conditions_are_met))
        selector(getString(R.string.trigger_mode), names) { dialog, i ->
            val bean = listData[i]
            bean.select = true
            mCurrTriggerMode = bean
            mBinding.modeValue.text = bean.value
            mBean.conditionLogic = bean.key
            dialog.dismiss()
        }
    }

    private val gotoEditName = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            mSceneName = it.data?.getStringExtra(SceneNameEditActivity.NAME) ?: ""
            mBinding.titleValue.text = "${mSceneName}"
            mBean.ruleName = mSceneName
        }
    }

    private val gotoTime = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bean = it.data?.getParcelableExtra(SceneAddTimeConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
            mTriggerType = bean.triggerType ?: ""
            val timeTrigger = if (mTriggerType == "SCHEDULER") bean.conDesc else ""
            mBinding.timeValue.text = timeTrigger
            if (!mBean.conditionList.isNullOrEmpty()) {
                mBean.conditionList.first().conDesc = bean.conDesc ?: ""
                mBean.conditionList.first().triggerParams.first().let { mstp ->
                    bean.triggerParams?.first().let { stp ->
                        mstp.left = stp?.left ?: ""
                        mstp.right = stp?.right ?: ""
                        mstp.operator = stp?.operator ?: ""
                    }
                }
            }
        }
    }

    private val gotoDate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bean = it.data?.getParcelableExtra(SceneAddTimeConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
            mTriggerType = bean.triggerType ?: ""
            mBean.triggerType = mTriggerType
            val deviceTrigger = if (mTriggerType == "SCHEDULER") "" else bean.desc
            mBinding.dateValue.text = deviceTrigger
            bean.desc?.let { desc -> mBean.desc = desc }
            bean.cronExpr?.let { cronExpr -> mBean.cronExpr = cronExpr }
        }
    }

    private val gotoAddCondition = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val time = it.data?.getStringExtra(SceneAddConditionActivity.TIME) ?: ""
            if (time == SceneAddConditionActivity.TIME) {
                val bean = it.data?.getParcelableExtra(SceneAddConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
                mTriggerType = bean.triggerType ?: ""
                mBean.triggerType = mTriggerType
                val timeTrigger = if (mTriggerType == "SCHEDULER") bean.conDesc else ""
                mBinding.timeLayout.visibility = View.VISIBLE
                mBinding.timeValue.text = timeTrigger
                mBinding.addCondition.visibility = View.GONE
                mBinding.conditionHint.visibility = View.GONE
                mBinding.conditionList.visibility = View.GONE
                bean.desc?.let { desc -> mBean.desc = desc }
                bean.cronExpr?.let { mBean.cronExpr = bean.cronExpr!! }
                val condListBean = SceneConditionListBeanNew()
                bean.conDesc?.let { condListBean.conDesc = bean.conDesc!! }
                bean.devTid?.let { condListBean.devTid = bean.devTid!! }
                bean.ctrlKey?.let { condListBean.ctrlKey = bean.ctrlKey!! }
                bean.desc?.let { condListBean.conDesc = bean.conDesc!! }
                bean.triggerParams?.let { condListBean.triggerParams = bean.triggerParams!! }
                bean.customFields?.let { condListBean.customFields = bean.customFields!! }
                mBean.conditionList = listOf(condListBean)
            }
            val device = it.data?.getStringExtra(SceneAddConditionActivity.DEVICE) ?: ""
            if (device == SceneAddConditionActivity.DEVICE) {
                val bean = it.data?.getParcelableExtra(SceneAddConditionActivity.DEVICE_BEAN) ?: SceneConditionListBeanNew()
                mTriggerType = "REPORT"
                mBinding.timeLayout.visibility = View.GONE
                mBinding.dateLayout.visibility = View.VISIBLE
                mBinding.dateValue.text = getString(R.string.all_day)
                mBinding.modeLayout.visibility = View.VISIBLE
                mBinding.modeValue.text = getString(R.string.any_of_the_following_conditions_are_satisfied)
                mBinding.conditionHint.visibility = View.VISIBLE
                mBinding.conditionList.visibility = View.VISIBLE
                mBean.triggerType = mTriggerType
                if (mBean.cronExpr.isEmpty()) {
                    mBean.cronExpr = "0-59 0-59 0-23 ? * MON,TUE,WED,THU,FRI,SAT,SUN *"
                    mBean.desc = getString(R.string.all_day)
                }
                mConditionAdapter.data.add(mConditionAdapter.data.size, bean)
                mConditionAdapter.notifyItemInserted(mConditionAdapter.data.size)
                mBean.conditionList = mConditionAdapter.data
            }
        }
    }

    private fun getIntentData() {
        intent?.let {
            mIsAdd = it.getBooleanExtra(IS_ADD, false)
            if (!mIsAdd) mBean = it.getParcelableExtra(BEAN) ?: SceneLinkBeanNew()
            if (mIsAdd) {
                mBinding.toolBar.title = "创建${resources.getString(R.string.scene_linked)}${resources.getString(R.string.scene)}"
                mBinding.titleValue.text = "请输入场景名称"
            } else {
                mBinding.toolBar.title = "编辑${resources.getString(R.string.scene_linked)}${resources.getString(R.string.scene)}"
            }
            if (!mIsAdd) {
                mSceneName = mBean.ruleName
                mTriggerType = mBean.triggerType
                mBinding.titleValue.text = "$mSceneName"
                val deviceTrigger = if (mTriggerType == "SCHEDULER") "" else mBean.desc
                if (deviceTrigger.isNotEmpty()) {
                    mBinding.dateValue.text = deviceTrigger
                    mBinding.dateValue.visibility = View.VISIBLE
                    mBinding.dateLayout.visibility = View.VISIBLE
                    mBinding.modeLayout.visibility = View.VISIBLE
                    mBinding.addCondition.visibility = View.VISIBLE
                    mBinding.conditionHint.visibility = View.VISIBLE
                    mBinding.conditionList.visibility = View.VISIBLE
                    if (mBean.conditionLogic == "OR") {
                        mCurrTriggerMode.key = "OR"
                        mCurrTriggerMode.value = resources.getString(R.string.any_of_the_following_conditions_are_satisfied)
                        mBinding.modeValue.text = mCurrTriggerMode.value
                    } else {
                        mCurrTriggerMode.key = "AND"
                        mCurrTriggerMode.value = resources.getString(R.string.all_of_the_following_conditions_are_met)
                        mBinding.modeValue.text = mCurrTriggerMode.value
                    }
                }
                val timeTrigger = if (mTriggerType == "SCHEDULER") mBean.conditionList.first().conDesc else ""
                if (timeTrigger.isNotEmpty()) {
                    mBinding.timeLayout.visibility = View.VISIBLE
                    mBinding.timeValue.text = timeTrigger
                    mBinding.addCondition.visibility = View.GONE
                    mBinding.conditionHint.visibility = View.GONE
                    mBinding.conditionList.visibility = View.GONE
                }
            } else {
                mBean.timeZoneOffset = 480
                mBean.iftttType = "CUSTOM"
                mCurrTriggerMode.key = "OR"
                mCurrTriggerMode.value = resources.getString(R.string.any_of_the_following_conditions_are_satisfied)
                mCurrTriggerMode.select = false
                mBean.conditionLogic = mCurrTriggerMode.key
                mBinding.modeValue.text = mCurrTriggerMode.value
            }
        }
    }

    companion object {
        const val IS_ADD = "is_add"
        const val BEAN = "bean"
    }
}