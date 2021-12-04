package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.diff.DiffRoomListCallback
import com.example.kiotsdk.adapter.scene.SceneSelectDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneSelectDevicesBinding
import com.example.kiotsdk.ui.operation.OperationLinkedListActivity
import com.example.kiotsdk.ui.operation.OperationListActivity
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceOperationProtocolBean
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FolderBean
import com.kunluiot.sdk.bean.scene.SceneConditionListParam
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import org.jetbrains.anko.selector


class SceneSelectConditionDevicesActivity : BaseActivity() {

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

    private val gotoAddDevice = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val device = it.data?.getStringExtra(OperationLinkedListActivity.DEVICE) ?: ""
            if (device == OperationLinkedListActivity.DEVICE) {
                val deviceBean = it.data?.getParcelableExtra(OperationLinkedListActivity.DEVICE_RESULT_BEAN) ?: SceneConditionListParam()
                setResult(Activity.RESULT_OK, intent.putExtra(SceneAddConditionActivity.DEVICE, SceneAddConditionActivity.DEVICE).putExtra(SceneAddConditionActivity.DEVICE_BEAN, deviceBean))
                finish()
            }
        }
    }

    private fun initAdapter() {
        mRoomAdapter = SceneSelectDeviceListAdapter(mutableListOf()) { protocol, bean ->
            val list = arrayListOf<DeviceOperationProtocolBean>()
            val fList = arrayListOf<DeviceOperationProtocolBean>()
            protocol.forEach { (_, u) -> run { if (!u.fields.isNullOrEmpty()) list.add(u) } }
            val l = list.filter { it.usedForIFTTT && it.frameType == 1 }
            fList.addAll(l)
            gotoAddDevice.launch(Intent(this, OperationLinkedListActivity::class.java).putExtra(OperationLinkedListActivity.LIST_BEAN, fList).putExtra(OperationLinkedListActivity.DEVICE_BEAN, bean))
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