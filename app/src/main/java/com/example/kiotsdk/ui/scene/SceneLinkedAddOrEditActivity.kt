package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.scene.SceneDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneLinkedEditOrAddBinding
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneLinkBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.callback.IResultCallback

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneLinkedAddOrEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneLinkedEditOrAddBinding

    private var mIsAdd = false
    private var mBean: SceneLinkBean = SceneLinkBean()

    private var mSceneName = ""
    private var mTriggerType = ""

    private lateinit var mAdapter: SceneDeviceListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneLinkedEditOrAddBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        getIntentData()

        mBinding.titleLayout.setOnClickListener { gotoEditName.launch(Intent(this, SceneNameEditActivity::class.java).putExtra(SceneNameEditActivity.NAME, mSceneName)) }
//        mBinding.iconLayout.setOnClickListener { if (mOneKeyType == 0) gotoEditIcon.launch(Intent(this, SceneOneKeyIconEditActivity::class.java)) }
//        mBinding.addDevices.setOnClickListener { gotoAddDevices.launch(Intent(this, SelectExecutionActionActivity::class.java)) }
//        mBinding.next.setOnClickListener { gotoSave() }

        initAdapter()
    }

    private fun gotoSave() {
        if (mSceneName.isEmpty()) {
            toastMsg("scene name is empty")
            return
        }
        if (mAdapter.data.isNullOrEmpty()) {
            toastMsg("please add one control action")
            return
        }
        if (mIsAdd) {
//            KunLuHomeSdk.sceneImpl.addOneKeyScene(mOneKeyType, mSelectIcon, mSceneName, mAdapter.data.toList(), mapOf(), "", object : IResultCallback {
//                override fun onError(code: String, error: String) {
//                    toastErrorMsg(code, error)
//                }
//
//                override fun onSuccess() {
//
//                }
//            })
        } else {
//            KunLuHomeSdk.sceneImpl.updateOneKeyScene(mBean.sceneId, mOneKeyType, mSelectIcon, mSceneName, mAdapter.data.toList(), mapOf(),  object : IResultCallback {
//                override fun onError(code: String, error: String) {
//                    toastErrorMsg(code, error)
//                }
//
//                override fun onSuccess() {
//
//                }
//            })
        }
    }

    private fun initAdapter() {
//        mAdapter = SceneDeviceListAdapter(mBean.sceneTaskList.toMutableList())
//        mBinding.list.adapter = mAdapter
    }

    private val gotoAddDevices = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val delay = it.data?.getStringExtra(SelectExecutionActionActivity.DELAY) ?: ""
            if (delay.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.DELAY_BEAN) ?: SceneLinkedBean()
                mAdapter.data.add(0, bean)
                mAdapter.notifyItemInserted(0)
            }
            val scene = it.data?.getStringExtra(SelectExecutionActionActivity.SCENE) ?: ""
            if (scene.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.SCENE_BEAN) ?: SceneLinkedBean()
                mAdapter.data.add(0, bean)
                mAdapter.notifyItemInserted(0)
            }
            val device = it.data?.getStringExtra(SelectExecutionActionActivity.DEVICE) ?: ""
            if (device.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.DEVICE_BEAN) ?: SceneLinkedBean()
                mAdapter.data.add(0, bean)
                mAdapter.notifyItemInserted(0)
            }
        }
    }

    private val gotoEditName = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            mSceneName = it.data?.getStringExtra(SceneNameEditActivity.NAME) ?: ""
            mBinding.titleValue.text = "${mSceneName}   >"
        }
    }

    private fun getIntentData() {
        intent?.let {
            mIsAdd = it.getBooleanExtra(IS_ADD, false)
            if (!mIsAdd) mBean = it.getParcelableExtra(BEAN) ?: SceneLinkBean()
            if (mIsAdd) mBinding.toolBar.title = "创建${resources.getString(R.string.scene_linked)}${resources.getString(R.string.scene)}"
            else mBinding.toolBar.title = "编辑${resources.getString(R.string.scene_linked)}${resources.getString(R.string.scene)}"
            if (!mIsAdd) {
                mSceneName = mBean.ruleName
                mTriggerType = mBean.triggerType
                mBinding.titleValue.text = "$mSceneName   >"
                val timeTrigger = if (mTriggerType == "SCHEDULER") mBean.conditionList.first().conDesc else ""
                val deviceTrigger = if (mTriggerType == "SCHEDULER") "" else mBean.desc

            }
        }
    }

    companion object {
        const val IS_ADD = "is_add"
        const val BEAN = "bean"
    }
}