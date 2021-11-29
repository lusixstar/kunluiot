package com.example.kiotsdk.ui.scene

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneOneKeyAddDevicesBinding

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneOneKeyAddDevicesActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneOneKeyAddDevicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneOneKeyAddDevicesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }
    }
}