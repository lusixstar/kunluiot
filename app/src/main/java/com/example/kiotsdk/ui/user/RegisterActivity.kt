package com.example.kiotsdk.ui.user

import android.os.Bundle
import android.os.CountDownTimer
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityRegisterBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.user.KunLuUserType
import com.kunluiot.sdk.bean.user.VerifyCodeBean
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity() {

    private lateinit var mBinding: ActivityRegisterBinding

    private var mRid = ""

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
        val code = mBinding.verifyCode.text.toString()
        val password = mBinding.password.text.toString()
        if (account.isEmpty()) {
            toast("account is empty")
            return
        }
        if (code.isEmpty()) {
            toast("code is empty")
            return
        }
        if (password.isEmpty() || password.length < 6) {
            toast("密码不能为空 最少6位长度")
            return
        }
        KunLuHomeSdk.userImpl.checkVerifyCode(account, KunLuUserType.SEND_CODE_REGISTER, code, { c, err -> toastErrorMsg(c, err) }, { finishRegister(it) })
    }

    private fun finishRegister(bean: VerifyCodeBean) {
        val password = mBinding.password.text.toString()
        KunLuHomeSdk.userImpl.register(bean.phoneNumber, password, bean.token, { code, err -> toastErrorMsg(code, err) }, { toastMsg("register success") })
    }

    private fun sendCode() {
        val phone = mBinding.emailPhone.text.toString()
        if (phone.isEmpty()) {
            toast("phone is empty")
            return
        }
        getSMSCode()
    }

    private fun getSMSCode() {
        val phone = mBinding.emailPhone.text.toString()
        KunLuHomeSdk.userImpl.getVerifyCode(phone, KunLuUserType.SEND_CODE_REGISTER, { code, err -> toastErrorMsg(code, err) }, { toastMsg("send SMS success") })
        countDownTimer.start()
    }

    private var smsTime: Long = 60 * 1000
    private val countDownTimer = object : CountDownTimer(smsTime, 1000) {

        override fun onFinish() {
            smsTime = 60 * 1000
            mBinding.send.text = resources.getString(R.string.send_verify_code)
            mBinding.send.isEnabled = true
        }

        override fun onTick(millisUntilFinished: Long) {
            mBinding.send.text = "${millisUntilFinished / 1000}S"
            mBinding.send.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}