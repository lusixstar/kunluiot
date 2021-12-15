package com.example.kiotsdk.ui.device

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceInfoBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceUpdateBean
import com.kunluiot.sdk.thirdlib.ws.websocket.SocketListener
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse
import com.kunluiot.sdk.ui.web.DeviceWebControlActivity
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.Tools
import org.java_websocket.framing.Framedata
import org.jetbrains.anko.startActivity
import java.nio.ByteBuffer
import java.util.*

/**
 * User: Chris
 * Date: 2021/12/7
 * Desc:
 */

class DeviceInfoActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceInfoBinding

    private var mBean = DeviceNewBean()

    private var mCheckUpdateBean = DeviceUpdateBean()
    private var mCoordinatorCheckUpdate = DeviceUpdateBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mBean = it.getParcelableExtra(BEAN) ?: DeviceNewBean() }

        mBinding.btnControl.setOnClickListener {
            if (mBean.androidPageZipURL.isNotEmpty()) {
                startActivity<DeviceWebControlActivity>(DeviceWebControlActivity.BEAN to mBean)
            } else {
                toastMsg("androidPageZipURL is empty")
            }
        }
        mBinding.btnUpdate.setOnClickListener {
            if (mBean.devType == "GATEWAY" && mBean.zigOtaBinVer.isNotEmpty()) {
                updateCoordinatorFirmware()
            } else {
                updateFirmware()
            }
        }
        mBinding.tvName.setOnClickListener {
            val devTid = if (mBean.devType == "SUB") mBean.parentDevTid else mBean.devTid
            gotoName.launch(Intent(this, DeviceEditActivity::class.java).putExtra(DeviceEditActivity.CTRL_KEY, mBean.ctrlKey).putExtra(DeviceEditActivity.DEV_TID, devTid))
        }
        KunLuHomeSdk.instance.getWebSocketManager()?.addListener(webSocketListener)
        setData()
        checkDeviceIsUpdate()
    }

    private val gotoName = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val name = it.data?.getStringExtra(DeviceEditActivity.NAME) ?: ""
            mBinding.tvName.text = name
        }
    }

    private fun checkDeviceIsUpdate() {
        if (mBean.devType == "GATEWAY" && mBean.zigOtaBinVer.isNotEmpty()) {
            KunLuHomeSdk.deviceImpl.checkZigVer(mBean.zigOtaBinVer, mBean.productPublicKey, { c, m -> toastErrorMsg(c, m) }, { mCoordinatorCheckUpdate = it.first() })
        } else {
            KunLuHomeSdk.deviceImpl.checkDeviceIsUpdate(if (mBean.binVer.isEmpty()) mBean.binVersion else mBean.binVer, mBean.binType, mBean.binVersion, mBean.productPublicKey, mBean.devTid, mBean.ctrlKey, { c, m -> toastErrorMsg(c, m) }, { mCheckUpdateBean = it.first() })
        }
    }

    private fun updateCoordinatorFirmware() {
        if (!mBean.online) {
            toastMsg("设备不在线")
            return
        }
        if (!mCoordinatorCheckUpdate.update) {
            toastMsg("无需更新")
            return
        }
        val bean = mCoordinatorCheckUpdate.devFirmwareOTARawRuleVO
        val devInfoMap = HashMap<String, Any>()
        devInfoMap["binVer"] = bean.latestBinVer
        devInfoMap["devTid"] = mBean.devTid
        devInfoMap["binUrl"] = bean.binUrl
        devInfoMap["md5"] = bean.md5
        devInfoMap["binType"] = bean.latestBinType
//        devInfoMap["appTid"] = Tools.getAppTid().toString() + "web"
        devInfoMap["appTid"] =  Build.BRAND + Build.MODEL
        devInfoMap["size"] = bean.size.toString() + ""
        devInfoMap["ctrlKey"] = mBean.ctrlKey
        val map = HashMap<String, Any>()
        map["msgId"] = KunLuHomeSdk.instance.getMsgId()
        map["action"] = "zigBeeDevUpgrade"
        map["params"] = devInfoMap
        KunLuHomeSdk.instance.getWebSocketManager()?.send(JsonUtils.toJson(map))
    }

    private fun updateFirmware() {
        if (!mBean.online) {
            toastMsg("设备不在线")
            return
        }
        if (!mCheckUpdateBean.update) {
            toastMsg("无需更新")
            return
        }
        val bean = mCheckUpdateBean.devFirmwareOTARawRuleVO
        val devInfoMap = HashMap<String, Any>()
        devInfoMap["binVer"] = bean.latestBinVer
        devInfoMap["devTid"] = mBean.devTid
        devInfoMap["binUrl"] = bean.binUrl
        devInfoMap["md5"] = bean.md5
        devInfoMap["binType"] = bean.latestBinType
//        devInfoMap["appTid"] = Tools.getAppTid().toString() + "web"
        devInfoMap["appTid"] =  Build.BRAND + Build.MODEL
        devInfoMap["size"] = bean.size
        devInfoMap["ctrlKey"] = mBean.ctrlKey
        val map = HashMap<String, Any>()
        map["msgId"] = KunLuHomeSdk.instance.getMsgId()
        map["action"] = "devUpgrade"
        map["params"] = devInfoMap
        KunLuHomeSdk.instance.getMsgId()
        KunLuHomeSdk.instance.getWebSocketManager()?.send(JsonUtils.toJson(map))
    }

    private val webSocketListener = object : SocketListener {
        override fun onConnected() {}
        override fun onConnectFailed(e: Throwable?) {}
        override fun onDisconnect() {}
        override fun onSendDataError(errorResponse: ErrorResponse?) {}

        override fun <T : Any?> onMessage(message: String?, data: T) {
            message?.let { getSocketMsg(message, JsonUtils.toJson(data)) }
        }

        override fun <T : Any?> onMessage(bytes: ByteBuffer?, data: T) {}
        override fun onPing(framedata: Framedata?) {}
        override fun onPong(framedata: Framedata?) {}
    }

    private fun getSocketMsg(message: String, toJson: String) {
        val map: Map<*, *> = JsonUtils.fromJson(message, MutableMap::class.java)
        if (map["action"].toString() == "devSend") {
            val params = map["params"] as Map<*, *>?
            val msgData = params!!["data"] as Map<*, *>?
            if (msgData!!["upgradeProgress"] != null) {
                val d = msgData["upgradeProgress"] as Double
                if (d >= 100) {
                    toastMsg("固件升级成功")
                    mBinding.tvDeviceInfoValue.text = getDeviceFirmwareInfo()
                }
            }
        } else if (map["action"].toString() == "devUpgradeResp") {
            try {
                val code = (map["code"] as Double?)!!.toInt()
                if (code != 200) {
                    toastMsg("固件升级失败")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (map["action"].toString() == "zigBeeDevUpgradeResp") {
            try {
                val code = map["code"] as Double
                if (code != 200.0) {
                    toastMsg("协调器升级失败")
                } else {
                    toastMsg("协调器升级成功")
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        } else if (map["action"].toString() == "gatewayLogin") {
            val params = map["params"] as Map<*, *>
            if (mBean.devTid == params["devTid"]) {
                toastMsg("协调器升级成功")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        KunLuHomeSdk.instance.getWebSocketManager()?.removeListener(webSocketListener)
    }

    private fun setData() {
        mBinding.header.load(mBean.logo) {
            transformations(CircleCropTransformation())
        }
        mBinding.tvName.text = if (mBean.deviceName.isNotEmpty()) mBean.deviceName else mBean.name
        mBinding.tvFamilyValue.text = mBean.familyName
        mBinding.tvRoomValue.text = mBean.folderName
        mBinding.tvDeviceIdValue.text = mBean.devTid
        mBinding.tvDeviceInfoValue.text = getDeviceFirmwareInfo()
    }

    private fun getDeviceFirmwareInfo(): String {
        var deviceFirmwareInfo = ""
        deviceFirmwareInfo = if (mBean.binVersion.isEmpty()) {
            "已更新"
        } else {
            if (mBean.binType.isEmpty()) {
                deviceFirmwareInfo + mBean.binVersion
            } else {
                deviceFirmwareInfo + mBean.binVersion + "（" + mBean.binType + "）"
            }
        }
        return deviceFirmwareInfo
    }

    companion object {
        const val BEAN = "bean"
    }
}