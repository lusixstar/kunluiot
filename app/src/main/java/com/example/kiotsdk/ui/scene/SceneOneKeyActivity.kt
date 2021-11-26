package com.example.kiotsdk.ui.scene

import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.diff.DiffOneKeyListCallback
import com.example.kiotsdk.adapter.scene.SceneOneKeyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneOneKeyBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneOneKeyActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneOneKeyBinding

    private lateinit var mAdapter: SceneOneKeyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneOneKeyBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        initAdapter()
        getData()
    }

    private fun getData() {
        KunLuHomeSdk.sceneImpl.getOneKeySceneList({ _, _ -> getTemplate() }, { getTemplate(it) })
    }

    private fun getTemplate(list: List<SceneOneKeyBean> = listOf()) {
        KunLuHomeSdk.sceneImpl.getSceneTemplate({ _, _ -> setData(list) }, { setData(list, it) })
    }

    private fun setData(list: List<SceneOneKeyBean> = listOf(), templateList: List<SceneOneKeyBean> = listOf()) {

        val ids = mutableListOf<String>()
        val allList = mutableListOf<SceneOneKeyBean>()

        list.forEach {
            XLog.e("list name == ${it.name}  ${it.templateId}")
            allList.add(it)
            ids.add(it.templateId)
        }

        mAdapter.setDiffNewData(allList)
    }

    private fun initAdapter() {
        mAdapter = SceneOneKeyListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffOneKeyListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
    }
}