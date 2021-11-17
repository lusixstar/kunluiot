package com.example.kiotsdk.ui.device

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceGatewayDetailsBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.common.BaseSocketBean
import com.kunluiot.sdk.bean.device.ConfigNetworkBean
import com.kunluiot.sdk.bean.device.ConfigZigBeeBean
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.callback.device.IConfigNetworkCallback
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.thirdlib.ws.websocket.SocketListener
import com.kunluiot.sdk.thirdlib.ws.websocket.response.ErrorResponse
import com.kunluiot.sdk.util.JsonUtils
import org.java_websocket.framing.Framedata
import org.jetbrains.anko.toast
import java.nio.ByteBuffer


class DeviceGateWayDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceGatewayDetailsBinding

    private var mBean: DeviceProductsBean = DeviceProductsBean()
    private var mGatewayBean: DeviceNewBean = DeviceNewBean()

    private val mTotalProgress = 100 //100s
    private val mCountDownInterval = 1000 //1s = 1000ms

    private var mRespState = 0

    private var mConfigWifiBean: ConfigZigBeeBean = ConfigZigBeeBean()
    private val mSubDeviceList: MutableList<DeviceNewBean> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceGatewayDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mBean = it.getParcelableExtra(BEAN) ?: DeviceProductsBean()
            mGatewayBean = it.getParcelableExtra(GATEWAY_BEAN) ?: DeviceNewBean()
        }

        initView()
        startConfig()
        KunLuHomeSdk.instance.getWebSocketManager()?.addListener(webSocketListener)
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
        if (msg.action == KunLuDeviceType.DEVICE_ADD_SUB_DEV) {
            val configNetworkBean: ConfigZigBeeBean = JsonUtils.fromJson(message, ConfigZigBeeBean::class.java)
            mConfigWifiBean = configNetworkBean
            KunLuHomeSdk.deviceImpl.getSubDevice(mGatewayBean.ctrlKey, configNetworkBean.params.subDevTid, KunLuDeviceType.DEVICE_SUB, true, deviceCallback)
        }
    }

    private val deviceCallback = object : IDeviceListCallback {

        override fun onSuccess(bean: List<DeviceNewBean>) {
            gotoNext(bean)
        }

        override fun onError(code: String, error: String) {
            toast("code : $code,  error: $error")
        }
    }

    private fun gotoNext(list: List<DeviceNewBean>) {
        if (list.isNullOrEmpty()) {
            return
        }
        mSubDeviceList.clear()
        mSubDeviceList.addAll(list)
        mRespState = RESP_SUCCESS
        mBinding.gifSecondStep.visibility = View.GONE
        mBinding.gifThirdStep.visibility = View.GONE
        mBinding.ivSecondStep.visibility = View.VISIBLE
        mBinding.ivThirdStep.visibility = View.VISIBLE
        mBinding.btnFinish.visibility = View.VISIBLE
        mBinding.btnFinish.text = getString(R.string.complete_distribution_network)
        mBinding.roundProgressBar.visibility = View.INVISIBLE
        mBinding.configResult.visibility = View.VISIBLE
        countDownTimer.cancel()
    }

    private fun initView() {
        mBinding.roundProgressBar.circularProgressBar.setCircleWidth(15f)
        mBinding.roundProgressBar.setMax(mTotalProgress)

        mBinding.btnFinish.setOnClickListener { saveConfigNetwork() }
    }

    private fun saveConfigNetwork() {
        when (mRespState) {
            RESP_LOADING -> { }
            RESP_SUCCESS -> {
                if (mSubDeviceList.size > 0) {
                    val devType: String = mSubDeviceList[0].devTid
                    var deviceName: String = mSubDeviceList[0].name
                    if (deviceName.isEmpty()) {
                        deviceName = mSubDeviceList[0].productName.zh_CN
                    }
//                    if (devType == KunLuDeviceType.DEVICE_SUB) {
//                        startActivity(Intent(this, ConfigNetworkFinishActivity::class.java)
//                            .putExtra("devTid", mSubDeviceList[0].getParentDevTid())
//                            .putExtra("branchNames", JsonUtils.toJson(mSubDeviceList[0].getBranchNames()))
//                            .putExtra("subDevTid", mSubDeviceList[0].getDevTid())
//                            .putExtra("resisterId", mSubDeviceList[0].getRegisterId())
//                            .putExtra("mid", mSubDeviceList[0].getMid()).putExtra("deviceName", deviceName)
//                            .putExtra("ctrlKey", mSubDeviceList[0].getParentCtrlKey()))
//                    } else {
//                        startActivity(Intent(this, ConfigNetworkFinishActivity::class.java)
//                            .putExtra("devTid", mSubDeviceList[0].getDevTid())
//                            .putExtra("deviceName", deviceName)
//                            .putExtra("branchNames", JsonUtils.toJson(mSubDeviceList[0].getBranchNames()))
//                            .putExtra("resisterId", mSubDeviceList[0].getRegisterId())
//                            .putExtra("mid", mSubDeviceList[0].getMid())
//                            .putExtra("ctrlKey", mSubDeviceList[0].getCtrlKey()))
//                    }
                    return
                }
                mRespState = RESP_FAIL
                showConfigNetworkPage(mRespState)
            }
            RESP_FAIL -> startConfig()
        }
    }

    /**
     * 开始配网
     */
    private fun startConfig() {
        mRespState = RESP_LOADING
        mBinding.gifSecondStep.visibility = View.VISIBLE
        mBinding.gifThirdStep.visibility = View.VISIBLE
        mBinding.ivSecondStep.visibility = View.GONE
        mBinding.ivThirdStep.visibility = View.GONE
        showConfigNetworkPage(mRespState)
        if (mBean.mid.isNotEmpty() && mGatewayBean.devTid.isNotEmpty() && mGatewayBean.ctrlKey.isNotEmpty()) {
            KunLuHomeSdk.deviceImpl.deviceControl(95, mBean.mid, mGatewayBean.devTid, mGatewayBean.ctrlKey, controlCallback)
        }
        countDownTimer.start()
    }

    /**
     * 停止配网
     */
    private fun stopConfig() {
        if (mBean.mid.isNotEmpty() && mGatewayBean.devTid.isNotEmpty() && mGatewayBean.ctrlKey.isNotEmpty()) {
            KunLuHomeSdk.deviceImpl.deviceControl(0, mBean.mid, mGatewayBean.devTid, mGatewayBean.ctrlKey, controlCallback)
        }
    }

    private fun showConfigNetworkPage(index: Int) {
        hideConfigNetworkPage()
        when (index) {
            RESP_LOADING -> {
                mBinding.btnFinish.text = getString(R.string.complete_distribution_network)
                mBinding.tvTipName.text = getString(R.string.countdown_of_distribution_network)
                mBinding.tvTipSubName.text = getString(R.string.routers_mobile_phones_and_devices_should_be_as_close_as_possible)
                mBinding.llConfigNetwork.visibility = View.VISIBLE
            }
            RESP_SUCCESS -> {
                mBinding.btnFinish.text = getString(R.string.complete_distribution_network)
                mBinding.tvTipName.text = getString(R.string.distribution_network_success)
                mBinding.tvTipSubName.text = getString(R.string.gateway_has_found_this_type_of_sub_device)
                mBinding.btnFinish.visibility = View.VISIBLE
                mBinding.llConfigNetworkFinish.visibility = View.VISIBLE
            }
            RESP_FAIL -> {
                mBinding.btnFinish.text = getString(R.string.retry_)
                mBinding.tvTipName.text = getString(R.string.distribution_network_failure)
                mBinding.tvTipSubName.text = getString(R.string.the_gateway_did_not_search_for_this_type_of_sub_device)
                mBinding.btnFinish.visibility = View.VISIBLE
                mBinding.llConfigNetworkError.visibility = View.VISIBLE
            }
        }
    }

    private fun hideConfigNetworkPage() {
        mBinding.btnFinish.visibility = View.INVISIBLE
        mBinding.llConfigNetworkFinish.visibility = View.GONE
        mBinding.llConfigNetworkError.visibility = View.GONE
        mBinding.llConfigNetwork.visibility = View.GONE
    }

    private val controlCallback = object : IConfigNetworkCallback {

        override fun onSuccess(bean: List<ConfigNetworkBean>) {

        }

        override fun onError(code: String, error: String) {
            mRespState = RESP_FAIL
        }
    }

    private val countDownTimer = object : CountDownTimer(((mTotalProgress * mCountDownInterval).toLong()), mCountDownInterval.toLong()) {
        override fun onFinish() {
            mBinding.roundProgressBar.progress = mTotalProgress
            if (mConfigWifiBean.params.devTid.isEmpty()) {
                mRespState = RESP_FAIL
            }
            showConfigNetworkPage(mRespState)
        }

        override fun onTick(millisUntilFinished: Long) {
            mBinding.roundProgressBar.progress = mTotalProgress - (millisUntilFinished / 1000).toInt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopConfig()
        KunLuHomeSdk.instance.getWebSocketManager()?.removeListener(webSocketListener)
        countDownTimer.cancel()
    }

    companion object {
        const val BEAN = "bean"
        const val GATEWAY_BEAN = "gateway_bean"

        const val RESP_LOADING = 0 // 没有状态
        const val RESP_SUCCESS = 1 // 成功
        const val RESP_FAIL = 2 // 失败
    }
}