package com.example.kiotsdk.ui.user

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityRegisterBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.user.KunLuUserType
import com.kunluiot.sdk.callback.IResultCallback
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity() {

    private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.send.setOnClickListener { sendCode() }
        mBinding.register.setOnClickListener { gotoRegister() }
    }

    private fun gotoRegister() {
        val account = mBinding.emailPhone.text.toString()
        val area = mBinding.countryCode.text.toString()
        if (account.isEmpty()) {
            toast("account is empty")
            return
        }
        KunLuHomeSdk.userImpl.getVerifyCode(account, KunLuUserType.SEND_CODE_REGISTER, area, callback)
    }

    private fun sendCode() {
        val account = mBinding.emailPhone.text.toString()
        val area = mBinding.countryCode.text.toString()
        if (account.isEmpty()) {
            toast("account is empty")
            return
        }
        KunLuHomeSdk.userImpl.getVerifyCode(account, KunLuUserType.SEND_CODE_REGISTER, area, callback)
    }

    private val callback = object : IResultCallback {
        override fun onError(code: String, error: String) {
            toast("code: $code error: $error")
        }

        override fun onSuccess() {

        }
    }
}