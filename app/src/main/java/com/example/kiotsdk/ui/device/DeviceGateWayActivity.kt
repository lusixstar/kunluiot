package com.example.kiotsdk.ui.device

import android.app.AlertDialog
import android.os.Bundle
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceGatewayBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.device.DeviceProductsBean
import com.kunluiot.sdk.callback.device.INewDeviceCallback
import com.kunluiot.sdk.thirdlib.ws.websocket.util.LogUtil
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class DeviceGateWayActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceGatewayBinding

    private var mBean: DeviceProductsBean = DeviceProductsBean()

    private var mCurrentSelectGateway = ""

    private lateinit var mGatewayNameArray: Array<String>
    private var mGatewayBeanList: List<DeviceNewBean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceGatewayBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mBean = it.getParcelableExtra(DeviceSetWifiDetailsActivity.BEAN) ?: DeviceProductsBean()
        }

        mBinding.name.text = "当前选择的网关是:$mCurrentSelectGateway"
        mBinding.addDevice.setOnClickListener { gotoNext() }
        mBinding.selectGateway.setOnClickListener {
            if (!mGatewayNameArray.isNullOrEmpty() && !mGatewayBeanList.isNullOrEmpty()) {
                showGatewayList(mGatewayNameArray)
            } else {
                getGatewayData()
            }
        }
        getGatewayData()
    }

    private fun gotoNext() {
        if (mGatewayBeanList.isNullOrEmpty() || mCurrentSelectGateway.isEmpty()) {
            toast("please select gateway")
        } else {
            mGatewayBeanList.first { mCurrentSelectGateway.contains(it.name) }.let { bean ->
                if (!bean.online) {
                    if(bean.registerId.isNullOrEmpty()) bean.registerId = ""
                    if(bean.bindResultMsg.isNullOrEmpty()) bean.bindResultMsg = ""
                    startActivity<DeviceGateWayDetailsActivity>(DeviceGateWayDetailsActivity.BEAN to mBean, DeviceGateWayDetailsActivity.GATEWAY_BEAN to bean)
                } else {
                    toast(R.string.gateway_offline)
                }
            }
        }
    }

    private fun getGatewayData() {
        KunLuHomeSdk.deviceImpl.getGateway(true, KunLuDeviceType.DEVICE_GATEWAY, gatewayCallback)
    }

    private val gatewayCallback = object : INewDeviceCallback {

        override fun onSuccess(bean: List<DeviceNewBean>) {
            if (!bean.isNullOrEmpty()) {
                val list = bean.map {
                    val online = if (it.online) "     {在线}" else "     {不在线}"
                    "${it.deviceName}$online"
                }.toTypedArray()
                mGatewayBeanList = bean
                mGatewayNameArray = list
                showGatewayList(list)
            }
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    private fun showGatewayList(bean: Array<String>) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.select_gateway))
        builder.setItems(bean) { dialog, which ->
            mCurrentSelectGateway = bean[which]
            mBinding.name.text = "当前选择的网关是:$mCurrentSelectGateway"
            dialog.dismiss()
        }
        builder.create().show()
    }

    companion object {
        const val BEAN = "bean"
    }
}