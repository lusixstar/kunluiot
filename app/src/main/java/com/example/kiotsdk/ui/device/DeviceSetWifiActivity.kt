package com.example.kiotsdk.ui.device

import android.os.Bundle
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceSetWifiBinding
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DevicePinCodeBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.bean.device.DeviceWifiBean
import com.kunluiot.sdk.callback.device.IPinCodeCallback
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DeviceSetWifiActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceSetWifiBinding

    private var mBean: DeviceProductsBean = DeviceProductsBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceSetWifiBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mBean = it.getParcelableExtra(DeviceSetWifiDetailsActivity.BEAN) ?: DeviceProductsBean() }

        mBinding.finish.setOnClickListener { gotoNext() }
        initWifiInfo()
    }

    private fun gotoNext() {
        val account = mBinding.etAccount.text.toString().trim()
        val password = mBinding.etPassword.text.toString().trim()
        if (account.isEmpty()) {
            toast("wifi name is empty")
            return
        }
        if (password.isEmpty()) {
            toast("password is empty")
            return
        }
        KunLuHomeSdk.deviceImpl.getPINCode(account, ssidCallback)
    }

    private val ssidCallback = object : IPinCodeCallback {

        override fun onSuccess(bean: DevicePinCodeBean) {
            saveWiFiInfo()
            gotoSetNet(bean)
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    private fun gotoSetNet(bean: DevicePinCodeBean) {
        val password = mBinding.etPassword.text.toString().trim()
        startActivity<DeviceSetWifiDetailsActivity>(
            DeviceSetWifiDetailsActivity.BEAN to mBean,
            DeviceSetWifiDetailsActivity.SSID to bean.ssid,
            DeviceSetWifiDetailsActivity.PIN to bean.PINCode,
            DeviceSetWifiDetailsActivity.PASSWD to password,
        )
    }


    private fun saveWiFiInfo() {
        val account = mBinding.etAccount.text.toString().trim()
        val info = DeviceWifiBean(account, "")
        SPUtil.apply(this, SPUtil.DEVICE_WIFI_INFO, JsonUtils.toJson(info))
    }

    private fun initWifiInfo() {
        val str = SPUtil.get(this, SPUtil.DEVICE_WIFI_INFO, "") as String
        val info =  JsonUtils.fromJson(str, DeviceWifiBean::class.java)

        var currentName = DemoUtils.getCurrentSSID(this)
        if (currentName == "<unknown ssid>") {
            currentName = ""
            mBinding.etAccount.hint = resources.getString(R.string.please_select_a_wifi_network)
        }
        mBinding.etAccount.setText(currentName)
        if (info != null && info.name.isNotEmpty()) {
            mBinding.etAccount.setText(info.name)
        }
    }

    companion object {
        const val BEAN = "bean"
    }
}