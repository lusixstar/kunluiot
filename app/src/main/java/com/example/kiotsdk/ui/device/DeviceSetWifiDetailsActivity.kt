package com.example.kiotsdk.ui.device

import android.os.Bundle
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceSetWifiBinding
import com.example.kiotsdk.databinding.ActivityDeviceSetWifiDetailsBinding
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DevicePinCodeBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.bean.device.DeviceWifiBean
import com.kunluiot.sdk.callback.device.IPinCodeCallback
import com.kunluiot.sdk.thirdlib.ws.websocket.util.LogUtil
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
import org.jetbrains.anko.toast

class DeviceSetWifiDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceSetWifiDetailsBinding

    private var mBean: DeviceProductsBean = DeviceProductsBean()
    private var mSsid: String = ""
    private var mPin: String = ""
    private var mPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceSetWifiDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mBean = it.getParcelableExtra(BEAN) ?: DeviceProductsBean()
            mSsid = it.getStringExtra(SSID) ?: ""
            mPin = it.getStringExtra(PIN) ?: ""
            mPassword = it.getStringExtra(PASSWD) ?: ""
        }

        startConfig()
    }

    /**
     * 开始配网
     */
    private fun startConfig() {
        LogUtil.e("startConfig == ", mBean.toString())
        LogUtil.e("mSsid == ", mSsid)
        LogUtil.e("mPin == ", mPin)
        LogUtil.e("mPassword == ", mPassword)
//        mRespState = com.leixun.iot.presentation.ui.device.DeviceConfigNetworkActivity.RESP_LOADING
//        mConfigWifiList.clear()
//        if (mIsSoftApConfig) {
//            mRNHekrConfigModule.softAP(mSSid, mWifiPwd, mPINCode, mHandler)
//        } else {
//            mRNHekrConfigModule.config(mSSid, mWifiPwd, mPINCode, mHandler)
//        }
//        mCurrStep = 2
//        getDoneDevices()
//        showConfigNetworkPage(mRespState)
//        if (null != time) {
//            time.start()
//        }
    }

    companion object {
        const val BEAN = "bean"
        const val SSID = "ssid"
        const val PIN = "pin"
        const val PASSWD = "password"
    }
}