package com.example.kiotsdk.ui.user

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityUserEditPhoneBinding
import com.kunluiot.sdk.KunLuHomeSdk
import org.jetbrains.anko.startActivity


class UserEditPhoneActivity : BaseActivity() {

    private lateinit var mBinding: ActivityUserEditPhoneBinding

    private var mPhone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUserEditPhoneBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mPhone = it.getStringExtra(PHONE) ?: "" }
        mBinding.nowPhone.text = "当前手机号：$mPhone"

        mBinding.btnFinish.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        startActivity<VerifyPhoneNumActivity>(VerifyPhoneNumActivity.PHONE to mPhone)
    }

    companion object {
        const val PHONE = "phone"
    }
}