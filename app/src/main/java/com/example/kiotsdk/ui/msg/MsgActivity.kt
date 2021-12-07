package com.example.kiotsdk.ui.msg

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMsgCenterBinding
import org.jetbrains.anko.startActivity

/**
 * User: Chris
 * Date: 2021/12/7
 * Desc:
 */

class MsgActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMsgCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMsgCenterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.itemPlatformMessage.setOnClickListener { startActivity<MsgListActivity>(MsgListActivity.TYPE to MsgListActivity.TYPE_PLATFORM) }
        mBinding.itemDeviceMessage.setOnClickListener { startActivity<MsgListActivity>(MsgListActivity.TYPE to MsgListActivity.TYPE_DEVICE) }
    }
}