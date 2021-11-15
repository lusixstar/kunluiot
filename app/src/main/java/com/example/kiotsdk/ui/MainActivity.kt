package com.example.kiotsdk.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMainBinding
import com.example.kiotsdk.ui.device.DeviceListActivity
import com.example.kiotsdk.ui.family.FamilyCreateActivity
import com.example.kiotsdk.ui.family.FamilyListActivity
import com.example.kiotsdk.ui.family.FamilySelectActivity
import com.example.kiotsdk.ui.user.UserInfoActivity
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceListBean
import com.kunluiot.sdk.request.UserApi
import com.kunluiot.sdk.thirdlib.qrcode.CaptureActivity
import com.kunluiot.sdk.thirdlib.qrcode.DecodeConfig
import com.kunluiot.sdk.thirdlib.qrcode.DecodeFormatManager
import com.kunluiot.sdk.thirdlib.qrcode.QRCodeActivity
import com.kunluiot.sdk.thirdlib.qrcode.analyze.MultiFormatAnalyzer
import com.kunluiot.sdk.util.SPUtil
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private var mCurrentFamilyName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.userInfo.setOnClickListener { startActivity<UserInfoActivity>() }
        mBinding.logout.setOnClickListener { logout() }

        mBinding.homeCreate.setOnClickListener { startActivity<FamilyCreateActivity>() }
        mBinding.homeSelect.setOnClickListener { selectFamily.launch(Intent(this, FamilySelectActivity::class.java)) }
        mBinding.homeList.setOnClickListener { startActivity<FamilyListActivity>() }
        mBinding.homeList.setOnClickListener { startActivity<FamilyListActivity>() }

        mBinding.deviceWifiMode.setOnClickListener { startActivity<DeviceListActivity>(DeviceListActivity.NET_TYPE to DeviceListActivity.NET_TYPE_WIFI) }
        mBinding.deviceApMode.setOnClickListener { startActivity<DeviceListActivity>(DeviceListActivity.NET_TYPE to DeviceListActivity.NET_TYPE_WIFI) }
        mBinding.deviceZigbeeMode.setOnClickListener { startActivity<DeviceListActivity>(DeviceListActivity.NET_TYPE to DeviceListActivity.NET_TYPE_ZIG_BEE) }
        mBinding.deviceQrMode.setOnClickListener { gotoQrLaunch.launch(Intent(this, QRCodeActivity::class.java)) }
    }

    private val gotoQrLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
//            val result: String = CameraScan.parseScanResult(data)
//            val resultMap: Map<String, String> = Tools.parseUrl(result)
//            if (resultMap.containsKey("action")) {
//                val actionValue = resultMap["action"]
//                when (actionValue) {
//                    "rauth", "bind" -> {
//                        val bindKey = resultMap["bindKey"]
//                        val devTid = resultMap["devTid"]
//                        KunluHomeSdk.getDeviceInstance().addZigbeeDev(devTid, bindKey, object : IResultCallback() {
//                            fun onError(code: String?, error: String?) {
//                                showToast("添加失败")
//                            }
//
//                            fun onSuccess() {
//                                showToast("添加成功")
//                            }
//                        })
//                    }
//                    else -> {
//                    }
//                }
//            }
        }
    }

    private val selectFamily = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            mCurrentFamilyName = it.data?.getStringExtra(FamilySelectActivity.CURRENT_FAMILY).toString()
            mBinding.homeManagerValue.text = "当前家庭：$mCurrentFamilyName"
        }
    }

    private fun logout() {
        // logout
        toast("logout")
        SPUtil.apply(KunLuHomeSdk.instance.getApp(), UserApi.KHA_API_LOGIN, "")
        KunLuHomeSdk.instance.webSocketDisConnect()
        startActivity<SplashActivity>()
        finish()
    }

    private var cacheMills: Long = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - cacheMills > 1000L) {
            cacheMills = System.currentTimeMillis()
            toast("连按两次退出app")
        } else {
            finish()
        }
    }
}