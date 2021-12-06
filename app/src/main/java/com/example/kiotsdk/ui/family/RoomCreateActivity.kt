package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityRoomCreateBinding
import com.kunluiot.sdk.KunLuHomeSdk
import org.jetbrains.anko.toast

class RoomCreateActivity : BaseActivity() {

    private lateinit var mBinding: ActivityRoomCreateBinding

    private var mFamilyId = ""
    private var mRoomName = ""
    private var mRoomId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityRoomCreateBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mFamilyId = it.getStringExtra(FamilyRoomListActivity.FAMILY_ID) ?: ""
            mRoomName = it.getStringExtra(ROOM_NAME) ?: ""
            mRoomId = it.getStringExtra(ROOM_ID) ?: ""
            if (mRoomName.isNotEmpty()) {
                mBinding.toolBar.title = "编辑房间名称"
                mBinding.name.setText(mRoomName)
            }
        }
        mBinding.create.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        val name = mBinding.name.text.toString()
        if (name.isEmpty()) {
            toast("name is empty")
            return
        }
        if (mRoomName.isNotEmpty()) {
            KunLuHomeSdk.familyImpl.updateRoomInfo(mRoomId, name, { code, msg -> toastErrorMsg(code, msg) }, {
                setResult(Activity.RESULT_OK)
                finish()
            })
        } else {
            KunLuHomeSdk.familyImpl.addRooms(mFamilyId, name, { code, msg -> toastErrorMsg(code, msg) }, {
                setResult(Activity.RESULT_OK)
                finish()
            })
        }
    }

    companion object {
        const val ROOM_NAME = "room_name"
        const val ROOM_ID = "room_id"
    }
}