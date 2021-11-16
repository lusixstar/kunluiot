package com.example.kiotsdk.ui.device

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceGatewayDetailsBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.device.INewDeviceCallback
import org.jetbrains.anko.toast


class DeviceGateWayDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceGatewayDetailsBinding

    private var mBean: DeviceProductsBean = DeviceProductsBean()
    private var mGatewayBean: DeviceNewBean = DeviceNewBean()

    private val mTotalProgress = 100 //100s
    private val mCountDownInterval = 1000 //1s = 1000ms

    private var mRespState = 0


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
    }

    private fun initView() {
        mBinding.roundProgressBar.circularProgressBar.setCircleWidth(15f)
        mBinding.roundProgressBar.setMax(mTotalProgress)
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

    private val controlCallback = object : IResultCallback {

        override fun onSuccess() {

        }

        override fun onError(code: String, error: String) {
            mRespState = RESP_FAIL
        }
    }

    private val countDownTimer = object : CountDownTimer(((mTotalProgress * mCountDownInterval).toLong()), mCountDownInterval.toLong()) {
        override fun onFinish() {
            mBinding.roundProgressBar.progress = mTotalProgress
//            if (mConfigNetwork == null) {
                mRespState = RESP_FAIL
//            }
            showConfigNetworkPage(mRespState)
        }

        override fun onTick(millisUntilFinished: Long) {
            mBinding.roundProgressBar.progress = mTotalProgress - (millisUntilFinished / 1000).toInt()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopConfig()
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