package com.example.kiotsdk.ui.user

import android.app.Activity
import android.os.Bundle
import com.blankj.utilcode.util.RegexUtils
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityLoginBinding
import com.example.kiotsdk.ui.MainNewActivity
import com.example.kiotsdk.util.DemoUtils
import com.example.kiotsdk.util.StringUtils
import com.kunluiot.sdk.KunLuHomeSdk
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

        mBinding.login.setOnClickListener { gotoNext() }
        mBinding.forget.setOnClickListener { startActivity<ForgetActivity>() }

        refreshLoginToken()
    }

    private fun refreshLoginToken() {
        val token = KunLuHomeSdk.instance.getSessionBean()?.refreshToken ?: ""
        if (token.isNotEmpty()) {
            KunLuHomeSdk.userImpl.refreshToken(token, { code, err -> toastErrorMsg(code, err) }, {
                setResult(Activity.RESULT_OK, intent)
                startActivity<MainNewActivity>()
                finish()
                toast("login success")
            })
        }
    }

    private fun gotoNext() {
        val account = mBinding.emailPhone.text.toString()
        val password = mBinding.password.text.toString()
        if (account.isEmpty()) {
            toast("account is empty")
            return
        }
        if (password.isEmpty()) {
            toast("password is empty")
            return
        }
        if (!RegexUtils.isMobileSimple(account)) {
            toast("phone error")
            return
        }
        if (StringUtils.inputJudge(password)) {
            toast("password error")
            return
        }
        KunLuHomeSdk.userImpl.login(account, password, { code, err -> toastErrorMsg(code, err) }, {
            setResult(Activity.RESULT_OK, intent)
            startActivity<MainNewActivity>()
            finish()
            toast("login success")
        })
    }
}