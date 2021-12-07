package com.example.kiotsdk.ui.device

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceConfigFinishBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.callback.IResultCallback
import org.jetbrains.anko.toast


class DeviceConfigFinishActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceConfigFinishBinding

    private var mDevTid = ""
    private var mSubDevTid = ""
    private var mMid = ""
    private var mRegisterId = ""
    private var mDeviceName = ""
    private var mCtrlKey = ""
    private var mBean = DeviceNewBean()
    private var mBranchNames = ""

    private var mFamilyId = ""
    private var mRoomId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceConfigFinishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mDevTid = it.getStringExtra(DEV_TID) ?: ""
            mSubDevTid = it.getStringExtra(SUB_DEV_TID) ?: ""
            mMid = it.getStringExtra(MID) ?: ""
            mRegisterId = it.getStringExtra(REGISTER_ID) ?: ""
            mDeviceName = it.getStringExtra(DEVICE_NAME) ?: ""
            mCtrlKey = it.getStringExtra(CTRL_KEY) ?: ""
            mBranchNames = it.getStringExtra(BRANCH_NAMES) ?: ""
            mBean = it.getParcelableExtra(DEVICE) ?: DeviceNewBean()
        }

        mBinding.finish.setOnClickListener { gotoNext() }


        getFamilyData()

    }

    private fun gotoNext() {
        val family = mBinding.family.text.toString()
        val room = mBinding.room.text.toString()
        if (family.isEmpty() || room.isEmpty()) {
            toast("family or room is empty")
            return
        }
        KunLuHomeSdk.deviceImpl.deviceConfigFinish(mDevTid, mCtrlKey, mDeviceName, mFamilyId, mRoomId, listOf(), listOf(), object : IResultCallback {
            override fun onError(code: String, error: String) {
                toast("code == $code, error == $error")
            }

            override fun onSuccess() {
                toast("finish")
            }
        })
    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getFamilyList({ code, msg -> toastErrorMsg(code, msg) }, { bean ->
            if (!bean.isNullOrEmpty()) {
                val info = bean.first()
                mBinding.family.text = "当前家庭: ${info.familyName}"
                mFamilyId = info.familyId
                getRoomData(info.familyId)
            }
        })
    }

    private fun getRoomData(familyId: String) {
        KunLuHomeSdk.familyImpl.getRooms(familyId, 0, 20, { c, m -> toastErrorMsg(c, m) }, { bean ->
            val info = bean.first()
            mRoomId = info.folderId
            mBinding.room.text = if (info.folderName == "root") "默认房间" else "当前房间: ${info.folderName}"
        }
//            object : IFamilyRoomListCallback {
//            override fun onSuccess(bean: List<FolderBean>) {
//                val info = bean.first()
//                mRoomId = info.folderId
//                mBinding.room.text =  if (info.folderName == "root") "默认房间" else "当前房间: ${info.folderName}"
//            }
//
//            override fun onError(code: String, error: String) {
//                toast("code == $code, error == $error")
//            }
//        }
        )
    }

    companion object {
        const val DEV_TID = "devTid"
        const val BRANCH_NAMES = "branchNames"
        const val DEVICE = "device"
        const val MID = "mid"
        const val REGISTER_ID = "registerId"
        const val DEVICE_NAME = "deviceName"
        const val CTRL_KEY = "ctrlKey"
        const val SUB_DEV_TID = "subDevTid"
    }
}