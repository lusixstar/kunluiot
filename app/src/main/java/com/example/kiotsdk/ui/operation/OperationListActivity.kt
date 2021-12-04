package com.example.kiotsdk.ui.operation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.operation.OperationListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityOperationListBinding
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean

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
        mAdapter = OperationListAdapter(mList)
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as DeviceOperationProtocolBean
            gotoAddDevice.launch(Intent(this, OperationListFieldsActivity::class.java).putExtra(DEVICE, mDeviceBean).putExtra(PROTOCOL_BEAN, bean))
        }
    }

    private val gotoAddDevice = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val device = it.data?.getStringExtra(DEVICE_RESULT) ?: ""
            val deviceBean = it.data?.getParcelableExtra(DEVICE_BEAN_RESULT) ?: SceneLinkedBean()
            if (device == DEVICE_RESULT) {
                setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, deviceBean))
                finish()
            }
        }
    }

    companion object {
        const val LIST_BEAN = "list_bean"
        const val DEVICE_BEAN = "device_bean"

        const val DEVICE = "device"
        const val PROTOCOL_BEAN = "protocol_bean"

        const val DEVICE_RESULT = "device_result"
        const val DEVICE_BEAN_RESULT = "device_bean_result"
    }
}