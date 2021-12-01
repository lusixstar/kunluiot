package com.example.kiotsdk.ui.scene

import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffSceneLinkedListCallback
import com.example.kiotsdk.adapter.scene.SceneLinkedListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneLinkedBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneLinkBean
import org.jetbrains.anko.startActivity

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

        mBinding.add.setOnClickListener { startActivity<SceneLinkedAddOrEditActivity>(SceneLinkedAddOrEditActivity.IS_ADD to true) }

        initAdapter()
        getData()
    }

    private fun getData() {
        KunLuHomeSdk.sceneImpl.getLinkageSceneList(0, 999, { code, msg -> toastErrorMsg(code, msg) }, { setData(it) })
    }

    private fun setData(list: List<SceneLinkBean> = listOf()) {
        mAdapter.setDiffNewData(list as MutableList<SceneLinkBean>)
    }

    private fun initAdapter() {
        mAdapter = SceneLinkedListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffSceneLinkedListCallback())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
        mAdapter.addChildClickViewIds(R.id.edit, R.id.play)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as SceneLinkBean
            when (view.id) {
                R.id.edit -> {
                    startActivity<SceneLinkedAddOrEditActivity>(SceneLinkedAddOrEditActivity.BEAN to bean)
                }
                R.id.play -> {
                    toastMsg("play")
                }
            }
        }
    }
}