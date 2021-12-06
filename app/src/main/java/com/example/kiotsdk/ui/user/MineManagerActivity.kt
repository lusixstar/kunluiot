package com.example.kiotsdk.ui.user

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMineManagerBinding
import com.example.kiotsdk.ui.device.DeviceManagerActivity
import com.example.kiotsdk.ui.family.FamilyListActivity
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
    }
}