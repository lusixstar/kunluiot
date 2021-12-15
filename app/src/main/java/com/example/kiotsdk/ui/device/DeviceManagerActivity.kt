package com.example.kiotsdk.ui.device

import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.adapter.device.DeviceRoomListAdapter
import com.example.kiotsdk.adapter.diff.DiffRoomListCallback
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceRoomListBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.common.BaseSocketBean
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.thirdlib.ws.websocket.SocketListener
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse
import com.kunluiot.sdk.util.JsonUtils
import org.java_websocket.framing.Framedata
import org.jetbrains.anko.alert
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity
import java.nio.ByteBuffer
import java.util.*


class DeviceManagerActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceRoomListBinding

    private lateinit var mRoomAdapter: DeviceRoomListAdapter

    private var mFamilyList = mutableListOf<FamilyBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceRoomListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }


        mBinding.title.setOnClickListener { selectFamily() }

        initAdapter()
        getFamilyData()
        KunLuHomeSdk.instance.getWebSocketManager()?.addListener(webSocketListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        KunLuHomeSdk.instance.getWebSocketManager()?.removeListener(webSocketListener)
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
        val msg: BaseSocketBean = JsonUtils.fromJson(toJson, BaseSocketBean::class.java)
        if (msg.action == "devDeleteResp") {
            getFamilyData()
        }
    }

    private fun initAdapter() {
        mRoomAdapter = DeviceRoomListAdapter(mutableListOf(), { gotoNext(it) }, { gotoDelete(it) })
        mRoomAdapter.setDiffCallback(DiffRoomListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mRoomAdapter
    }

    private fun gotoNext(bean: DeviceNewBean) {
        startActivity<DeviceInfoActivity>(DeviceInfoActivity.BEAN to bean)
    }

    private fun gotoDelete(it: DeviceNewBean) {
        val userId = KunLuHomeSdk.instance.getSessionBean()?.user ?: ""
        alert("是否删除设备") {
            positiveButton("确定") { dialog ->
                if (it.devType == KunLuDeviceType.DEVICE_SUB) {
                    KunLuHomeSdk.deviceImpl.deletesSubDevice(it.parentDevTid, it.parentCtrlKey, it.devTid, { code, msg -> toastErrorMsg(code, msg) }, {
                        val params: MutableMap<String, Any> = HashMap()
                        params["devTid"] = it.parentDevTid
                        params["ctrlKey"] = it.parentCtrlKey
                        params["subDevTid"] = it.devTid
                        params["randomToken"] = ""
                        val resp: MutableMap<String, Any> = HashMap()
                        resp["action"] = "devDelete"
                        resp["params"] = params
                        KunLuHomeSdk.instance.getWebSocketManager()?.send(JsonUtils.toJson(resp))
                    })
                } else {
                    if (userId == it.ownerUid) {
                        KunLuHomeSdk.deviceImpl.deleteDevice(it.devTid, it.bindKey, { code, msg -> toastErrorMsg(code, msg) }, { getFamilyData() })
                    } else {
                        KunLuHomeSdk.deviceImpl.deleteAuthorizationDevice(it.ownerUid, it.ctrlKey, userId, it.devTid, "", { code, msg -> toastErrorMsg(code, msg) }, { getFamilyData() })
                    }
                }
                dialog.dismiss()
            }
            negativeButton("取消") { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun selectFamily() {
        if (mFamilyList.isNotEmpty()) {
            val listName = mFamilyList.map { it.familyName }
            selector(title = "选择家庭", items = listName) { dialog, index ->
                mBinding.title.text = "当前选择家庭是:${mFamilyList[index].familyName}"
                getRoomsDevice(mFamilyList[index].familyId)
                dialog.dismiss()
            }
        } else {
            getFamilyData()
        }
    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getFamilyList({ code, msg -> toastErrorMsg(code, msg) }, { setFamilyData(it) })
    }

    private fun setFamilyData(list: List<FamilyBean>) {
        if (!list.isNullOrEmpty()) {
            mFamilyList.clear()
            mFamilyList.addAll(list)
            mBinding.title.text = "当前选择家庭是:${mFamilyList.first().familyName}"
            getRoomsDevice(mFamilyList.first().familyId)
        }
    }

    private fun getRoomsDevice(familyId: String) {
        KunLuHomeSdk.familyImpl.getRoomsDevice(familyId, false, 0, 20, { code, msg -> toastErrorMsg(code, msg) }, { setRoomData(it) })
    }

    private fun setRoomData(it: List<FolderBean>) {
        if (!it.isNullOrEmpty()) {
            mRoomAdapter.data.clear()
            mRoomAdapter.addData(it)
        }
    }
}