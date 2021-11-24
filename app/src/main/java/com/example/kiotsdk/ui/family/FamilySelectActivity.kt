package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.adapter.family.FamilyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilySelectBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.callback.family.IFamilyListCallback
import org.jetbrains.anko.toast

class FamilySelectActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilySelectBinding

    private lateinit var mAdapter: FamilyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilySelectBinding.inflate(layoutInflater)
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
            adapter.data.forEachIndexed { index, it ->
                if ((it as FamilyBean).current) {
                    it.current = false
                    adapter.notifyItemChanged(index)
                }
            }
            val data = adapter.data[position] as FamilyBean
            data.current = true
            adapter.notifyItemChanged(position)
            intent.putExtra(CURRENT_FAMILY, data.familyName)
            setResult(Activity.RESULT_OK, intent)
            toast("select family: ${data.familyName}")
        }
    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getFamilyList({ code, msg -> toastErrorMsg(code, msg) }, {
            if (!it.isNullOrEmpty()) {
                mAdapter.addData(it)
            }
        })
    }

    companion object {
        const val CURRENT_FAMILY: String = "current_family"
    }
}