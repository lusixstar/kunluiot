package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.device.DeviceRoomItemAdapter
import com.example.kiotsdk.adapter.family.FamilyMemberDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyMemberEditBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.family.FamilyMemberMapBean
import com.kunluiot.sdk.bean.family.FolderBean
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast

class MemberEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyMemberEditBinding

    private lateinit var mAdapter: FamilyMemberDeviceListAdapter

    private var mBean = FamilyMemberMapBean()

    private var mGender = ""
    private var mName = ""
    private var mFamilyId = ""
    private var mType = ""

    private var mFolderList = arrayListOf<FolderBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyMemberEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mBean = it.getParcelableExtra(MEMBER_DEVICE_BEAN) ?: FamilyMemberMapBean()
            mType = it.getStringExtra(MEMBER_TYPE) ?: ""
            mFamilyId = it.getStringExtra(MEMBER_FAMILY_ID) ?: ""
            mFolderList = it.getParcelableArrayListExtra(MEMBER_FOLDER) ?: arrayListOf()
            setData()
        }
        mBinding.roomLayout.setOnClickListener { selectRoom() }
        mBinding.sexLayout.setOnClickListener { selectGender() }
        mBinding.create.setOnClickListener { gotoNext() }
        mBinding.authorize.setOnClickListener { gotoAuthorize() }

        initAdapter()
        if (!mFolderList.isNullOrEmpty()) {
            mBinding.room.text = mFolderList.first().folderName
            KunLuHomeSdk.deviceImpl.getRoomsDevices(mFolderList.first().folderId, true, { c, m -> toastErrorMsg(c, m) }, { list ->
                val l = list.filter { it.devType != "SUB" }.filter { it.bindKey.isNotEmpty() }.filter { it.ctrlKey.isNotEmpty() }
                l.forEach { if (mBean.ctrlKeys.contains(it.ctrlKey)) it.select = true }
                mAdapter.data.clear()
                mAdapter.addData(l)
            })
        }
    }

    private fun gotoAuthorize() {
        if (!mAdapter.data.isNullOrEmpty()) {
            val list = mAdapter.data.map { it.ctrlKey }
            KunLuHomeSdk.familyImpl.updateMemberCtrlKeys(mFamilyId, mBean.uid, list, { c, m -> toastErrorMsg(c, m) }, { toastMsg("change success") })
        }
    }

    private fun initAdapter() {
        mAdapter = FamilyMemberDeviceListAdapter(arrayListOf())
        (mBinding.deviceList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.deviceList.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as DeviceNewBean
            bean.select = !bean.select
            adapter.notifyItemChanged(position)
        }
    }

    private fun selectRoom() {
        val list = mFolderList.map { it.folderName }
        selector("选择房间", list) { dialog, i ->
            val bean = mFolderList[i]
            mBinding.room.text = list[i]
            KunLuHomeSdk.deviceImpl.getRoomsDevices(bean.folderId, true, { c, m -> toastErrorMsg(c, m) }, { list ->
                val l = list.filter { it.devType != "SUB" }.filter { it.bindKey.isNotEmpty() }.filter { it.ctrlKey.isNotEmpty() }
                l.forEach { if (mBean.ctrlKeys.contains(it.ctrlKey)) it.select = true }
                mAdapter.data.clear()
                mAdapter.addData(l)
            })
            dialog.dismiss()
        }
    }

    private fun setData() {
        mGender = mBean.gender
        mName = mBean.name
        mBinding.phone.text = mBean.phoneNumber
        mBinding.name.setText(mBean.name)
        val gender = when (mBean.gender) {
            "man" -> "男"
            "woman" -> "女"
            else -> "小孩"
        }
        mBinding.gender.text = gender
    }

    private fun selectGender() {
        val list = listOf("男", "女", "小孩")
        selector("选择", list) { dialog, i ->
            when (i) {
                0 -> mGender = "man"
                1 -> mGender = "woman"
                2 -> mGender = "children"
            }
            mBinding.gender.text = list[i]
            dialog.dismiss()
        }
    }

    private fun gotoNext() {
        val name = mBinding.name.text.toString()
        if (name.isEmpty()) {
            toast("name is empty")
            return
        }
        if (mType.isEmpty()) {
            toast("gender is empty")
            return
        }
        KunLuHomeSdk.familyImpl.updateFamilyMemberInfo(mFamilyId, name, mGender, mBean.uid, { code, msg -> toastErrorMsg(code, msg) }, {
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

    companion object {
        const val MEMBER_DEVICE_BEAN = "member_device_bean"
        const val MEMBER_TYPE = "member_type"
        const val MEMBER_FAMILY_ID = "member_family_id"
        const val MEMBER_FOLDER = "member_folder"
    }
}