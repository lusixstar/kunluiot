package com.example.kiotsdk.ui.family

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.family.FamilyRoomListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyRoomBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.family.FolderBean
import org.jetbrains.anko.alert
import org.jetbrains.anko.selector

class FamilyRoomListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyRoomBinding

    private lateinit var mAdapter: FamilyRoomListAdapter

    private var mFamilyId = ""

    private var mRoomIds = mutableListOf<String>()
    private var mRoomNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyRoomBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mFamilyId = it.getStringExtra(FAMILY_ID) ?: "" }

        mBinding.add.setOnClickListener { gotoAdd.launch(Intent(this, RoomCreateActivity::class.java).putExtra(FAMILY_ID, mFamilyId)) }

        initAdapter()
        getRoomData()
    }

    private val gotoEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getRoomData()
        }
    }

    private fun initAdapter() {
        mAdapter = FamilyRoomListAdapter(arrayListOf()) { device -> moveDevice(device) }
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.img)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as FolderBean
            if (view.id == R.id.img) {
                gotoEdit.launch(Intent(this, RoomCreateActivity::class.java).putExtra(RoomCreateActivity.ROOM_NAME, bean.folderName).putExtra(RoomCreateActivity.ROOM_ID, bean.folderId))
            }
        }
        mAdapter.setOnItemLongClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as FolderBean
            deleteRoom(bean)
            false
        }
    }

    private fun moveDevice(device: DeviceNewBean) {
        var devTid = ""
        var ctrlKey = ""
        var subDevTid = ""
        if ("SUB" == device.devType) {
            devTid = device.parentDevTid
            ctrlKey = device.parentCtrlKey
            subDevTid = device.devTid
        } else {
            devTid = device.devTid
            ctrlKey = device.ctrlKey
            subDevTid = ""
        }
        selector("选择房间", mRoomNames) { dialog, i ->
            val roomId = mRoomIds[i]
            KunLuHomeSdk.familyImpl.moveRoomDevice(roomId, devTid, ctrlKey, subDevTid, { c, m -> toastErrorMsg(c, m) }, {
                getRoomData()
            })
            dialog.dismiss()
        }
    }

    private fun deleteRoom(bean: FolderBean) {
        alert("是否删除") {
            positiveButton("确定") { dialog ->
                KunLuHomeSdk.familyImpl.deleteRoom(bean.folderId, { c, m -> toastErrorMsg(c, m) }, {
                    getRoomData()
                })
                dialog.dismiss()
            }
        }.show()
    }

    private fun getRoomData() {
        KunLuHomeSdk.familyImpl.getRoomsDevice(mFamilyId, false, 0, 999, { c, m -> toastErrorMsg(c, m) }, { list ->
            mAdapter.data.clear()
            mAdapter.addData(list)
            mRoomIds.clear()
            mRoomNames.clear()
            list.forEach {
                mRoomIds.add(it.folderId)
                mRoomNames.add(it.folderName)
            }
        })
    }

    private val gotoAdd = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getRoomData()
        }
    }

    companion object {
        const val FAMILY_ID = "family_id"
    }
}