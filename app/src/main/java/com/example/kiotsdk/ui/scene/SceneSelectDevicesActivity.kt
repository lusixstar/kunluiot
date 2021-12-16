package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.adapter.diff.DiffRoomListCallback
import com.example.kiotsdk.adapter.scene.SceneSelectDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneSelectConditionDevicesBinding
import com.example.kiotsdk.databinding.ActivitySceneSelectDevicesBinding
import com.example.kiotsdk.ui.operation.OperationListActivity
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.bean.scene.SceneIftttTasksListBeanNew
import com.kunluiot.sdk.bean.scene.SceneIftttTasksParamBeanNew
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import org.jetbrains.anko.selector


class SceneSelectDevicesActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneSelectConditionDevicesBinding

    private lateinit var mRoomAdapter: SceneSelectDeviceListAdapter

    private var mFamilyList = mutableListOf<FamilyBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneSelectConditionDevicesBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.title.setOnClickListener { selectFamily() }

        initAdapter()
        getFamilyData()
    }

    private val gotoAddDevice = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val device = it.data?.getStringExtra(OperationListActivity.DEVICE) ?: ""
            val deviceBean = it.data?.getParcelableExtra(OperationListActivity.DEVICE_BEAN) ?: SceneIftttTasksListBeanNew()
            if (device == OperationListActivity.DEVICE) {
                setResult(Activity.RESULT_OK, intent.putExtra(OperationListActivity.DEVICE, OperationListActivity.DEVICE).putExtra(OperationListActivity.DEVICE_BEAN, deviceBean))
                finish()
            }
        }
    }

    private fun initAdapter() {
        mRoomAdapter = SceneSelectDeviceListAdapter(mutableListOf()) { protocol, bean ->
            val list = arrayListOf<DeviceOperationProtocolBean>()
            val fList = arrayListOf<DeviceOperationProtocolBean>()
            protocol.forEach { (_, u) -> run { if (!u.fields.isNullOrEmpty()) list.add(u) } }
            val l = list.filter { it.usedForIFTTT && it.frameType == 2 }
            fList.addAll(l)
            gotoAddDevice.launch(Intent(this, OperationListActivity::class.java).putExtra(OperationListActivity.LIST_BEAN, fList).putExtra(OperationListActivity.DEVICE_BEAN, bean))
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