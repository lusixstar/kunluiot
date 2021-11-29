package com.example.kiotsdk.ui.scene

import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffOneKeyListCallback
import com.example.kiotsdk.adapter.scene.SceneOneKeyListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneOneKeyBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean
import org.jetbrains.anko.startActivity

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

        mBinding.add.setOnClickListener { startActivity<SceneOneKeyAddOrEditActivity>(SceneOneKeyAddOrEditActivity.IS_ADD to true) }

        initAdapter()
        getData()
    }

    private fun getData() {
        KunLuHomeSdk.sceneImpl.getOneKeySceneList({ code, msg -> toastErrorMsg(code, msg) }, { setData(it) })
    }

    private fun setData(list: List<SceneOneKeyBean> = listOf()) {
        mAdapter.setDiffNewData(list as MutableList<SceneOneKeyBean>)
    }

    private fun initAdapter() {
        mAdapter = SceneOneKeyListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffOneKeyListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.edit, R.id.play)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as SceneOneKeyBean
            when (view.id) {
                R.id.edit -> {
                    startActivity<SceneOneKeyAddOrEditActivity>(SceneOneKeyAddOrEditActivity.BEAN to bean)
                }
                R.id.play -> {
                    toastMsg("play")
                }
            }
        }
    }
}