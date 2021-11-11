package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.adapter.family.FamilyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyCreateBinding
import com.example.kiotsdk.databinding.ActivityFamilySelectBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyCreateBean
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
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
                if ((it as FamilyCreateBean).current) {
                    it.current = false
                    adapter.notifyItemChanged(index)
                }
            }
            val data = adapter.data[position] as FamilyCreateBean
            data.current = true
            adapter.notifyItemChanged(position)
            intent.putExtra(CURRENT_FAMILY, data.familyName)
            setResult(Activity.RESULT_OK, intent)
            toast("select family: ${data.familyName}")
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

    companion object {
        const val CURRENT_FAMILY : String = "current_family"
    }
}