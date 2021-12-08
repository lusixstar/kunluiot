package com.example.kiotsdk.ui.device

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceSetWifiApBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.bean.device.DeviceWifiBean
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
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
        mBinding.change.setOnClickListener { startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }

        mBinding.etAccount.addTextChangedListener({ _, _, _, _ -> }, { currentName, _, _, _ ->
            if (currentName.toString().contains("SmartDevice")) {
                mBinding.finish.visibility = View.VISIBLE
            } else {
                mBinding.finish.visibility = View.INVISIBLE
            }
        }, { })

        getPermissions()
    }

    override fun onResume() {
        super.onResume()
        getPermissions()
    }

    private fun setTitleHint() {
        val subName = SpannableString("请将当前Wi-Fi更换成“SmartDevice-xxx”")
        val start = subName.toString().indexOf("Smart")
        val end = subName.toString().indexOf("xxx") + "xxx".length
        subName.setSpan(ForegroundColorSpan(Color.parseColor("#0E6CAE")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.title.text = subName
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
        XXPermissions.with(this)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .permission(Permission.ACCESS_COARSE_LOCATION)
            .request(object : OnPermissionCallback {
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