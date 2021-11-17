package com.example.kiotsdk.ui.family

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyDetailsBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.family.IFamilyDetailsCallback
import org.jetbrains.anko.toast

class FamilyDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyDetailsBinding

    private var mFamilyId = ""

    private var mFamilyBean: FamilyBean = FamilyBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.edit.setOnClickListener { gotoEdit() }
        mBinding.delete.setOnClickListener { deleteFamily() }

        getFamilyDetails()
    }

    private fun gotoEdit() {
        val intent = Intent(this, FamilyEditActivity::class.java)
        intent.putExtra(FamilyEditActivity.FAMILY_ID, mFamilyId)
        gotoUpdateLaunch.launch(intent)
    }

    private val gotoUpdateLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun deleteFamily() {
        if (mFamilyId.isNotEmpty()) KunLuHomeSdk.familyImpl.delete(mFamilyId, deleteCallback)
    }

    private val deleteCallback = object : IResultCallback {

        override fun onSuccess() {
            setResult(Activity.RESULT_OK)
            finish()
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    private fun setFamilyData(bean: FamilyBean) {
        mFamilyBean = bean
        mBinding.id.text = bean.familyId
        mBinding.name.text = bean.familyName
        mBinding.city.text = bean.detailAddress
    }

    private fun getFamilyDetails() {
        mFamilyId = intent.getStringExtra(FAMILY_ID) ?: ""
        if (mFamilyId.isNotEmpty()) KunLuHomeSdk.familyImpl.getFamilyDetails(mFamilyId, detailsCallback)
    }

    private val detailsCallback = object : IFamilyDetailsCallback {

        override fun onSuccess(bean: FamilyBean) {
            setFamilyData(bean)
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    companion object {
        const val FAMILY_ID = "family_id"
    }
}