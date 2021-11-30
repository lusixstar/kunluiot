package com.example.kiotsdk.ui.operation

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.adapter.operation.OperationListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityOperationListBinding
import com.example.kiotsdk.ui.scene.SelectExecutionActionActivity
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationEnumerationBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyCustomParam

/**
 * User: Chris
 * Date: 2021/11/30
 * Desc:
 */

class OperationListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityOperationListBinding

    private lateinit var mAdapter: OperationListAdapter

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
        }

        initAdapter()
    }

    private fun initAdapter() {
        val list = mList.filter { it.usedForIFTTT && it.frameType == 2 }
        mAdapter = OperationListAdapter(list.toMutableList())
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as DeviceOperationProtocolBean
            gotoNext(bean)
        }
    }

    private fun gotoNext(bean: DeviceOperationProtocolBean) {
        val customParamBean = SceneOneKeyCustomParam()
        customParamBean.name = if (mDeviceBean.deviceName.isEmpty()) mDeviceBean.name else mDeviceBean.deviceName
        customParamBean.icon = mDeviceBean.logo
        customParamBean.mid = mDeviceBean.mid
        val foldName = if (mDeviceBean.folderName == "root") "默认房间" else mDeviceBean.folderName
        customParamBean.family_folder = mDeviceBean.familyName + "-" + foldName

        val eventData = SceneLinkedBean()
        eventData.customParam = customParamBean
        if (mDeviceBean.devType == "SUB") {
            //子设备
            val ctrlKey = mDeviceBean.ctrlKey
            val devTid = mDeviceBean.parentDevTid
            eventData.devTid = devTid
            eventData.subDevTid = mDeviceBean.devTid
            eventData.ctrlKey = ctrlKey
        } else {
            eventData.devTid = mDeviceBean.devTid
            eventData.ctrlKey = mDeviceBean.ctrlKey
        }

        eventData.desc = bean.desc
        eventData.newDesc = bean.desc
        eventData.customParam = customParamBean
        setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, eventData))
        finish()
    }

    companion object {
        const val LIST_BEAN = "list_bean"
        const val DEVICE_BEAN = "device_bean"

        const val DEVICE = "device"
    }
}