package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffSceneLinkedDeviceListCallback
import com.example.kiotsdk.adapter.scene.SceneDeviceListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneOneKeyEditOrAddBinding
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean
import com.kunluiot.sdk.callback.IResultCallback
import org.jetbrains.anko.alert

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneOneKeyAddOrEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneOneKeyEditOrAddBinding

    private var mIsAdd = false
    private var mBean: SceneOneKeyBean = SceneOneKeyBean()

    private var mSceneName = ""
    private var mOneKeyType = 0
    private var mSelectIcon: String = ""

    private lateinit var mAdapter: SceneDeviceListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneOneKeyEditOrAddBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        getIntentData()

        mBinding.titleLayout.setOnClickListener { if (mOneKeyType == 0) gotoEditName.launch(Intent(this, SceneNameEditActivity::class.java).putExtra(SceneNameEditActivity.NAME, mSceneName)) }
        mBinding.iconLayout.setOnClickListener { if (mOneKeyType == 0) gotoEditIcon.launch(Intent(this, SceneOneKeyIconEditActivity::class.java)) }
        mBinding.addDevices.setOnClickListener { gotoAddDevices.launch(Intent(this, SelectExecutionActionActivity::class.java).putExtra(SelectExecutionActionActivity.IS_ONE_KEY, true)) }
        mBinding.next.setOnClickListener { gotoSave() }

        initAdapter()
    }

    private fun gotoSave() {
        if (mSceneName.isEmpty()) {
            toastMsg("scene name is empty")
            return
        }
        if (mSelectIcon.isEmpty()) {
            toastMsg("scene icon is empty")
            return
        }
        if (mAdapter.data.isNullOrEmpty()) {
            toastMsg("please add one control action")
            return
        }
        if (mIsAdd) {
            KunLuHomeSdk.sceneImpl.addOneKeyScene(mOneKeyType, mSelectIcon, mSceneName, mAdapter.data.toList(), mapOf(), "", object : IResultCallback {
                override fun onError(code: String, error: String) {
                    toastErrorMsg(code, error)
                }

                override fun onSuccess() {
                    toastMsg("success")
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            })
        } else {
            KunLuHomeSdk.sceneImpl.updateOneKeyScene(mBean.sceneId, mOneKeyType, mSelectIcon, mSceneName, mAdapter.data.toList(), mapOf(), object : IResultCallback {
                override fun onError(code: String, error: String) {
                    toastErrorMsg(code, error)
                }

                override fun onSuccess() {
                    toastMsg("success")
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            })
        }
    }

    private fun initAdapter() {
        mAdapter = SceneDeviceListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffSceneLinkedDeviceListCallback())
        mBinding.list.adapter = mAdapter
        if (!mIsAdd) {
            mAdapter.setDiffNewData(mBean.sceneTaskList.toMutableList())
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            alert("是否删除") {
                positiveButton("确定") { dialog ->
                    mAdapter.removeAt(position)
                    dialog.dismiss()
                }
            }.show()
        }
    }

    private val gotoAddDevices = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val delay = it.data?.getStringExtra(SelectExecutionActionActivity.DELAY) ?: ""
            if (delay.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.DELAY_BEAN) ?: SceneLinkedBean()
                mAdapter.data.add(mAdapter.data.size, bean)
                mAdapter.notifyItemRangeChanged(0, mAdapter.data.size)
                XLog.e("bean == $bean")
            }
            val scene = it.data?.getStringExtra(SelectExecutionActionActivity.SCENE) ?: ""
            if (scene.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.SCENE_BEAN) ?: SceneLinkedBean()
                mAdapter.data.add(mAdapter.data.size, bean)
                mAdapter.notifyItemRangeChanged(0, mAdapter.data.size)
                XLog.e("bean == $bean")
            }
            val device = it.data?.getStringExtra(SelectExecutionActionActivity.DEVICE) ?: ""
            if (device.isNotEmpty()) {
                val bean = it.data?.getParcelableExtra(SelectExecutionActionActivity.DEVICE_BEAN) ?: SceneLinkedBean()
                mAdapter.data.add(mAdapter.data.size, bean)
                mAdapter.notifyItemRangeChanged(0, mAdapter.data.size)
                XLog.e("bean == $bean")
            }
        }
    }

    private val gotoEditName = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            mSceneName = it.data?.getStringExtra(SceneNameEditActivity.NAME) ?: ""
            mBinding.titleValue.text = "${mSceneName}   >"
        }
    }

    private val gotoEditIcon = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val res = it.data?.getIntExtra(SceneOneKeyIconEditActivity.ICON, -1) ?: -1
            if (res != -1) {
                mSelectIcon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, res)
                val imageBase64 = "data:image/png;base64,"
                mBinding.icon.setImageBitmap(DemoUtils.base64CodeToBitmap(mSelectIcon.substring(imageBase64.length, mSelectIcon.length)))
            }
        }
    }

    private fun getIntentData() {
        intent?.let {
            mIsAdd = it.getBooleanExtra(IS_ADD, false)
            if (!mIsAdd) mBean = it.getParcelableExtra(BEAN) ?: SceneOneKeyBean()
            if (mIsAdd) mBinding.toolBar.title = "创建${resources.getString(R.string.scene_one_key_scene)}"
            else mBinding.toolBar.title = "编辑${resources.getString(R.string.scene_one_key_scene)}"
            if (!mIsAdd) {
                mSceneName = mBean.sceneName
                mOneKeyType = mBean.oneKeyType
                mSelectIcon = mBean.icon
                if (mOneKeyType == 0) mBinding.titleValue.text = "$mSceneName   >"
                else mBinding.titleValue.text = mSceneName
                if (mSelectIcon.isNotEmpty()) {
                    val imageBase64 = "data:image/png;base64,"
                    mBinding.icon.setImageBitmap(DemoUtils.base64CodeToBitmap(mSelectIcon.substring(imageBase64.length, mSelectIcon.length)))
                }
                if (mOneKeyType == 0) mBinding.iconNext.visibility = View.VISIBLE else mBinding.iconNext.visibility = View.GONE
            }
        }
    }

    companion object {
        const val IS_ADD = "is_add"
        const val BEAN = "bean"
    }
}