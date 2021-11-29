package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffSceneLinkedListCallback
import com.example.kiotsdk.adapter.scene.SelectSceneListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySelectSceneBinding
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneLinkBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyCustomParam

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SelectSceneActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySelectSceneBinding

    private lateinit var mAdapter: SelectSceneListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySelectSceneBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        initAdapter()
        getData()
    }

    private fun getData() {
        KunLuHomeSdk.sceneImpl.getLinkageSceneList(0, 999, { code, msg -> toastErrorMsg(code, msg) }, { mAdapter.setDiffNewData(it as MutableList<SceneLinkBean>) })
    }

    private fun initAdapter() {
        mAdapter = SelectSceneListAdapter(arrayListOf())
        mAdapter.setDiffCallback(DiffSceneLinkedListCallback())
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as SceneLinkBean
            gotoNext(bean)
        }
    }

    private fun gotoNext(bean: SceneLinkBean) {
        val customParamBean = SceneOneKeyCustomParam()
        customParamBean.name = bean.ruleName
        customParamBean.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_scene)
        val eventData = SceneLinkedBean()
        eventData.customParam = customParamBean
        eventData.desc = resources.getString(R.string.open_)
        eventData.iftttId = bean.ruleId
        eventData.enable = "ENABLE"
        setResult(Activity.RESULT_OK, intent.putExtra(SCENE, SCENE).putExtra(SCENE_BEAN, eventData))
        finish()
    }

    companion object {
        const val SCENE = "scene"
        const val SCENE_BEAN = "scene_bean"
    }
}