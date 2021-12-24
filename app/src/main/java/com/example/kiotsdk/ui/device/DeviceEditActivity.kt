package com.example.kiotsdk.ui.device

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceEditBinding
import com.kunluiot.sdk.KunLuHomeSdk
import org.jetbrains.anko.toast

class DeviceEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceEditBinding

    private var mCtrlKey = ""
    private var mDevTid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mCtrlKey = it.getStringExtra(CTRL_KEY) ?: ""
            mDevTid = it.getStringExtra(DEV_TID) ?: ""
            val name  = it.getStringExtra(NAME) ?: ""
            mBinding.etName.setText(name)
        }
        mBinding.finish.setOnClickListener { updateFamily() }
    }

    private fun updateFamily() {
        val name = mBinding.etName.text.trim().toString()
        if (name.isEmpty()) {
            toast("name is empty")
            return
        }
        KunLuHomeSdk.deviceImpl.editDeviceName(name, mCtrlKey, mDevTid, { code, msg -> toastErrorMsg(code, msg) }, {
            setResult(Activity.RESULT_OK, intent.putExtra(NAME, name))
            finish()
        })
    }

    companion object {
        const val CTRL_KEY = "ctrlKey"
        const val DEV_TID = "devTid"
        const val NAME = "name"
    }
}