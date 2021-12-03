package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.elvishew.xlog.XLog
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffSceneLinkedListCallback
import com.example.kiotsdk.adapter.scene.SceneLinkedListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneLinkedBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneLinkBean
import com.kunluiot.sdk.callback.IResultCallback
import org.jetbrains.anko.alert

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneLinkedActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneLinkedBinding

    private lateinit var mAdapter: SceneLinkedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneLinkedBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.add.setOnClickListener { gotoLinkedAddOrEdit.launch(Intent(this, SceneLinkedAddOrEditActivity::class.java).putExtra(SceneLinkedAddOrEditActivity.IS_ADD, true)) }

        initAdapter()
        getData()
    }

    private val gotoLinkedAddOrEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getData()
        }
    }

    private fun getData() {
        KunLuHomeSdk.sceneImpl.getLinkageSceneList(0, 999, { code, msg -> toastErrorMsg(code, msg) }, { setData(it) })
    }

    private fun setData(list: List<SceneLinkBean> = listOf()) {
        mAdapter.data.clear()
        mAdapter.addData(list as MutableList<SceneLinkBean>)
    }

    private fun initAdapter() {
        mAdapter = SceneLinkedListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffSceneLinkedListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.edit, R.id.off)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as SceneLinkBean
            when (view.id) {
                R.id.edit -> {
                    gotoLinkedAddOrEdit.launch(Intent(this, SceneLinkedAddOrEditActivity::class.java).putExtra(SceneLinkedAddOrEditActivity.BEAN, bean))
                }
                R.id.off -> {
                    KunLuHomeSdk.sceneImpl.updateLinkageScene(bean.ruleId, bean.enabled, bean, { code, msg -> toastErrorMsg(code, msg) }, { getData() })
                }
            }
        }
        mAdapter.setOnItemLongClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as SceneLinkBean
            gotoDelete(bean)
            false
        }
    }

    private fun gotoDelete(bean: SceneLinkBean) {
        alert(message = "是否删除") {
            positiveButton("确定") { dialog ->
                KunLuHomeSdk.sceneImpl.deleteLinkageScene(bean.ruleId, object : IResultCallback {
                    override fun onSuccess() {
                        getData()
                    }

                    override fun onError(code: String, error: String) {
                        toastErrorMsg(code, error)
                    }
                })
                dialog.dismiss()
            }
            negativeButton("取消") { dialog -> dialog.dismiss() }
        }.show()
    }
}