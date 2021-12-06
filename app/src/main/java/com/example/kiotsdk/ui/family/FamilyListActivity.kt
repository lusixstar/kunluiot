package com.example.kiotsdk.ui.family

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.adapter.family.FamilyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyListBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

class FamilyListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyListBinding

    private lateinit var mAdapter: FamilyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.add.setOnClickListener { gotoAdd.launch(Intent(this, FamilyCreateActivity::class.java)) }

        initAdapter()
        getFamilyData()
    }

    private val gotoAdd = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getFamilyData()
        }
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
        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            val data = adapter.data[position] as FamilyBean
            deleteFamily(data.familyId)
            false
        }

    }

    private fun deleteFamily(familyId: String) {
        alert("是否删除") {
            positiveButton("确定") { dialog ->
                if (familyId.isNotEmpty()) KunLuHomeSdk.familyImpl.delete(familyId, { code, msg -> toastErrorMsg(code, msg) }, {
                    getFamilyData()
                })
                dialog.dismiss()
            }
        }.show()
    }

    private val gotoDeleteFamilyLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getFamilyData()
        }
    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getFamilyList({ code, msg -> toastErrorMsg(code, msg) }, {
            if (!it.isNullOrEmpty()) {
                mAdapter.data.clear()
                mAdapter.addData(it)
            }
        })
    }
}