package com.example.kiotsdk.ui.device

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceSetWifiDetailsBinding
import com.example.kiotsdk.widget.GifView
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.ConfigWifiBean
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.thirdlib.wifi.RNHekrConfigModule
import com.kunluiot.sdk.thirdlib.ws.websocket.util.LogUtil
import com.kunluiot.sdk.util.JsonUtils
import org.jetbrains.anko.startActivity
import java.util.*

class DeviceSetWifiDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceSetWifiDetailsBinding

    private lateinit var mRNHekrConfigModule: RNHekrConfigModule

    private var mBean: DeviceProductsBean = DeviceProductsBean()
    private var mSsid: String = ""
    private var mPin: String = ""
    private var mPassword: String = ""

    private val mTotalProgress = 100 //100s
    private val mCountDownInterval = 1000 //1s = 1000ms

    private var mRespState = 0
    private val mConfigWifiList = ArrayList<ConfigWifiBean>()
    private var mCurrStep = 1
    private var mIsNewDevice = false

    private val mNewDeviceBean: ArrayList<DeviceNewBean> = arrayListOf()

    // 第二步有没有失败的标志 0表示失败， 1表示成功
    private var mSecondStepFlag = 0
    private var mThirdStepFlag = 0
    private var mFourStepFlag = 0
    private var mFiveStepFlag = 0

    private var mApModel = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceSetWifiDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mBean = it.getParcelableExtra(BEAN) ?: DeviceProductsBean()
            mSsid = it.getStringExtra(SSID) ?: ""
            mPin = it.getStringExtra(PIN) ?: ""
            mPassword = it.getStringExtra(PASSWD) ?: ""
            mApModel = it.getBooleanExtra(NET_TYPE_AP, false)
        }

        initView()
        startConfig()
    }

    private fun initView() {
        mBinding.roundProgressBar.circularProgressBar.setCircleWidth(15f)
        mBinding.roundProgressBar.setMax(mTotalProgress)

        mBinding.tvFirstStep.text = getString(R.string.obtain_the_distribution_network_security_code) + "(" + mPin + ")"

        mBinding.btnFinish.setOnClickListener { saveConfigNetwork() }
    }

    private fun saveConfigNetwork() {
        when (mRespState) {
            RESP_LOADING -> { }
            RESP_SUCCESS -> {
                if (mNewDeviceBean.isNotEmpty()) {
                    val bean = mNewDeviceBean.first()
                    startActivity<DeviceConfigFinishActivity>(
                        DeviceConfigFinishActivity.DEV_TID to bean.devTid,
                        DeviceConfigFinishActivity.MID to bean.mid,
                        DeviceConfigFinishActivity.REGISTER_ID to bean.registerId,
                        DeviceConfigFinishActivity.DEVICE_NAME to bean.deviceName,
                        DeviceConfigFinishActivity.CTRL_KEY to bean.ctrlKey,
                        DeviceConfigFinishActivity.DEVICE to bean,
                    )
                    return
                }
                mRespState = RESP_FAIL
                showConfigNetworkPage(mRespState)
            }
            RESP_FAIL -> startConfig()
        }
    }

    /**
     * 停止配网
     */
    private fun stopConfig() {
        mRNHekrConfigModule.stop(0)
    }

    /**
     * 开始配网
     */
    private fun startConfig() {
        mRNHekrConfigModule = RNHekrConfigModule(this)
        mRespState = RESP_LOADING
        mConfigWifiList.clear()
        if (mApModel) {
            mRNHekrConfigModule.softAP(mSsid, mPassword, mPin, mHandler)
        } else {
            mRNHekrConfigModule.config(mSsid, mPassword, mPin, mHandler)
        }
        mCurrStep = 2
        getDoneDevices()
        showConfigNetworkPage(mRespState)
        countDownTimer.start()
    }

    private fun showConfigNetworkPage(index: Int) {
        hideConfigNetworkPage()
        when (index) {
            RESP_LOADING -> {
                setTipsInfo(getString(R.string.complete_distribution_network), getString(R.string.countdown_of_distribution_network), getString(R.string.device_net_tips))
                showStepIndicator(true)
                mBinding.llConfigNetwork.visibility = View.VISIBLE
                mBinding.roundProgressBar.visibility = View.VISIBLE
            }
            RESP_SUCCESS -> {
                setTipsInfo(getString(R.string.complete_distribution_network), getString(R.string.distribution_network_success), "")
                showStepIndicator(false)
                mBinding.btnFinish.visibility = View.VISIBLE
                mBinding.llConfigNetwork.visibility = View.VISIBLE
                mBinding.ivConfigNetworkFinish.visibility = View.VISIBLE
            }
            RESP_FAIL -> {
                setTipsInfo(if (mCurrStep == 2) getString(R.string.retry_) else getString(R.string.retry_), getString(R.string.distribution_network_failure), getString(R.string.net_failure_tips))
                mBinding.btnFinish.visibility = View.VISIBLE
                mBinding.llConfigNetworkError.visibility = View.VISIBLE
                mBinding.tvConfigNetworkErrorTip.text = getString(R.string.device_connection_router) + "(" + mPin + ")"
                when (mCurrStep) {
                    2 -> {
                        mBinding.tvConfigNetworkError.text = getString(R.string.not_bound_to_device_please_try_to_use_compatibility_mode_to_complete_device_distribution)
                    }
                    3, 4 -> mBinding.tvConfigNetworkError.text = getString(R.string.bad_wifi)
                    5 -> {
                        val bounder: String = getBounder(mNewDeviceBean.first().bindResultMsg)
                        if (bounder.isEmpty()) {
                            mBinding.tvConfigNetworkError.text = getString(R.string.device_bound_by_sb)
                        } else {
                            val tips = String.format(getString(R.string.device_bound_not_support), bounder)
                            mBinding.tvConfigNetworkError.text = tips
                        }
                    }
                }
            }
        }
    }

    /**
     * 当设备配网第五步出错时，获取绑定者信息
     */
    private fun getBounder(bindResultMsg: String = ""): String {
        // 字符串为空
        if (bindResultMsg.isEmpty()) {
            return ""
        }
        // 不符合格式
        if (bindResultMsg.length < 4 || bindResultMsg.indexOf("E00") == -1) {
            return ""
        }
        val prefix = bindResultMsg.substring(0, 4)
        return if (prefix == "E001") {
            bindResultMsg.substring(4, bindResultMsg.length)
        } else ""
    }

    private fun setTipsInfo(btnText: String, tipName: String, tipSubName: String) {
        mBinding.btnFinish.text = btnText
        mBinding.tvTipName.text = tipName
        mBinding.tvTipSubName.text = tipSubName
    }

    private fun showStepIndicator(loading: Boolean) {
        //loading为true 表示还在加载中 则隐藏ImageView
        mBinding.ivSecondStep.visibility = if (loading) View.GONE else View.VISIBLE
        mBinding.ivThirdStep.visibility = if (loading) View.GONE else View.VISIBLE
        mBinding.ivFourStep.visibility = if (loading) View.GONE else View.VISIBLE
        mBinding.ivFiveStep.visibility = if (loading) View.GONE else View.VISIBLE
        //loading为true 表示还在加载中 则显示GifView
        mBinding.gifSecondStep.visibility = if (loading) View.VISIBLE else View.GONE
        mBinding.gifThirdStep.visibility = if (loading) View.VISIBLE else View.GONE
        mBinding.gifFourStep.visibility = if (loading) View.VISIBLE else View.GONE
        mBinding.gifFiveStep.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun hideConfigNetworkPage() {
        mBinding.btnFinish.visibility = View.INVISIBLE
        mBinding.roundProgressBar.visibility = View.GONE
        mBinding.ivConfigNetworkFinish.visibility = View.GONE
        mBinding.llConfigNetwork.visibility = View.GONE
        mBinding.llConfigNetworkError.visibility = View.GONE
    }

    /**
     * 设置Step状态
     */
    private fun setStepState(gifView: GifView, imageView: ImageView, success: Boolean) {
        gifView.visibility = View.GONE
        imageView.visibility = View.VISIBLE
        mBinding.ivFourStep.setImageResource(if (success) R.mipmap.ic_config_step_success else R.mipmap.ic_config_step_step_error)
    }

    private val mHandler = Handler(Handler.Callback { msg ->
        val message = msg.data.getString("message")
        val configWifiBean = JsonUtils.fromJson(message, ConfigWifiBean::class.java) ?: return@Callback false
        mConfigWifiList.add(mConfigWifiList.size, configWifiBean)
        when (configWifiBean.params.STEP) {
            0 -> {
            }
            1, 2, 3 -> {
                if (configWifiBean.params.code != 200) {
                    mSecondStepFlag = 0
                    setStepState(mBinding.gifSecondStep, mBinding.ivSecondStep, false)
                }
                mCurrStep = 2
            }
            4 -> {
                mSecondStepFlag = if (configWifiBean.params.code == 200) {
                    1
                } else {
                    0
                }
                setStepState(mBinding.gifSecondStep, mBinding.ivSecondStep, mSecondStepFlag == 1)
                mCurrStep = 2
            }
            5, 6, 7 -> {
                if (configWifiBean.params.code != 200) {
                    mThirdStepFlag = 0
                    setStepState(mBinding.gifThirdStep, mBinding.ivThirdStep, false)
                }
                mCurrStep = 3
            }
            8 -> {
                mThirdStepFlag = if (configWifiBean.params.code == 200) {
                    1
                } else {
                    0
                }
                mCurrStep = 3
                setStepState(mBinding.gifThirdStep, mBinding.ivThirdStep, mThirdStepFlag == 1)
            }
            9 -> {
                mFourStepFlag = if (configWifiBean.params.code == 200) {
                    1
                } else {
                    0
                }
                setStepState(mBinding.gifFourStep, mBinding.ivFourStep, mFourStepFlag == 1)
                mCurrStep = 4
            }
        }
        false
    })

    /**
     * 获取云端是否已经有设备信息数据
     */
    private fun getDoneDevices() {
        if (!mIsNewDevice) {
            KunLuHomeSdk.deviceImpl.getNewDeviceList(mSsid, mPin, listCallback)
        }
    }

    private val listCallback = object : IDeviceListCallback {

        override fun onSuccess(bean: List<DeviceNewBean>) {
            if (bean.isNullOrEmpty()) {
                return
            }
            mNewDeviceBean.clear()
            mNewDeviceBean.addAll(bean)
            mCurrStep = 5
            mIsNewDevice = true
            showStepIndicator(false)
            // bindResultCode如果为0，那么绑定成功，否则绑定失败
            if (mNewDeviceBean.first().bindResultCode == 0) {
                mRespState = RESP_SUCCESS
                mFiveStepFlag = 1
                mBinding.btnFinish.visibility = View.VISIBLE
                stopConfig()
                showConfigNetworkPage(mRespState)
            } else {
                //设备已经绑定到其他账号
                mRespState = RESP_FAIL
                mFiveStepFlag = 0
            }
            setStepState(mBinding.gifFiveStep, mBinding.ivFiveStep, mFiveStepFlag == 1)
        }

        override fun onError(code: String, error: String) {
            mRespState = RESP_FAIL
        }
    }

    private val countDownTimer = object : CountDownTimer(((mTotalProgress * mCountDownInterval).toLong()), mCountDownInterval.toLong()) {
        override fun onFinish() {
            mBinding.roundProgressBar.progress = mTotalProgress
            stopConfig()
            if (mNewDeviceBean.isNullOrEmpty()) {
                mRespState = RESP_FAIL
            }
            showConfigNetworkPage(mRespState)
        }

        override fun onTick(millisUntilFinished: Long) {
            //计时过程显示
            val progress: Int = mTotalProgress - (millisUntilFinished / 1000).toInt()
            mBinding.roundProgressBar.progress = progress
            if (progress % 5 == 0) {
                //间隔五秒请求一次数据 如果已请到云端数据则不请求
                if (mNewDeviceBean.isNullOrEmpty()) {
                    getDoneDevices()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopConfig()
        countDownTimer.cancel()
    }

    companion object {
        const val BEAN = "bean"
        const val SSID = "ssid"
        const val PIN = "pin"
        const val PASSWD = "password"

        const val NET_TYPE_AP = "type_ap"

        const val RESP_LOADING = 0 // 没有状态
        const val RESP_SUCCESS = 1 // 成功
        const val RESP_FAIL = 2 // 失败
    }
}