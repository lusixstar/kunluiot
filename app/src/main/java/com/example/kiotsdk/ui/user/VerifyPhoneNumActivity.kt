package com.example.kiotsdk.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityUserVerifyPhoneNumBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.user.KunLuUserType

/**
 * User: Chris
 * Date: 2021/12/4
 * Desc:
 */

class VerifyPhoneNumActivity : BaseActivity() {

    private lateinit var mBinding: ActivityUserVerifyPhoneNumBinding

    private var mPhone = ""
    private var mNewPhone = ""
    private var mToken = ""

    private var isNewPhone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUserVerifyPhoneNumBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mPhone = it.getStringExtra(UserEditPhoneActivity.PHONE) ?: "" }
        mBinding.hint.text = "我们已发送一条短信验证码至 $mPhone"

        mBinding.btnFinish.setOnClickListener { gotoNext() }
        mBinding.code.setOnClickListener { sendSms() }

        sendSms()
    }

    private fun sendSms() {
        KunLuHomeSdk.userImpl.getVerifyCode(mPhone, KunLuUserType.SEND_CODE_CHANGE_PHONE, "", { code, msg -> toastErrorMsg(code, msg) }, { countDownTimer.start() })
    }

    private fun gotoNext() {
        val code = mBinding.etSms.text.toString().trim()
        if (code.isEmpty()) {
            toastMsg("code is empty")
            return
        }
        if (!isNewPhone) {
            KunLuHomeSdk.userImpl.checkVerifyCode(mPhone, KunLuUserType.SEND_CODE_CHANGE_PHONE, "", code, { c, msg -> toastErrorMsg(c, msg) }, {
                mToken = it.token
                gotoNew.launch(Intent(this, BindNewPhoneNumActivity::class.java))
            })
        } else {
            KunLuHomeSdk.userImpl.changePhoneNum(code, mToken, mNewPhone, { c, msg -> toastErrorMsg(c, msg) }, {
                toastMsg("修改成功， 请关闭APP重新登录")
            })
        }
    }

    private val gotoNew = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val phone = it.data?.getStringExtra(BindNewPhoneNumActivity.NEW_PHONE) ?: ""
            if (phone.isNotEmpty()) {
                mNewPhone = phone
                sendNewPhoneSms(phone)
            }
        }
    }

    private fun sendNewPhoneSms(phone: String) {
        KunLuHomeSdk.userImpl.getVerifyCode(phone, KunLuUserType.SEND_CODE_REGISTER, "", { code, msg -> toastErrorMsg(code, msg) }, {
            mBinding.hint.text = "我们已发送一条短信验证码至 $phone"
            mBinding.etSms.setText("")
            isNewPhone = true
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    private var smsTime: Long = 60 * 1000
    private val countDownTimer = object : CountDownTimer(smsTime, 1000) {

        override fun onFinish() {
            smsTime = 60 * 1000
            mBinding.code.text = resources.getString(R.string.send_verify_code)
            mBinding.code.isEnabled = true
        }

        override fun onTick(millisUntilFinished: Long) {
            mBinding.code.text = "${millisUntilFinished / 1000}S"
            mBinding.code.isEnabled = false
        }
    }

    companion object {
        const val PHONE = "phone"
    }
}