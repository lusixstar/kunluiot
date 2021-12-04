package com.example.kiotsdk.ui.user

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityBindSocialAccountBinding
import com.kunluiot.sdk.KunLuHomeSdk

/**
 * User: Chris
 * Date: 2021/12/4
 * Desc:
 */

class BindSocialAccountActivity : BaseActivity() {

    private lateinit var mBinding: ActivityBindSocialAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityBindSocialAccountBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.wechatLayout.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        toastMsg("接入微信sdk获取到微信信息后绑定")
    }
}