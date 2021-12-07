package com.example.kiotsdk.ui.user

import android.content.Intent
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMineManagerBinding
import com.example.kiotsdk.ui.SplashActivity
import com.example.kiotsdk.ui.device.DeviceManagerActivity
import com.example.kiotsdk.ui.family.FamilyListActivity
import com.example.kiotsdk.ui.feedback.FeedbackActivity
import com.example.kiotsdk.ui.msg.MsgActivity
import com.kunluiot.sdk.KunLuHomeSdk
import org.jetbrains.anko.startActivity


/**
 * User: Chris
 * Date: 2021/12/4
 * Desc:
 */

class MineManagerActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMineManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMineManagerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.btnMyInfo.setOnClickListener { startActivity<UserInfoActivity>() }
        mBinding.btnDeviceManager.setOnClickListener { startActivity<DeviceManagerActivity>() }
        mBinding.btnFamilyManager.setOnClickListener { startActivity<FamilyListActivity>() }
        mBinding.btnMsgCenter.setOnClickListener { startActivity<MsgActivity>() }
        mBinding.btnFeedback.setOnClickListener { startActivity<FeedbackActivity>() }
        mBinding.btnLogout.setOnClickListener {
            KunLuHomeSdk.instance.logout()
            val intent = Intent()
            intent.setClass(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(SplashActivity.GO_FINISH, true)
            startActivity(intent)
        }
    }
}