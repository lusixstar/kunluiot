package com.example.kiotsdk.ui.operation

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.operation.OperationFieldsListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityOperationListBinding
import com.example.kiotsdk.ui.scene.SelectExecutionActionActivity
import com.example.kiotsdk.util.DemoUtils
import com.example.kiotsdk.widget.SeekBarBottomDialog
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationFieldsBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.scene.*
import org.jetbrains.anko.selector
import java.util.*

/**
 * User: Chris
 * Date: 2021/11/30
 * Desc:
 */

class OperationListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityOperationListBinding

    private lateinit var mAdapter: OperationFieldsListAdapter

    private var mIsOneKey = false

    private var mList = mutableListOf<DeviceOperationProtocolBean>()
    private var mDeviceBean = DeviceNewBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityOperationListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { it ->
            mList = it.getParcelableArrayListExtra(LIST_BEAN) ?: mutableListOf()
            mDeviceBean = it.getParcelableExtra(DEVICE_BEAN) ?: DeviceNewBean()
            mIsOneKey = it.getBooleanExtra(SelectExecutionActionActivity.IS_ONE_KEY, false)
            if (mDeviceBean.devType == "INDEPENDENT") {
                mBinding.btnFinish.visibility = View.GONE
            }
        }

        mBinding.btnFinish.setOnClickListener { gotoNext() }
        initAdapter()
    }

    private fun gotoNext() {
        if (mIsOneKey) {
            val customFields = SceneCustomFieldsBeanNew()
            customFields.name = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
            customFields.devName = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
            customFields.icon = mDeviceBean.logo
            customFields.mid = mDeviceBean.mid
            customFields.desc = getDesc()
            val folder = if (mDeviceBean.folderName == "root") "默认房间" else mDeviceBean.folderName
            customFields.family_folder = mDeviceBean.familyName + "-" + folder

            val oneKeyTask = SceneOneKeyTaskListBean()
            oneKeyTask.customParam = customFields
            oneKeyTask.desc = getDesc()
            when (mDeviceBean.devType) {
                "SUB" -> {
                    oneKeyTask.devTid = mDeviceBean.parentDevTid
                    oneKeyTask.subDevTid = mDeviceBean.devTid
                    oneKeyTask.ctrlKey = mDeviceBean.parentCtrlKey
                }
                else -> {
                    oneKeyTask.devTid = mDeviceBean.devTid
                    oneKeyTask.ctrlKey = mDeviceBean.ctrlKey
                }
            }
            oneKeyTask.cmdArgs = getCmdArgs()

            setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, oneKeyTask))
            finish()
        } else {
            val customFields = SceneCustomFieldsBeanNew()
            customFields.name = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
            customFields.devName = if (mDeviceBean.deviceName.isNotEmpty()) mDeviceBean.deviceName else mDeviceBean.name
            customFields.icon = mDeviceBean.logo
            customFields.mid = mDeviceBean.mid
            customFields.desc = getDesc()
            val folder = if (mDeviceBean.folderName == "root") "默认房间" else mDeviceBean.folderName
            customFields.family_folder = mDeviceBean.familyName + "-" + folder

            val iftttTasksBean = SceneIftttTasksListBeanNew()
            iftttTasksBean.desc = getDesc()

            val iftttTasksParam = SceneIftttTasksParamBeanNew()

            when (mDeviceBean.devType) {
                "SUB" -> {
                    iftttTasksBean.type = "APPSUBSEND"
                    iftttTasksParam.devTid = mDeviceBean.parentDevTid
                    iftttTasksParam.subDevTid = mDeviceBean.devTid
                    iftttTasksParam.ctrlKey = mDeviceBean.parentCtrlKey
                }
                else -> {
                    iftttTasksBean.type = "APPSEND"
                    iftttTasksParam.devTid = mDeviceBean.devTid
                    iftttTasksParam.ctrlKey = mDeviceBean.ctrlKey
                }
            }
            iftttTasksParam.data = getCmdArgs()
            iftttTasksBean.params = iftttTasksParam
            iftttTasksBean.customParam = customFields

            setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, iftttTasksBean))
            finish()
        }
    }

    private fun getCmdArgs(): Map<String, String> {
        val cmdArgMap: MutableMap<String, String> = HashMap()
        cmdArgMap["cmdId"] = mList.first().cmdId.toString()
        for (item in mAdapter.data) {
            if (mDeviceBean.devType == "INDEPENDENT") {
                if (item.select) cmdArgMap[item.name] = item.selectValue
            } else {
                cmdArgMap[item.name] = item.selectValue
            }
        }
        return cmdArgMap
    }

    private fun getDesc(): String {
        var desc = ""
        for (i in mAdapter.data.indices) {
            if (mDeviceBean.devType == "INDEPENDENT") {
                if (mAdapter.data[i].select) desc = desc + mAdapter.data[i].desc + ":" + mAdapter.data[i].selectedDesc
            } else {
                if (i != 0) {
                    desc = "$desc,"
                }
                desc = desc + mAdapter.data[i].desc + ":" + mAdapter.data[i].selectedDesc
            }
        }
        return desc
    }

    private fun initAdapter() {
        val list = mutableListOf<DeviceOperationFieldsBean>()
        mList.forEach { list.addAll(it.fields) }
        list.forEach {
            if (!it.enumeration.isNullOrEmpty()) {
                it.selectValue = it.enumeration.first().value.toString()
                it.selectedDesc = it.enumeration.first().desc
            } else {
                it.selectValue = it.minValue.toString()
                it.selectedDesc = it.minValue.toString()
            }
        }
        mAdapter = OperationFieldsListAdapter(list)
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as DeviceOperationFieldsBean
            selectData(bean, position)
        }
    }

    private fun selectData(bean: DeviceOperationFieldsBean, position: Int) {
        if (!bean.enumeration.isNullOrEmpty()) {
            val names = bean.enumeration.map { it.desc }
            selector("", names) { dialog, i ->
                val selectData = bean.enumeration[i]
                bean.selectValue = selectData.value.toString()
                bean.selectedDesc = selectData.desc
                if (mDeviceBean.devType == "INDEPENDENT") {
                    bean.select = true
                    gotoNext()
                } else {
                    mAdapter.notifyItemChanged(position)
                }
                dialog.dismiss()
            }
        } else {
            val dialog = SeekBarBottomDialog(this)
            dialog.show()
            dialog.setTitleSubName(bean.desc)
            dialog.setCurrProgress(bean.selectValue.toInt())
            dialog.setMaxProgress(bean.maxValue.toInt())
            dialog.setMinProgress(bean.minValue)
            dialog.setOnDialogClickListener(object : SeekBarBottomDialog.OnDialogClickListener {
                override fun onFinishClick(selectProgress: Int) {
                    bean.selectValue = selectProgress.toString()
                    bean.selectedDesc = selectProgress.toString()
                    if (mDeviceBean.devType == "INDEPENDENT") {
                        bean.select = true
                        gotoNext()
                    } else {
                        mAdapter.notifyItemChanged(position)
                    }
                }

                override fun onDismissClick() {
                }
            })
        }
    }

    companion object {
        const val LIST_BEAN = "list_bean"
        const val DEVICE_BEAN = "device_bean"

        const val DEVICE = "device"
        const val PROTOCOL_BEAN = "protocol_bean"
    }
}