package com.example.kiotsdk.ui.device

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceConfigFinishBinding
import com.example.kiotsdk.ui.MainNewActivity
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast


class DeviceConfigFinishActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceConfigFinishBinding

    private var mDevTid = ""
    private var mSubDevTid = ""
    private var mMid = ""
    private var mRegisterId = ""
    private var mDeviceName = ""
    private var mCtrlKey = ""

    //    private var mBean = DeviceNewBean()
    private var mGatewayBean: DeviceNewBean = DeviceNewBean()
    private var mBranchNames = ""

    private var mFamilyId = ""
    private var mRoomId = ""

    private var mFamilyList = mutableListOf<FamilyBean>()
    private var mFolderList = mutableListOf<FolderBean>()

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
            mGatewayBean = it.getParcelableExtra(GATEWAY_BEAN) ?: DeviceNewBean()
            if (mDeviceName.isNotEmpty()) {
                mBinding.deviceValue.setText(mDeviceName)
            }
        }

        mBinding.finish.setOnClickListener { gotoNext() }
        mBinding.deviceValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mDeviceName = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        mBinding.familyLayout.setOnClickListener { selectFamily() }
        mBinding.roomLayout.setOnClickListener { selectRoom() }
        getFamilyData()

    }

    private fun selectRoom() {
        if (!mFolderList.isNullOrEmpty()) {
            val list = if (mSubDevTid.isNotEmpty()) {
                mFolderList.filter { mGatewayBean.folderName == it.folderName }.map { it.folderName }
            } else {
                mFolderList.map { it.folderName }
            }
            selector("选择房间", list) { dialog, i ->
                val name = list[i]
                val info = mFolderList.first { it.folderName == name }
                mBinding.roomValue.text = if (info.folderName == "root") "默认房间" else "当前房间: ${info.folderName}"
                mRoomId = info.folderId
                dialog.dismiss()
            }
        }
    }

    private fun selectFamily() {
        if (!mFamilyList.isNullOrEmpty()) {
            val list = if (mSubDevTid.isNotEmpty()) {
                mFamilyList.filter { mGatewayBean.familyName == it.familyName }.map { it.familyName }
            } else {
                mFamilyList.map { it.familyName }
            }
            selector("选择家庭", list) { dialog, i ->
                val name = list[i]
                val info = mFamilyList.first { it.familyName == name }
                mBinding.familyValue.text = "${info.familyName}"
                mFamilyId = info.familyId
                getRoomData(info.familyId)
                dialog.dismiss()
            }
        }
    }

    private fun gotoNext() {
        if (mDeviceName.isEmpty()) {
            toast("device name is empty")
            return
        }
        val family = mBinding.familyValue.text.toString()
        val room = mBinding.roomValue.text.toString()
        if (family.isEmpty() || room.isEmpty()) {
            toast("family or room is empty")
            return
        }
        if (mSubDevTid.isNotEmpty()) {
            KunLuHomeSdk.deviceImpl.subDeviceConfigFinish(mDevTid, mSubDevTid, mCtrlKey, mDeviceName, mFamilyId, mRoomId, { c, m -> toastErrorMsg(c, m) }, {
                val intent = Intent()
                intent.setClass(this, MainNewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                toastMsg("set success")
            })
        } else {
            KunLuHomeSdk.deviceImpl.deviceConfigFinish(mDevTid, mCtrlKey, mDeviceName, mFamilyId, mRoomId, { c, m -> toastErrorMsg(c, m) }, {
                val intent = Intent()
                intent.setClass(this, MainNewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                toastMsg("set success")
            })
        }

    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getFamilyList({ code, msg -> toastErrorMsg(code, msg) }, { bean ->
            if (!bean.isNullOrEmpty()) {
                mFamilyList.addAll(bean)
                val info = if (mSubDevTid.isNotEmpty()) {
                    bean.first { it.familyName == mGatewayBean.familyName }
                } else {
                    bean.first()
                }
                mBinding.familyValue.text = "当前家庭: ${info.familyName}"
                mFamilyId = info.familyId
                getRoomData(info.familyId)
            }
        })
    }

    private fun getRoomData(familyId: String) {
        KunLuHomeSdk.familyImpl.getRooms(familyId, 0, 20, { c, m -> toastErrorMsg(c, m) }, { bean ->
            mFolderList.clear()
            mFolderList.addAll(bean)
            val info = bean.first { it.folderName == "root" }
            mRoomId = info.folderId
            mBinding.roomValue.text = if (info.folderName == "root") "默认房间" else "当前房间: ${info.folderName}"
        })
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
        const val GATEWAY_BEAN = "gateway_bean"
    }
}