package com.example.kiotsdk.ui.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import com.blankj.utilcode.util.RegexUtils
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityForgetBinding
import com.example.kiotsdk.util.DemoUtils
import com.example.kiotsdk.util.StringUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.user.KunLuUserType
import com.kunluiot.sdk.bean.user.VerifyCodeBean
import org.jetbrains.anko.toast

class ForgetActivity : BaseActivity() {

    private lateinit var mBinding: ActivityForgetBinding

    private var mRid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.send.setOnClickListener { sendCode() }
        mBinding.reset.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        val account = mBinding.emailPhone.text.toString()
        val code = mBinding.verifyCode.text.toString()
        val password = mBinding.password.text.toString()
        if (!RegexUtils.isMobileSimple(account)) {
            toast("phone error")
            return
        }
        if (StringUtils.inputJudge(password)) {
            toast("password error")
            return
        }
        if (account.isEmpty()) {
            toast("account is empty")
            return
        }
        if (code.isEmpty()) {
            toast("code is empty")
            return
        }
        if (password.isEmpty() || password.length < 6 || password.length > 20) {
            toast("password is error")
            return
        }
        KunLuHomeSdk.userImpl.checkVerifyCode(account, KunLuUserType.SEND_CODE_RESET_PASSWORD, code, { c, err -> toastErrorMsg(c, err) }, { finishResetPassword(it) })
    }

    private fun finishResetPassword(bean: VerifyCodeBean) {
        val password = mBinding.password.text.toString()
        val verifyCode = mBinding.verifyCode.text.toString()
        KunLuHomeSdk.userImpl.resetPassword(bean.phoneNumber, password, bean.token, verifyCode, { c, err -> toastErrorMsg(c, err) }, { toastMsg("resetPassword success") })
    }

    private fun sendCode() {
        val phone = mBinding.emailPhone.text.toString()
        if (phone.isEmpty()) {
            toast("phone is empty")
            return
        }
        if (!RegexUtils.isMobileSimple(phone)) {
            toast("phone error")
            return
        }
        getSMSCode()
    }

    private fun getSMSCode() {
        val phone = mBinding.emailPhone.text.toString()
        KunLuHomeSdk.userImpl.getVerifyCode(phone, KunLuUserType.SEND_CODE_RESET_PASSWORD, { code, err -> toastErrorMsg(code, err) }, { toastMsg("send SMS success") })
        countDownTimer.start()
    }

    /**
     * 将Base64字符串形式存储的图片转成Bitmap
     */
    private fun base64CodeToBitmap(base64Code: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val bitmapArray = Base64.decode(base64Code, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
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