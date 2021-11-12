package com.example.kiotsdk.ui.device

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.adapter.family.FamilyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceListBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceListBean
import com.kunluiot.sdk.callback.device.IDeviceListCallback
import org.jetbrains.anko.toast

class DeviceListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceListBinding

    private lateinit var mAdapter: FamilyListAdapter

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
//        mAdapter = FamilyListAdapter(arrayListOf())
//        mBinding.list.adapter = mAdapter
//        mAdapter.setOnItemClickListener { adapter, _, position ->
//            val data = adapter.data[position] as FamilyCreateBean
//            val intent = Intent(this, FamilyDetailsActivity::class.java)
//            intent.putExtra(FamilyDetailsActivity.FAMILY_ID, data.familyId)
//            gotoDeleteFamilyLaunch.launch(intent)
//        }
    }

    private val gotoDeleteFamilyLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
        }
    }

    private val listCallback = object : IDeviceListCallback {

        override fun onSuccess(bean: DeviceListBean) {

        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }
}