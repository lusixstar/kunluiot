package com.example.kiotsdk.ui.operation

import android.app.Activity
import android.os.Bundle
import android.view.View
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
    private var mProtocolBean = DeviceOperationProtocolBean()
    private var mDeviceBean = DeviceNewBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityOperationLinkedListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.finish.visibility = View.INVISIBLE

        intent?.let { it ->
            val list = it.getParcelableArrayListExtra<DeviceOperationProtocolBean>(LIST_BEAN) ?: mutableListOf()
            if (list.isNotEmpty()) mProtocolBean = list[0]
            list.forEach {
                val ll = it.fields.filter { field -> field.usedForIFTTT }
                ll.forEach { field ->
                    if (!field.enumeration.isNullOrEmpty()) {
                        field.selectValue = field.enumeration.first().value.toString()
                        field.selectedDesc = field.enumeration.first().desc
                    } else {
                        field.selectValue = field.minValue.toString()
                        field.selectedDesc = field.minValue.toString()
                    }
                }
                mList.addAll(ll)
            }
            mDeviceBean = it.getParcelableExtra(DEVICE_BEAN) ?: DeviceNewBean()
        }

        initAdapter()
    }

    private fun gotoNext(bean: DeviceOperationFieldsBean) {
        val condBean = SceneConditionListBeanNew()

        val customFields = SceneCustomFieldsBeanNew()
        customFields.name = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
        customFields.devName = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
        customFields.icon = mDeviceBean.logo
        customFields.desc = bean.desc + ":" + bean.selectedDesc
        customFields.mid = mDeviceBean.mid
        val folder = if (mDeviceBean.folderName == "root") "默认房间" else mDeviceBean.folderName
        customFields.family_folder = mDeviceBean.familyName + "-" + folder
        condBean.customFields = customFields
        condBean.relation = "OR"

        when (mDeviceBean.devType) {
            "SUB" -> {
                condBean.devTid = mDeviceBean.parentDevTid
                condBean.subDevTid = mDeviceBean.devTid
                condBean.ctrlKey = mDeviceBean.parentCtrlKey
            }
            else -> {
                condBean.devTid = mDeviceBean.devTid
                condBean.ctrlKey = mDeviceBean.ctrlKey
            }
        }
        condBean.conDesc = bean.desc + ":" + bean.selectedDesc
        condBean.triggerParams = getCmdArgs()
        setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_RESULT_BEAN, condBean))
        finish()
    }

    private fun getCmdArgs(): MutableList<SceneTriggerBeanNew> {
        val triggerParams = mutableListOf<SceneTriggerBeanNew>()
        val triggerParamsBean = SceneTriggerBeanNew()
        triggerParamsBean.left = "cmdId"
        triggerParamsBean.right = mProtocolBean.cmdId.toString()
        triggerParamsBean.operator = "=="
        triggerParams.add(triggerParamsBean)
        for (item in mList) {
            val itemBean = SceneTriggerBeanNew()
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
            selectData(bean)
        }
    }

    private fun selectData(bean: DeviceOperationFieldsBean) {
        if (!bean.enumeration.isNullOrEmpty()) {
            val names = bean.enumeration.map { it.desc }
            selector(bean.desc, names) { dialog, i ->
                val selectData = bean.enumeration[i]
                bean.selectValue = selectData.value.toString()
                bean.selectedDesc = selectData.desc
                bean.select = true
                gotoNext(bean)
                dialog.dismiss()
            }
        } else {
            showSeekBarBottomDialog(bean)
        }
    }

    private fun showSeekBarBottomDialog(bean: DeviceOperationFieldsBean) {
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
                    gotoNext(bean)
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