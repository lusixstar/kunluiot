package com.example.kiotsdk.ui.user

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityLoginBinding
import com.example.kiotsdk.ui.MainActivity
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.user.User
import com.kunluiot.sdk.callback.user.ILoginCallback
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.emailPhone.setText("18259158984")
        mBinding.password.setText("333333")

        mBinding.login.setOnClickListener { gotoNext() }
        mBinding.forget.setOnClickListener { startActivity<ForgetActivity>() }

//        refreshLoginToken()
    }

    private fun refreshLoginToken() {
        val token = KunLuHomeSdk.instance.getSessionBean()?.refreshToken ?: ""
        KunLuHomeSdk.userImpl.refreshToken(token, refreshCallback)
    }

    private val refreshCallback = object : ILoginCallback {

        override fun onSuccess(user: User) {
            setResult(Activity.RESULT_OK, intent)
            startActivity<MainActivity>()
            finish()
            toast("login success")
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    private fun gotoNext() {
        val account = mBinding.emailPhone.text.toString()
        val country = mBinding.countryCode.text.toString()
        val password = mBinding.password.text.toString()
        if (account.isEmpty()) {
            toast("account is empty")
            return
        }
        if (password.isEmpty()) {
            toast("password is empty")
            return
        }
        if (DemoUtils.isEmail(account)) {

        } else {
            KunLuHomeSdk.userImpl.loginWithPhonePassword(country, account, password, loginCallback)
        }
    }

    private val loginCallback = object : ILoginCallback {

        override fun onSuccess(user: User) {
            setResult(Activity.RESULT_OK, intent)
            startActivity<MainActivity>()
            finish()
            toast("login success")
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }
}