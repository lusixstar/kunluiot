package com.example.kiotsdk.ui.operation

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.operation.OperationFieldsListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityOperationFieldsListBinding
import com.example.kiotsdk.ui.scene.SelectExecutionActionActivity
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationFieldsBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyCustomParam
import org.jetbrains.anko.selector
import java.util.*

/**
 * User: Chris
 * Date: 2021/11/30
 * Desc:
 */

class OperationListFieldsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityOperationFieldsListBinding

    private lateinit var mAdapter: OperationFieldsListAdapter

    private var mDeviceBean = DeviceNewBean()
    private var mProtocolBean = DeviceOperationProtocolBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityOperationFieldsListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { it ->
            mDeviceBean = it.getParcelableExtra(OperationListActivity.DEVICE) ?: DeviceNewBean()
            mProtocolBean = it.getParcelableExtra(OperationListActivity.PROTOCOL_BEAN) ?: DeviceOperationProtocolBean()
            mProtocolBean.fields.forEach {
                if (!it.enumeration.isNullOrEmpty()) {
                    it.selectValue = it.enumeration.first().value.toString()
                    it.selectedDesc = it.enumeration.first().desc
                } else {
                    it.selectValue = it.minValue.toString()
                    it.selectedDesc = it.minValue.toString()
                }
            }
            mBinding.toolBar.title = resources.getString(R.string.action_list) + " - " + mProtocolBean.desc
        }

        initAdapter()
    }

    private fun initAdapter() {
        mAdapter = OperationFieldsListAdapter(mProtocolBean.fields.toMutableList())
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as DeviceOperationFieldsBean
            selectData(bean)
        }
    }

    private fun gotoNext() {
        val customParamBean = SceneOneKeyCustomParam()
        customParamBean.name = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
        customParamBean.icon = mDeviceBean.logo
        customParamBean.mid = mDeviceBean.mid
        val folder = if (mDeviceBean.folderName == "root") "默认房间" else mDeviceBean.folderName
        customParamBean.family_folder = mDeviceBean.familyName + "-" + folder
        val eventData = SceneLinkedBean()
        when (mDeviceBean.devType) {
            "SUB" -> {
                eventData.devTid = mDeviceBean.parentDevTid
                eventData.subDevTid = mDeviceBean.devTid
                eventData.ctrlKey = mDeviceBean.ctrlKey
            }
            else -> {
                eventData.devTid = mDeviceBean.devTid
                eventData.ctrlKey = mDeviceBean.ctrlKey
            }
        }
        eventData.cmdArgs = getCmdArgs()
        eventData.desc = getDesc()
        eventData.newDesc = getDesc()
        eventData.customParam = customParamBean
        setResult(Activity.RESULT_OK, intent.putExtra(OperationListActivity.DEVICE_RESULT, OperationListActivity.DEVICE_RESULT).putExtra(OperationListActivity.DEVICE_BEAN_RESULT, eventData))
        finish()
    }

    private fun getDesc(): String {
        var desc = ""
        if (mProtocolBean.fields.size > 1) {
            desc = mProtocolBean.desc + ":"
        }
        for (i in mProtocolBean.fields.indices) {
            if (i != 0) {
                desc = "$desc,"
            }
            desc = desc + mProtocolBean.fields[i].desc + ":" + mProtocolBean.fields[i].selectedDesc
        }
        return desc
    }

    private fun getCmdArgs(): Map<String, Int> {
        val cmdArgMap: MutableMap<String, Int> = HashMap()
        cmdArgMap["cmdId"] = mProtocolBean.cmdId
        for (item in mProtocolBean.fields) {
            val value: String = item.selectValue
            if (value.matches(Regex("^[0-9]*$"))) {
                cmdArgMap[item.name] = item.selectValue.toInt()
            }
        }
        return cmdArgMap
    }

    private fun selectData(bean: DeviceOperationFieldsBean) {
        if (!bean.enumeration.isNullOrEmpty()) {
            val names = bean.enumeration.map { it.desc }
            selector(bean.desc, names) { dialog, i ->
                val selectData = bean.enumeration[i]
                bean.selectValue = selectData.value.toString()
                bean.selectedDesc = selectData.desc
                dialog.dismiss()
                gotoNext()
            }
        }
    }
}