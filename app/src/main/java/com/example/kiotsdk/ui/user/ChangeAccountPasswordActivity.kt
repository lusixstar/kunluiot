package com.example.kiotsdk.ui.user

import android.content.Intent
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityChangeAccountPasswordBinding
import com.example.kiotsdk.ui.SplashActivity
import com.kunluiot.sdk.KunLuHomeSdk

/**
 * User: Chris
 * Date: 2021/12/4
 * Desc:
 */

class ChangeAccountPasswordActivity : BaseActivity() {

    private lateinit var mBinding: ActivityChangeAccountPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityChangeAccountPasswordBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.finish.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        val old = mBinding.tvOld.text.toString().trim()
        val new = mBinding.tvNew.text.toString().trim()
        val two = mBinding.tvNewTwo.text.toString().trim()
        if (old.isEmpty() || new.isEmpty() || two.isEmpty()) {
            toastMsg("password is empty")
            return
        }
        if (new != two) {
            toastMsg("两次输入新密码不一致")
            return
        }
        KunLuHomeSdk.userImpl.changePassword(old, new, { code, msg -> toastErrorMsg(code, msg) }, {
            toastMsg("change success")
            KunLuHomeSdk.instance.logout()
            val intent = Intent()
            intent.setClass(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(SplashActivity.GO_FINISH, true)
            startActivity(intent)
        })
    }
}