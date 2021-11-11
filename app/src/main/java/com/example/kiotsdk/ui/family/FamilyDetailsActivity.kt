package com.example.kiotsdk.ui.family

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyCreateBinding
import com.example.kiotsdk.databinding.ActivityFamilyDetailsBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyCreateBean
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import org.jetbrains.anko.toast

class FamilyDetailsActivity :BaseActivity() {

    private lateinit var mBinding: ActivityFamilyDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        getFamilyDetails()
    }

    private fun getFamilyDetails() {
        val familyId = intent.getStringExtra(FAMILY_ID) ?: ""
        if (familyId.isNotEmpty()) KunLuHomeSdk.familyImpl.getHomeDetails(familyId, detailsCallback)
    }

    private val detailsCallback = object : IFamilyDetailsCallback {

        override fun onSuccess(bean: FamilyCreateBean) {
            toast("get details success name: ${bean.familyName}")
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    companion object {
        const val FAMILY_ID = "family_id"
    }
}