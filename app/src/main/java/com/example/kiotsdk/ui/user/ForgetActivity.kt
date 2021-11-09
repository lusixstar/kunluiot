package com.example.kiotsdk.ui.user

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityForgetBinding
import com.example.kiotsdk.databinding.ActivityRegisterBinding

class ForgetActivity : BaseActivity() {

    private lateinit var mBinding: ActivityForgetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.send.setOnClickListener { }
        mBinding.reset.setOnClickListener { }
    }
}