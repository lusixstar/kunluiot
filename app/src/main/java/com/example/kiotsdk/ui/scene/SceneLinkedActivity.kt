package com.example.kiotsdk.ui.scene

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneLinkedBinding

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneLinkedActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneLinkedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneLinkedBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }
    }
}