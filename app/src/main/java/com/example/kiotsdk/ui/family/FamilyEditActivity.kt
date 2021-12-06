package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyEditBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import org.jetbrains.anko.toast

class FamilyEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyEditBinding

    private var mFamilyId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.finish.setOnClickListener { updateFamily() }

        getFamilyDetails()
    }

    private fun updateFamily() {
        val name = mBinding.etName.text.trim().toString()
        val city = mBinding.etCity.text.trim().toString()
        if (name.isEmpty()) {
            toast("name is empty")
            return
        }
        if (mFamilyId.isNotEmpty()) KunLuHomeSdk.familyImpl.update(mFamilyId, name, city, { code, msg -> toastErrorMsg(code, msg) }, {
            setResult(Activity.RESULT_OK)
            toast("update success")
            finish()
        })
    }

    private fun setFamilyData(bean: FamilyBean) {
        mBinding.etName.setText(bean.familyName)
        mBinding.etCity.setText(bean.detailAddress)
    }

    private fun getFamilyDetails() {
        mFamilyId = intent.getStringExtra(FamilyDetailsActivity.FAMILY_ID) ?: ""
        if (mFamilyId.isNotEmpty()) KunLuHomeSdk.familyImpl.getFamilyDetails(mFamilyId, { code, msg -> toastErrorMsg(code, msg) }, { setFamilyData(it) })
    }

    companion object {
        const val FAMILY_ID = "family_id"
    }
}