package com.example.kiotsdk.ui.msg

import android.os.Bundle
import android.view.View
import com.example.kiotsdk.adapter.msg.MsgListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMsgListBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.CommonMsgContentBean

/**
 * User: Chris
 * Date: 2021/12/7
 * Desc:
 */

class MsgListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMsgListBinding

    private lateinit var mAdapter: MsgListAdapter

    private var mType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMsgListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mType = it.getStringExtra(TYPE) ?: "" }
        if (mType == TYPE_PLATFORM) mBinding.all.visibility = View.GONE

        mBinding.all.setOnClickListener { allReadMsg() }
        mBinding.clean.setOnClickListener { cleanMsg() }

        initAdapter()
        getMsgData()
    }

    private fun cleanMsg() {
        if (mType == TYPE_DEVICE) {
            KunLuHomeSdk.commonImpl.emptyMessageDevice({ c, m -> toastErrorMsg(c, m) }, { getMsgData() })
        } else {
            KunLuHomeSdk.commonImpl.emptyMessagePlatform({ c, m -> toastErrorMsg(c, m) }, { getMsgData() })
        }
    }

    private fun allReadMsg() {
        if (mType == TYPE_DEVICE) {
            KunLuHomeSdk.commonImpl.allReadMessageDevice({ c, m -> toastErrorMsg(c, m) }, { getMsgData() })
        }
    }

    private fun getMsgData() {
        if (mType == TYPE_PLATFORM) {
            KunLuHomeSdk.commonImpl.getMessagePlatform(0, 20, { c, m -> toastErrorMsg(c, m) }, {
                mAdapter.data.clear()
                mAdapter.addData(it.content)
            })
        } else {
            KunLuHomeSdk.commonImpl.getMessageDevice(0, 20, { c, m -> toastErrorMsg(c, m) }, {
                mAdapter.data.clear()
                mAdapter.addData(it.content)
            })
        }
    }

    private fun initAdapter() {
        mAdapter = MsgListAdapter(arrayListOf())
        mAdapter.setType(mType)
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as CommonMsgContentBean
            if (!bean.isRead) readMsg(bean)
        }
    }

    private fun readMsg(bean: CommonMsgContentBean) {
        if (mType == TYPE_PLATFORM) {
            KunLuHomeSdk.commonImpl.readMessagePlatform(bean.id, { c, m -> toastErrorMsg(c, m) }, { getMsgData() })
        } else {
            KunLuHomeSdk.commonImpl.readMessageDevice(bean.id, { c, m -> toastErrorMsg(c, m) }, { getMsgData() })
        }
    }

    companion object {
        const val TYPE = "type"
        const val TYPE_PLATFORM = "type_platform"
        const val TYPE_DEVICE = "type_device"
    }
}