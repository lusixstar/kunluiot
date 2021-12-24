package com.example.kiotsdk.ui.device

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceChangeWifiBinding
import com.example.kiotsdk.util.DESUtil
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceChangeWifiBean
import com.kunluiot.sdk.bean.device.DeviceWifiBean
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
import org.jetbrains.anko.toast


class DeviceChangeWifiActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceChangeWifiBinding

    private var mCtrlKey = ""
    private var mSsid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceChangeWifiBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mSsid = it.getStringExtra(SSID) ?: ""
            mCtrlKey = it.getStringExtra(CTRL_KEY) ?: ""
            if (mSsid.isNotEmpty()) {
                mBinding.etAccount.setText(mSsid)
            }
        }

        mBinding.finish.setOnClickListener { gotoNext() }
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
        val key = mCtrlKey.substring(0, 4) + mCtrlKey.substring(mCtrlKey.length - 4)
        val pwd: String = DESUtil.encryptDES(password, key)
        KunLuHomeSdk.deviceImpl.switchDeviceWifi(mCtrlKey, account, pwd, { c, m -> toastErrorMsg(c, m) }, {
            toastMsg("请稍后")
            countDownTimer.start()
        })
    }

    private fun initWifiInfo() {
        var currentName = getWifiSsid()
//        var currentName = DemoUtils.getCurrentSSID(this)
        if (currentName == "<unknown ssid>") {
            currentName = ""
            mBinding.etAccount.hint = resources.getString(R.string.please_select_a_wifi_network)
        }
        mBinding.etAccount.setText(currentName)
    }

    private fun getPermissions() {
        XXPermissions.with(this).permission(Permission.ACCESS_FINE_LOCATION).permission(Permission.ACCESS_COARSE_LOCATION).request(object : OnPermissionCallback {
            override fun onGranted(permissions: List<String>, all: Boolean) {
                if (all) {
                    initWifiInfo()
                }
            }

            override fun onDenied(permissions: List<String>, never: Boolean) {

            }
        })
    }

    override fun onResume() {
        super.onResume()
        getPermissions()
    }

    private fun getWifiSsid(): String {
        val ssid = "unknown id"
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val mWifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = mWifiManager.connectionInfo
            return info.ssid.replace("\"", "")
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            val connManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connManager.activeNetworkInfo
            if (networkInfo!!.isConnected) {
                if (networkInfo.extraInfo != null) {
                    return networkInfo.extraInfo.replace("\"", "");
                }
            }
        }
        return ssid
    }

    private var smsTime: Long = 30 * 1000
    private val countDownTimer = object : CountDownTimer(smsTime, 3000) {

        override fun onFinish() {
            mBinding.finish.isEnabled = true
            mBinding.finish.text = "请重试"
        }

        override fun onTick(millisUntilFinished: Long) {
            mBinding.finish.isEnabled = false
            KunLuHomeSdk.deviceImpl.pollingDeviceWifi(mCtrlKey, { c, m -> toastErrorMsg(c, m) }, { setData(it) })
        }
    }

    private fun setData(it: DeviceChangeWifiBean) {
        when (it.changeWIFI.status) {
            1 -> {
                toastMsg("切换网络成功!")
                countDownTimer.cancel()
                mBinding.finish.isEnabled = true
                setResult(Activity.RESULT_OK)
                finish()
            }
            4 -> {
                toastMsg("设备未发现Wi-Fi!")
                countDownTimer.cancel()
                mBinding.finish.isEnabled = true
            }
            5 -> {
                toastMsg("密码错误!")
                countDownTimer.cancel()
                mBinding.finish.isEnabled = true
            }
            else -> {
                toastMsg("请稍后")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    companion object {
        const val CTRL_KEY = "ctrl_key"
        const val SSID = "ssid"
    }
}