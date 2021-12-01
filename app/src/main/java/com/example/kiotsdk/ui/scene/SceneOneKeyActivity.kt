package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffOneKeyListCallback
import com.example.kiotsdk.adapter.scene.SceneOneKeyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneOneKeyBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.BaseSocketBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.thirdlib.ws.websocket.SocketListener
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse
import com.kunluiot.sdk.util.JsonUtils
import org.java_websocket.framing.Framedata
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import java.nio.ByteBuffer
import java.util.*

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneOneKeyActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneOneKeyBinding

    private lateinit var mAdapter: SceneOneKeyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneOneKeyBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.add.setOnClickListener { gotoOneKey.launch(Intent(this, SceneOneKeyAddOrEditActivity::class.java).putExtra(SceneOneKeyAddOrEditActivity.IS_ADD, true)) }

        initAdapter()
        getData()
        KunLuHomeSdk.instance.getWebSocketManager()?.addListener(webSocketListener)
    }

    private val gotoOneKey = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        KunLuHomeSdk.instance.getWebSocketManager()?.removeListener(webSocketListener)
    }

    private fun getSocketMsg(message: String, toJson: String) {
        val msg: BaseSocketBean = JsonUtils.fromJson(toJson, BaseSocketBean::class.java)
        if (msg.action == "sceneTriggerSendResp" && msg.code == 200) {
            toastMsg("指令发送成功")
        }
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

    private fun getData() {
        KunLuHomeSdk.sceneImpl.getOneKeySceneList({ code, msg -> toastErrorMsg(code, msg) }, { setData(it) })
    }

    private fun setData(list: List<SceneOneKeyBean> = listOf()) {
        mAdapter.setDiffNewData(list as MutableList<SceneOneKeyBean>)
    }

    private fun initAdapter() {
        mAdapter = SceneOneKeyListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffOneKeyListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.edit, R.id.play)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as SceneOneKeyBean
            when (view.id) {
                R.id.edit -> {
                    startActivity<SceneOneKeyAddOrEditActivity>(SceneOneKeyAddOrEditActivity.BEAN to bean)
                }
                R.id.play -> {
                    sendSceneOperation(bean.sceneId, bean.uid)
                }
            }
        }
        mAdapter.setOnItemLongClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as SceneOneKeyBean
            gotoDelete(bean)
            false
        }
    }

    private fun sendSceneOperation(sceneId: String, uid: String) {
        val devInfoMap = HashMap<String, Any>()
        devInfoMap["uid"] = uid
        devInfoMap["sceneId"] = sceneId
        val map = HashMap<String, Any>()
        map["msgId"] = KunLuHomeSdk.instance.getMsgId()
        map["action"] = "sceneTriggerSend"
        map["params"] = devInfoMap
        KunLuHomeSdk.instance.getWebSocketManager()?.send(JsonUtils.toJson(map))
    }

    private fun gotoDelete(bean: SceneOneKeyBean) {
        alert(message = "是否删除") {
            positiveButton("确定") { dialog ->
                KunLuHomeSdk.sceneImpl.deleteOneKeyScene(bean.sceneId, object : IResultCallback {
                    override fun onSuccess() {
                        getData()
                    }

                    override fun onError(code: String, error: String) {
                        toastErrorMsg(code, error)
                    }
                })
                dialog.dismiss()
            }
            negativeButton("取消") { dialog -> dialog.dismiss() }
        }.show()
    }
}