package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.scene.SceneOneKeyIconListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.bean.SceneIconBean
import com.example.kiotsdk.databinding.ActivitySceneOneKeyIconEditBinding
import com.example.kiotsdk.util.DemoUtils

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneOneKeyIconEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneOneKeyIconEditBinding

    private lateinit var mAdapter: SceneOneKeyIconListAdapter

    private var mSelectIcon = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneOneKeyIconEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.next.setOnClickListener { gotoNext() }

        initAdapter()
    }

    private fun gotoNext() {
        if (mSelectIcon != -1) {
            setResult(Activity.RESULT_OK, intent.putExtra(ICON, mSelectIcon))
            finish()
        } else {
            toastMsg("no select icon")
        }
    }

    private fun initAdapter() {
        val icons = DemoUtils.getSceneIcon()
        val list = icons.map { SceneIconBean(false, it) }
        mAdapter = SceneOneKeyIconListAdapter(list as MutableList<SceneIconBean>)
        val manager = GridLayoutManager(this, 5)
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.layoutManager = manager
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            if (mAdapter.data[position].select) return@setOnItemClickListener
            mAdapter.data.forEach { it.select = false }
            mAdapter.data[position].select = true
            mAdapter.notifyItemRangeChanged(0, mAdapter.data.size)
            mSelectIcon = mAdapter.data[position].id
        }
    }

    companion object {
        const val ICON = "icon"
    }
}