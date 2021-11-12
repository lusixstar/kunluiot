package com.example.kiotsdk.ui.device

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.adapter.device.DeviceTabListAdapter
import com.example.kiotsdk.adapter.family.FamilyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceListBinding
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceListBean
import com.kunluiot.sdk.bean.device.DeviceProductTabBean
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import com.kunluiot.sdk.thirdlib.qrcode.util.LogUtils
import com.kunluiot.sdk.thirdlib.ws.websocket.util.LogUtil
import org.jetbrains.anko.toast

class DeviceListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceListBinding

    private lateinit var mAdapter: DeviceTabListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        initAdapter()
        getDeviceList()
    }

    private fun getDeviceList() {
        KunLuHomeSdk.deviceImpl.list(listCallback)
    }

    private fun initAdapter() {
        mAdapter = DeviceTabListAdapter(arrayListOf())
        (mBinding.listTab.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.listTab.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position -> mAdapter.selectPosition(position) }
    }


    private fun setListData(bean: List<DeviceListBean>) {
        val tabList = mutableListOf<DeviceProductTabBean>()
        bean.forEachIndexed { index, item ->
            val bean = item.category.parent
            bean.categoryName = item.categoryParentName
            if (index == 0) bean.select = true
            tabList.add(bean)
        }
        mAdapter.data.clear()
        mAdapter.addData(DemoUtils.getSingle(tabList))
    }

    private val gotoDeleteFamilyLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
        }
    }

    private val listCallback = object : IDeviceListCallback {

        override fun onSuccess(bean: List<DeviceListBean>) {
            LogUtils.e("bean == $bean")
            setListData(bean)
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }
}