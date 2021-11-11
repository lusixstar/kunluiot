package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.adapter.family.FamilyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyCreateBinding
import com.example.kiotsdk.databinding.ActivityFamilyListBinding
import com.example.kiotsdk.databinding.ActivityFamilySelectBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyCreateBean
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyListCallback
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class FamilyListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyListBinding

    private lateinit var mAdapter: FamilyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        initAdapter()
        getFamilyData()
    }

    private fun initAdapter() {
        mAdapter = FamilyListAdapter(arrayListOf())
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val data = adapter.data[position] as FamilyCreateBean
            startActivity<FamilyDetailsActivity>(FamilyDetailsActivity.FAMILY_ID to data.familyId)
        }
    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getHomeList(listCallback)
    }

    private val listCallback = object : IFamilyListCallback {

        override fun onSuccess(bean: List<FamilyCreateBean>) {
            if (!bean.isNullOrEmpty()) {
                mAdapter.addData(bean)
            }
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }
}