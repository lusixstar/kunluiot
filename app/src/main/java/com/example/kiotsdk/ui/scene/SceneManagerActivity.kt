package com.example.kiotsdk.ui.scene

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneManagerBinding
import org.jetbrains.anko.startActivity

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneManagerActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneManagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneManagerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.oneKey.setOnClickListener { startActivity<SceneOneKeyActivity>() }
        mBinding.linked.setOnClickListener { startActivity<SceneLinkedActivity>() }
    }
}