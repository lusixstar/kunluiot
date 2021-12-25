package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffSceneLinkedListCallback
import com.example.kiotsdk.adapter.scene.SceneLinkedListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneLinkedBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneLinkBeanNew
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
        KunLuHomeSdk.sceneImpl.getLinkageSceneList({ code, msg -> toastErrorMsg(code, msg) }, { setData(it) })
    }

    private fun setData(list: List<SceneLinkBeanNew> = listOf()) {
        mAdapter.data.clear()
        mAdapter.addData(list as MutableList<SceneLinkBeanNew>)
    }

    private fun initAdapter() {
        mAdapter = SceneLinkedListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffSceneLinkedListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.edit, R.id.off)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as SceneLinkBeanNew
            when (view.id) {
                R.id.edit -> {
                    gotoLinkedAddOrEdit.launch(Intent(this, SceneLinkedAddOrEditActivity::class.java).putExtra(SceneLinkedAddOrEditActivity.BEAN, bean))
                }
                R.id.off -> {
                    if (bean.conditionList.isNullOrEmpty()) {
                        toastMsg("请添加条件")
                        return@setOnItemChildClickListener
                    }
                    if (bean.iftttTasks.isNullOrEmpty()) {
                        toastMsg("请添加执行动作")
                        return@setOnItemChildClickListener
                    }
                    KunLuHomeSdk.sceneImpl.updateLinkageScene(bean.ruleId, !bean.enabled, bean, { code, msg -> toastErrorMsg(code, msg) }, { getData() })
                }
            }
        }
        mAdapter.setOnItemLongClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as SceneLinkBeanNew
            gotoDelete(bean)
            false
        }
    }

    private fun gotoDelete(bean: SceneLinkBeanNew) {
        alert(message = "是否删除") {
            positiveButton("确定") { dialog ->
                KunLuHomeSdk.sceneImpl.deleteLinkageScene(bean.ruleId, "", { c, m -> toastErrorMsg(c, m) }, {
                    if (it.randomToken.isNotEmpty()) {
                        KunLuHomeSdk.sceneImpl.deleteLinkageScene(bean.ruleId, it.randomToken, { c, m -> toastErrorMsg(c, m) }, {
                            getData()
                        })
                    } else {
                        getData()
                    }
                })
                dialog.dismiss()
            }
            negativeButton("取消") { dialog -> dialog.dismiss() }
        }.show()
    }
}