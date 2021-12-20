package com.example.kiotsdk.ui.msg

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMsgDetailsBinding

/**
 * User: Chris
 * Date: 2021/12/7
 * Desc:
 */

class MsgDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMsgDetailsBinding

    private var mContent = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMsgDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mContent = it.getStringExtra(CONTENT) ?: "" }
        mBinding.content.text = mContent
    }

    companion object {
        const val CONTENT = "content"
    }
}