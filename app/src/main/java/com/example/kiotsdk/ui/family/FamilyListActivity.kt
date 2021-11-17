package com.example.kiotsdk.ui.family

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.adapter.family.FamilyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyListBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.callback.family.IFamilyListCallback
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
            val data = adapter.data[position] as FamilyBean
            val intent = Intent(this, FamilyDetailsActivity::class.java)
            intent.putExtra(FamilyDetailsActivity.FAMILY_ID, data.familyId)
            gotoDeleteFamilyLaunch.launch(intent)
        }
    }

    private val gotoDeleteFamilyLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getFamilyData()
        }
    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getFamilyList(listCallback)
    }

    private val listCallback = object : IFamilyListCallback {

        override fun onSuccess(bean: List<FamilyBean>) {
            if (!bean.isNullOrEmpty()) {
                mAdapter.data.clear()
                mAdapter.addData(bean)
            }
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }
}