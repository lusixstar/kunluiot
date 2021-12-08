package com.example.kiotsdk.ui.device

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceSetWifiApBinding
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import org.jetbrains.anko.startActivity

class DeviceSetWifiApActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceSetWifiApBinding

    private var mBean: DeviceProductsBean = DeviceProductsBean()
    private var mApModel = true
    private var mSsid = ""
    private var mPasswd = ""
    private var mPin = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceSetWifiApBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mBean = it.getParcelableExtra(DeviceSetWifiDetailsActivity.BEAN) ?: DeviceProductsBean()
            mSsid = it.getStringExtra(DeviceSetWifiDetailsActivity.SSID) ?: ""
            mPin = it.getStringExtra(DeviceSetWifiDetailsActivity.PIN) ?: ""
            mPasswd = it.getStringExtra(DeviceSetWifiDetailsActivity.PASSWD) ?: ""
            mApModel = it.getBooleanExtra(DeviceSetWifiDetailsActivity.NET_TYPE_AP, false)
        }

        setTitleHint()

        mBinding.finish.setOnClickListener { gotoSetNet() }

        mBinding.etAccount.addTextChangedListener({ _, _, _, _ -> }, { currentName, _, _, _ ->
            if (currentName.toString().contains("SmartDevice")) {
                mBinding.finish.visibility = View.VISIBLE
            } else {
                mBinding.finish.visibility = View.INVISIBLE
            }
        }, { })
    }

    private fun setTitleHint() {
        val subName = SpannableString("请将当前Wi-Fi更换成“SmartDevice-xxx”")
        val start = subName.toString().indexOf("Smart")
        val end = subName.toString().indexOf("xxx") + "xxx".length
        subName.setSpan(ForegroundColorSpan(Color.parseColor("#0E6CAE")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.title.text = subName
    }

    private fun gotoSetNet() {
        startActivity<DeviceSetWifiDetailsActivity>(
            DeviceSetWifiDetailsActivity.BEAN to mBean,
            DeviceSetWifiDetailsActivity.SSID to mSsid,
            DeviceSetWifiDetailsActivity.PIN to mPin,
            DeviceSetWifiDetailsActivity.PASSWD to mPasswd,
            DeviceSetWifiDetailsActivity.NET_TYPE_AP to mApModel,
        )
        finish()
    }
}