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
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.request.UserApi
import com.kunluiot.sdk.thirdlib.qrcode.CameraScan
import com.kunluiot.sdk.thirdlib.qrcode.QRCodeActivity
import com.kunluiot.sdk.thirdlib.qrcode.util.LogUtils
import com.kunluiot.sdk.util.SPUtil
import com.kunluiot.sdk.util.Tools
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
        mBinding.deviceApMode.setOnClickListener { startActivity<DeviceListActivity>(DeviceListActivity.NET_TYPE to DeviceListActivity.NET_TYPE_WIFI, DeviceListActivity.NET_TYPE_AP to true) }
        mBinding.deviceZigbeeMode.setOnClickListener { startActivity<DeviceListActivity>(DeviceListActivity.NET_TYPE to DeviceListActivity.NET_TYPE_ZIG_BEE) }
        mBinding.deviceQrMode.setOnClickListener { gotoQrLaunch.launch(Intent(this, QRCodeActivity::class.java)) }

        mBinding.deviceList.setOnClickListener { getDeviceList() }
    }

    private fun getDeviceList() {
        KunLuHomeSdk.deviceImpl.getAllDevicesAct(true, object : IDeviceListCallback {
            override fun onSuccess(bean: List<DeviceNewBean>) {

            }

            override fun onError(code: String, error: String) {

            }
        })
    }

    private val gotoQrLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            it.data?.let { bean ->
                val result: String = CameraScan.parseScanResult(bean) ?: ""
                val resultMap: Map<String, String> = Tools.parseUrl(result)
                LogUtils.e("resultMap == $resultMap")
                if (resultMap.containsKey(KunLuDeviceType.DEVICE_ACTION)) {
                    when (resultMap[KunLuDeviceType.DEVICE_ACTION]) {
                        KunLuDeviceType.DEVICE_BIND -> {
                            gotoScanNext(resultMap[KunLuDeviceType.DEVICE_BIND_KEY] ?: "", resultMap[KunLuDeviceType.DEVICE_DEV_TID] ?: "")
                        }
                    }
                }
            }
        }
    }

    private fun gotoScanNext(bindKey: String, devTid: String) {
        if (bindKey.isEmpty() || devTid.isEmpty()) {
            return
        }
//        KunLuHomeSdk.deviceImpl.scanCodeDevice(bindKey, devTid, scanCallback)
    }

//    private val scanCallback = object : IOneDeviceCallback {
//
//        override fun onSuccess(bean: DeviceNewBean) {
//            gotoNext(bean)
//        }
//
//        override fun onError(code: String, error: String) {
//            toast("code == $code, error == $error")
//        }
//    }

    private fun gotoNext(bean: DeviceNewBean) {
        val devType: String = bean.devType
//        if (devType == KunLuDeviceType.DEVICE_SUB) {
//            startActivity(Intent(this, ConfigNetworkFinishActivity::class.java)
//                .putExtra("devTid", response.getParentDevTid())
//                .putExtra("subDevTid", response.getDevTid())
//                .putExtra("branchNames", JsonUtils.toJson(response.getBranchNames()))
//                //                    .putExtra("device", response)
//                .putExtra("registerId", response.getRegisterId())
//                .putExtra("deviceName", response.getName()).putExtra("mid", response.getMid())
//                .putExtra("ctrlKey", response.getParentCtrlKey()))
//        } else {
//            startActivity(Intent(this, ConfigNetworkFinishActivity::class.java)
//                .putExtra("devTid", response.getDevTid())
//                .putExtra("branchNames", JsonUtils.toJson(response.getBranchNames()))
    //                    .putExtra("device", response)
//                .putExtra("mid", response.getMid())
//                .putExtra("registerId", response.getRegisterId())
//                .putExtra("deviceName", response.getName())
//                .putExtra("ctrlKey", response.getCtrlKey()))
//        }
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