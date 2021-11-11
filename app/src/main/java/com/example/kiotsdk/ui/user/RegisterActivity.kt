package com.example.kiotsdk.ui.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityRegisterBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.user.KunLuUserType
import com.kunluiot.sdk.bean.user.CheckVerifyImageBean
import com.kunluiot.sdk.bean.user.User
import com.kunluiot.sdk.bean.user.VerifyCodeBean
import com.kunluiot.sdk.bean.user.VerifyImageBean
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.user.ICheckVerifyImageCallback
import com.kunluiot.sdk.callback.user.ICodeCallback
import com.kunluiot.sdk.callback.user.IRegisterCallback
import com.kunluiot.sdk.callback.user.IVerifyImageCallback
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
        val area = mBinding.countryCode.text.toString()
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
        if (password.isEmpty()) {
            toast("password is empty")
            return
        }
        KunLuHomeSdk.userImpl.checkVerifyCode(account, KunLuUserType.SEND_CODE_REGISTER, area, code, verifyCallback)
    }

    private val verifyCallback = object : ICodeCallback {
        override fun onError(code: String, error: String) {
            toast("code: $code error: $error")
        }

        override fun onSuccess(bean: VerifyCodeBean) {
            finishRegister(bean)
        }
    }

    private fun finishRegister(bean: VerifyCodeBean) {
        val password = mBinding.password.text.toString()
        KunLuHomeSdk.userImpl.register(bean.phoneNumber, password, bean.token, registerCallback)
    }

    private val registerCallback = object : IRegisterCallback {
        override fun onError(code: String, error: String) {
            toast("code: $code error: $error")
        }

        override fun onSuccess(bean: User) {
            toast("register success")
        }
    }

    private fun sendCode() {
        if (mBinding.verifyImageLayout.visibility != View.VISIBLE) {
            getVerifyImage()
        } else {
            val imageCode = mBinding.etVerifyImage.text.toString()
            val phone = mBinding.emailPhone.text.toString()
            if (imageCode.isEmpty()) {
                toast("imageCode is empty")
                return
            }
            if (phone.isEmpty()) {
                toast("phone is empty")
                return
            }
            checkVerifyImage(imageCode)
        }
    }

    private fun getSMSCode(captchaToken: String) {
        val phone = mBinding.emailPhone.text.toString()
        KunLuHomeSdk.userImpl.getVerifyCode(phone, KunLuUserType.SEND_CODE_REGISTER, captchaToken, getSMSCodeCallback)
        countDownTimer.start()
    }

    private val getSMSCodeCallback = object : IResultCallback {
        override fun onSuccess() {
            toast("send SMS success")
        }

        override fun onError(code: String, error: String) {
            toast("code: $code error: $error")
        }
    }

    private fun checkVerifyImage(imageCode: String) {
        KunLuHomeSdk.userImpl.checkVerifyImageCode(mRid, imageCode, checkVerifyImageCallback)
    }

    private val checkVerifyImageCallback = object : ICheckVerifyImageCallback {
        override fun onSuccess(bean: CheckVerifyImageBean) {
            getSMSCode(bean.captchaToken)
        }

        override fun onError(code: String, error: String) {
            toast("code: $code error: $error")
        }
    }

    private fun getVerifyImage() {
        KunLuHomeSdk.userImpl.getVerifyImageCode(verifyImageCallback)
    }

    private val verifyImageCallback = object : IVerifyImageCallback {
        override fun onSuccess(bean: VerifyImageBean) {
            if (bean.png.isNotEmpty()) {
                mBinding.verifyImageLayout.visibility = View.VISIBLE
                mBinding.verifyImage.setImageBitmap(base64CodeToBitmap(bean.png))
                mRid = bean.rid
            } else {
                toast("please once click")
            }
        }

        override fun onError(code: String, error: String) {
            toast("code: $code error: $error")
        }
    }

    /**
     * 将Base64字符串形式存储的图片转成Bitmap
     */
    fun base64CodeToBitmap(base64Code: String): Bitmap? {
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