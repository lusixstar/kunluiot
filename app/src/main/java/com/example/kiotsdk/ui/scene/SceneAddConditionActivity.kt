package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneAddConditionEditBinding
import com.example.kiotsdk.ui.operation.OperationListActivity
import com.kunluiot.sdk.bean.scene.AddTimeConditionEvent
import com.kunluiot.sdk.bean.scene.SceneConditionListParam
import com.kunluiot.sdk.bean.scene.SceneLinkedBean

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneAddConditionActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneAddConditionEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneAddConditionEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.timeLayout.setOnClickListener { gotoTime.launch(Intent(this, SceneAddTimeConditionActivity::class.java)) }
        mBinding.deviceLayout.setOnClickListener { gotoAddDevice.launch(Intent(this, SceneSelectConditionDevicesActivity::class.java)) }
    }

    private val gotoAddDevice = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val device = it.data?.getStringExtra(DEVICE) ?: ""
            if (device == DEVICE) {
                val deviceBean = it.data?.getParcelableExtra(DEVICE_BEAN) ?: SceneConditionListParam()
                setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, deviceBean))
                finish()
            }
        }
    }

    private val gotoTime = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bean = it.data?.getParcelableExtra(SceneAddTimeConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
            setResult(Activity.RESULT_OK, intent.putExtra(TIME, TIME).putExtra(TIME_BEAN, bean))
            finish()
        }
    }

    companion object {
        const val TIME = "time"
        const val TIME_BEAN = "time_bean"

        const val DEVICE = "device"
        const val DEVICE_BEAN = "device_bean"
    }
}