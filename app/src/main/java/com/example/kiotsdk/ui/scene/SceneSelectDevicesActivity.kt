package com.example.kiotsdk.ui.scene

import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.diff.DiffRoomListCallback
import com.example.kiotsdk.adapter.scene.SceneSelectDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneSelectDevicesBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import org.jetbrains.anko.selector


class SceneSelectDevicesActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneSelectDevicesBinding

    private lateinit var mRoomAdapter: SceneSelectDeviceListAdapter

    private var mFamilyList = mutableListOf<FamilyBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneSelectDevicesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }


        mBinding.title.setOnClickListener { selectFamily() }

        initAdapter()
        getFamilyData()
    }

    private fun initAdapter() {
        mRoomAdapter = SceneSelectDeviceListAdapter(mutableListOf()) {
            XLog.e("it == $it")
        }
        mRoomAdapter.setDiffCallback(DiffRoomListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mRoomAdapter
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