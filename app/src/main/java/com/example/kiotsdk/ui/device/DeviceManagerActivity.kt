package com.example.kiotsdk.ui.device

import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.adapter.device.DeviceRoomListAdapter
import com.example.kiotsdk.adapter.diff.DiffRoomListCallback
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceRoomListBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.api.device.KunLuDeviceType
import com.kunluiot.sdk.bean.device.DeviceNewBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.thirdlib.ws.websocket.util.LogUtil
import com.kunluiot.sdk.ui.web.DeviceWebControlActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity


class DeviceManagerActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceRoomListBinding

    private lateinit var mRoomAdapter: DeviceRoomListAdapter

    private var mFamilyList = mutableListOf<FamilyBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceRoomListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }


        mBinding.title.setOnClickListener { selectFamily() }

        initAdapter()
        getFamilyData()
    }

    private fun initAdapter() {
        mRoomAdapter = DeviceRoomListAdapter(mutableListOf()) { gotoNext(it) }
        mRoomAdapter.setDiffCallback(DiffRoomListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mRoomAdapter
    }

    private fun gotoNext(bean: DeviceNewBean) {
        if (bean.androidPageZipURL.isNotEmpty()) {
            startActivity<DeviceWebControlActivity>(DeviceWebControlActivity.BEAN to bean)
        } else {
            toastMsg("androidPageZipURL is empty")
        }
    }

    private fun gotoDelete(it: DeviceNewBean) {
        val userId = KunLuHomeSdk.instance.getSessionBean()?.user ?: ""
        alert("是否删除设备") {
            positiveButton("确定") { dialog ->
                if (it.devType == KunLuDeviceType.DEVICE_SUB) {
                    KunLuHomeSdk.deviceImpl.deletesSubDevice(it.parentDevTid, it.parentCtrlKey, it.devTid, { code, msg -> toastErrorMsg(code, msg) }, { getFamilyData() })
                } else {
                    if (userId == it.ownerUid) {
                        KunLuHomeSdk.deviceImpl.deleteDevice(it.devTid, it.bindKey, "", false, { code, msg -> toastErrorMsg(code, msg) }, { getFamilyData() })
                    } else {
                        //删除授权设备
//            KunLuHomeSdk.deviceImpl.deleteDevice(it.devTid, it.bindKey, "", false, { code, msg -> toastErrorMsg(code, msg) }, {
//                LogUtil.e("delete", "$it")
//            })
                    }
                }
                dialog.dismiss()
            }
            negativeButton("取消") { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun selectFamily() {
        if (mFamilyList.isNotEmpty()) {
            val listName = mFamilyList.map { it.familyName }
            selector(title = "选择家庭", items = listName) { dialog, index ->
                mBinding.title.text = "当前选择家庭是:${mFamilyList[index].familyName}"
                getRoomsDevice(mFamilyList[index].familyId)
                dialog.dismiss()
            }
        } else {
            getFamilyData()
        }
    }

    private fun getFamilyData() {
        KunLuHomeSdk.familyImpl.getFamilyList({ code, msg -> toastErrorMsg(code, msg) }, { setFamilyData(it) })
    }

    private fun setFamilyData(list: List<FamilyBean>) {
        if (!list.isNullOrEmpty()) {
            mFamilyList.clear()
            mFamilyList.addAll(list)
            mBinding.title.text = "当前选择家庭是:${mFamilyList.first().familyName}"
            getRoomsDevice(mFamilyList.first().familyId)
        }
    }

    private fun getRoomsDevice(familyId: String) {
        KunLuHomeSdk.familyImpl.getRoomsDevice(familyId, false, 0, 20, { code, msg -> toastErrorMsg(code, msg) }, { setRoomData(it) })
    }

    private fun setRoomData(it: List<FolderBean>) {
        if (!it.isNullOrEmpty()) {
            mRoomAdapter.setDiffNewData(it as MutableList<FolderBean>)
        }
    }
}