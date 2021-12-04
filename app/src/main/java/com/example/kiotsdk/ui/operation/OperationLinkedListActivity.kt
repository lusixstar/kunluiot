package com.example.kiotsdk.ui.operation

import android.app.Activity
import android.os.Bundle
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.operation.OperationLinkedListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityOperationLinkedListBinding
import com.example.kiotsdk.widget.SeekBarCurtainsBottomDialog
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationFieldsBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.scene.*
import org.jetbrains.anko.selector

/**
 * User: Chris
 * Date: 2021/11/30
 * Desc:
 */

class OperationLinkedListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityOperationLinkedListBinding

    private lateinit var mAdapter: OperationLinkedListAdapter

    private var mList = mutableListOf<DeviceOperationFieldsBean>()
    private var mDeviceBean = DeviceNewBean()

    private var mProtocolBean = DeviceOperationProtocolBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityOperationLinkedListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { it ->
            val list = it.getParcelableArrayListExtra<DeviceOperationProtocolBean>(LIST_BEAN) ?: mutableListOf()
            if (list.size == 1) {
                mProtocolBean = list[0]
                XLog.e("mProtocolBean == $mProtocolBean")
                val ll = list[0].fields.filter { it.usedForIFTTT }
                ll.forEach {
                    if (!it.enumeration.isNullOrEmpty()) {
                        it.selectValue = it.enumeration.first().value.toString()
                        it.selectedDesc = it.enumeration.first().desc
                    } else {
                        it.selectValue = it.minValue.toString()
                        it.selectedDesc = it.minValue.toString()
                    }
                }
                mList.addAll(ll)
            }
            mDeviceBean = it.getParcelableExtra(DEVICE_BEAN) ?: DeviceNewBean()
        }

        mBinding.finish.setOnClickListener { gotoNext() }

        initAdapter()
    }

    private fun gotoNext() {
        val list = mAdapter.data.filter { it.select }
        if (list.isNullOrEmpty()) {
            toastMsg("最少选择一项")
            return
        }
        val bean = SceneConditionListParam()
        val beanCf = SceneOneKeyCustomParam()
        beanCf.name = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
        beanCf.icon = mDeviceBean.logo
        beanCf.mid = mDeviceBean.mid
        val folder = if (mDeviceBean.folderName == "root") "默认房间" else mDeviceBean.folderName
        beanCf.family_folder = mDeviceBean.familyName + "-" + folder
        bean.customFields = beanCf
        when (mDeviceBean.devType) {
            "SUB" -> {
                bean.devTid = mDeviceBean.parentDevTid
                bean.subDevTid = mDeviceBean.devTid
                bean.ctrlKey = mDeviceBean.ctrlKey
            }
            else -> {
                bean.devTid = mDeviceBean.devTid
                bean.ctrlKey = mDeviceBean.ctrlKey
            }
        }
        bean.conDesc = getDesc()
        bean.triggerParams = getCmdArgs()
        bean.relation = "OR"
        setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_RESULT_BEAN, bean))
        finish()
    }

    private fun getDesc(): String {
        var desc = ""
        for (i in mList.indices) {
            if (!mList[i].select || mList[i].selectValue.isEmpty() || mList[i].selectValue == "0") {
                continue
            }
            if (i != 0) {
                desc = "$desc,"
            }
            desc = desc + mAdapter.data[i].desc + ":" + mList[i].selectedDesc
        }
        if (desc.startsWith(",")) {
            desc = desc.substring(1)
        }
        return desc
    }

    private fun getCmdArgs(): MutableList<SceneTriggerParam> {
        val triggerParams = mutableListOf<SceneTriggerParam>()
        val triggerParamsBean = SceneTriggerParam()
        triggerParamsBean.left = "cmdId"
        triggerParamsBean.right = mProtocolBean.cmdId.toString()
        triggerParamsBean.operator = "=="
        triggerParams.add(triggerParamsBean)
        for (item in mList) {
            if (!item.select || item.selectValue.isEmpty() || item.selectValue == "0") {
                continue
            }
            val itemBean = SceneTriggerParam()
            itemBean.left = item.name
            itemBean.right = item.selectValue
            if (item.operator.isEmpty()) {
                itemBean.operator = "=="
            } else {
                itemBean.operator = item.operator
            }
            triggerParams.add(itemBean)
        }
        return triggerParams
    }

    private fun initAdapter() {
        mAdapter = OperationLinkedListAdapter(mList)
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as DeviceOperationFieldsBean
            selectData(bean, position)
        }
    }

    private fun selectData(bean: DeviceOperationFieldsBean, position: Int) {
        if (!bean.enumeration.isNullOrEmpty()) {
            val names = bean.enumeration.map { it.desc }
            selector(bean.desc, names) { dialog, i ->
                val selectData = bean.enumeration[i]
                bean.selectValue = selectData.value.toString()
                bean.selectedDesc = selectData.desc
                bean.select = true
                mAdapter.notifyItemChanged(position)
                dialog.dismiss()
            }
        } else {
            showSeekBarBottomDialog(bean, position)
        }
    }

    private fun showSeekBarBottomDialog(bean: DeviceOperationFieldsBean, position: Int) {
        if (bean.dataType == "INT" || bean.dataType == "NUMBER") {
            val curtainsDialog = SeekBarCurtainsBottomDialog(this)
            curtainsDialog.show()
            curtainsDialog.setTitleName(bean.desc)
            curtainsDialog.setTitleSubName(bean.desc)
            curtainsDialog.setCurrProgress(bean.selectValue.toInt())
            curtainsDialog.setMaxProgress(bean.maxValue.toInt())
            curtainsDialog.setMinProgress(bean.minValue)
            curtainsDialog.setOnDialogClickListener(object : SeekBarCurtainsBottomDialog.OnDialogClickListener {
                override fun onFinishClick(selectProgress: Int) {
                    bean.selectValue = selectProgress.toString()
                    bean.selectedDesc = selectProgress.toString()
                    bean.select = true
                    when (curtainsDialog.checkMode) {
                        0 -> {
                            bean.operator = ">"
                        }
                        1 -> {
                            bean.operator = "<"
                        }
                        else -> {
                            bean.operator = "in"
                            bean.maxValue = curtainsDialog.max.toLong()
                            bean.minValue = curtainsDialog.min
                            bean.selectValue = curtainsDialog.min.toString() + "," + curtainsDialog.max
                            bean.selectedDesc = "(" + curtainsDialog.min.toString() + "," + curtainsDialog.max.toString() + ")"
                        }
                    }
                    mAdapter.notifyItemChanged(position)
                }

                override fun onDismissClick() {}
            })
        } else {
            toastMsg("不支持选项")
        }
    }

    companion object {
        const val LIST_BEAN = "list_bean"
        const val DEVICE_BEAN = "device_bean"

        const val DEVICE = "device"
        const val DEVICE_RESULT_BEAN = "device_result_bean"
    }
}