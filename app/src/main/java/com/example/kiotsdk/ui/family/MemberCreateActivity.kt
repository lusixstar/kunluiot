package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyMemberCreateBinding
import com.kunluiot.sdk.KunLuHomeSdk
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast

class MemberCreateActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyMemberCreateBinding

    private var mGender = ""
    private var mFamilyId = ""
    private var mType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyMemberCreateBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mFamilyId = it.getStringExtra(FAMILY_ID) ?: ""
            mType = it.getStringExtra(FAMILY_TYPE) ?: ""
        }
        mBinding.sexLayout.setOnClickListener { selectGender() }
        mBinding.create.setOnClickListener { gotoNext() }
    }

    private fun selectGender() {
        val list = listOf("男", "女", "小孩")
        selector("选择", list) { dialog, i ->
            when (i) {
                0 -> mGender = "man"
                1 -> mGender = "woman"
                2 -> mGender = "children"
            }
            mBinding.gender.text = list[i]
            dialog.dismiss()
        }
    }

    private fun gotoNext() {
        val phone = mBinding.phone.text.toString()
        val name = mBinding.name.text.toString()
        if (phone.isEmpty()) {
            toast("phone is empty")
            return
        }
        if (name.isEmpty()) {
            toast("name is empty")
            return
        }
        if (mType.isEmpty()) {
            toast("gender is empty")
            return
        }
        KunLuHomeSdk.familyImpl.addFamilyMember(mFamilyId, phone, name, mGender, mType, { code, msg -> toastErrorMsg(code, msg) }, {
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

    companion object {
        const val FAMILY_ID = "family_id"
        const val FAMILY_TYPE = "family_type"
    }
}