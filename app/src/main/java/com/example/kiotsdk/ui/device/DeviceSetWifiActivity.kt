package com.example.kiotsdk.ui.device

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceSetWifiBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DevicePinCodeBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.bean.device.DeviceWifiBean
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class DeviceSetWifiActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceSetWifiBinding

    private var mBean: DeviceProductsBean = DeviceProductsBean()
    private var mApModel = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceSetWifiBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mBean = it.getParcelableExtra(BEAN) ?: DeviceProductsBean()
            mApModel = it.getBooleanExtra(NET_TYPE_AP, false)
        }

        mBinding.finish.setOnClickListener { gotoNext() }
        getPermissions()
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
        KunLuHomeSdk.deviceImpl.getPINCode(account, { c, m -> toastErrorMsg(c, m) }, {
            saveWiFiInfo()
            gotoSetNet(it)
        })
    }

    private fun gotoSetNet(bean: DevicePinCodeBean) {
        val password = mBinding.etPassword.text.toString().trim()
        if (mApModel) {
            startActivity<DeviceSetWifiApActivity>(
                DeviceSetWifiDetailsActivity.BEAN to mBean,
                DeviceSetWifiDetailsActivity.SSID to bean.ssid,
                DeviceSetWifiDetailsActivity.PIN to bean.PINCode,
                DeviceSetWifiDetailsActivity.PASSWD to password,
                DeviceSetWifiDetailsActivity.NET_TYPE_AP to mApModel,
            )
        } else {
            startActivity<DeviceSetWifiDetailsActivity>(
                DeviceSetWifiDetailsActivity.BEAN to mBean,
                DeviceSetWifiDetailsActivity.SSID to bean.ssid,
                DeviceSetWifiDetailsActivity.PIN to bean.PINCode,
                DeviceSetWifiDetailsActivity.PASSWD to password,
                DeviceSetWifiDetailsActivity.NET_TYPE_AP to mApModel,
            )
        }
    }

    private fun saveWiFiInfo() {
        val account = mBinding.etAccount.text.toString().trim()
        val info = DeviceWifiBean(account, "")
        SPUtil.apply(this, SPUtil.DEVICE_WIFI_INFO, JsonUtils.toJson(info))
    }

    private fun initWifiInfo() {
        val str = SPUtil.get(this, SPUtil.DEVICE_WIFI_INFO, "") as String
        val info = JsonUtils.fromJson(str, DeviceWifiBean::class.java)

        var currentName = getWifiSsid()
//        var currentName = DemoUtils.getCurrentSSID(this)
        if (currentName == "<unknown ssid>") {
            currentName = ""
            mBinding.etAccount.hint = resources.getString(R.string.please_select_a_wifi_network)
        }
        mBinding.etAccount.setText(currentName)
        if (info != null && info.name.isNotEmpty()) {
            mBinding.etAccount.setText(info.name)
        }
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

    companion object {
        const val BEAN = "bean"
        const val NET_TYPE_AP = "type_ap"
    }
}