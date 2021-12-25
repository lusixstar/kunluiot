package com.example.kiotsdk.ui.device

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceGotoBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.thirdlib.qrcode.CameraScan
import com.kunluiot.sdk.thirdlib.qrcode.QRCodeActivity
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.Tools
import org.jetbrains.anko.startActivity

/**
 * User: Chris
 * Date: 2021/12/4
 * Desc:
 */

class DeviceSetNetGotoActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceGotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceGotoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.deviceWifiMode.setOnClickListener { startActivity<DeviceProductListActivity>(DeviceProductListActivity.NET_TYPE to DeviceProductListActivity.NET_TYPE_WIFI) }
        mBinding.deviceApMode.setOnClickListener { startActivity<DeviceProductListActivity>(DeviceProductListActivity.NET_TYPE to DeviceProductListActivity.NET_TYPE_WIFI, DeviceProductListActivity.NET_TYPE_AP to true) }
        mBinding.deviceZigbeeMode.setOnClickListener { startActivity<DeviceProductListActivity>(DeviceProductListActivity.NET_TYPE to DeviceProductListActivity.NET_TYPE_ZIG_BEE) }
        mBinding.deviceQrMode.setOnClickListener { gotoQrLaunch.launch(Intent(this, QRCodeActivity::class.java)) }
    }

    private val gotoQrLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            it.data?.let { bean ->
                val result: String = CameraScan.parseScanResult(bean) ?: ""
                val resultMap: Map<String, String> = Tools.parseUrl(result)
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
        KunLuHomeSdk.deviceImpl.scanCodeDevice(bindKey, devTid, { code, msg -> toastErrorMsg(code, msg) }, { gotoNext(it) })
    }

    private fun gotoNext(bean: DeviceNewBean) {
//        startActivity<DeviceConfigFinishActivity>(
//            DeviceConfigFinishActivity.DEV_TID to bean.devTid,
//            DeviceConfigFinishActivity.DEVICE to bean,
//            DeviceConfigFinishActivity.MID to bean.mid,
//            DeviceConfigFinishActivity.REGISTER_ID to bean.registerId,
//            DeviceConfigFinishActivity.BRANCH_NAMES to JsonUtils.toJson(bean.branchNames),
//            DeviceConfigFinishActivity.DEVICE_NAME to bean.deviceName,
//            DeviceConfigFinishActivity.CTRL_KEY to bean.ctrlKey,
//        )
        toastMsg("bind success")
        finish()
    }
}