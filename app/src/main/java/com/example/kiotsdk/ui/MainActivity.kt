package com.example.kiotsdk.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMainBinding
import com.example.kiotsdk.ui.family.FamilyCreateActivity
import com.example.kiotsdk.ui.family.FamilySelectActivity
import com.example.kiotsdk.ui.user.UserInfoActivity
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.helper.UserApi
import com.kunluiot.sdk.util.SPUtil
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private var mCurrentFamilyName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.userInfo.setOnClickListener { startActivity<UserInfoActivity>() }
        mBinding.logout.setOnClickListener { logout() }

        mBinding.homeCreate.setOnClickListener { startActivity<FamilyCreateActivity>() }
        mBinding.homeSelect.setOnClickListener { selectFamily.launch(Intent(this, FamilySelectActivity::class.java)) }

    }

    private val selectFamily = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            mCurrentFamilyName = it.data?.getStringExtra(FamilySelectActivity.CURRENT_FAMILY).toString()
            mBinding.homeManagerValue.text = "当前家庭：$mCurrentFamilyName"
        }
    }

    private fun logout() {
        // logout
        toast("logout")
        SPUtil.apply(KunLuHomeSdk.instance.getApp(), UserApi.KHA_API_LOGIN, "")
        KunLuHomeSdk.instance.webSocketDisConnect()
        startActivity<SplashActivity>()
        finish()
    }

    private var cacheMills: Long = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - cacheMills > 1000L) {
            cacheMills = System.currentTimeMillis()
            toast("连按两次退出app")
        } else {
            finish()
        }
    }
}