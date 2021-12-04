package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.diff.DiffOneKeyListCallback
import com.example.kiotsdk.adapter.diff.DiffSceneLinkedListCallback
import com.example.kiotsdk.adapter.scene.SelectSceneLinkedListAdapter
import com.example.kiotsdk.adapter.scene.SelectSceneListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySelectSceneBinding
import com.example.kiotsdk.util.DemoUtils
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.scene.SceneLinkBean
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyCustomParam
import org.jetbrains.anko.selector

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SelectSceneActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySelectSceneBinding

    private lateinit var mAdapter: SelectSceneListAdapter
    private lateinit var mLinkedAdapter: SelectSceneLinkedListAdapter

    private var mIsOneKey = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySelectSceneBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mIsOneKey = it.getBooleanExtra(SelectExecutionActionActivity.IS_ONE_KEY, false) }

        initAdapter()
        getData()
    }

    private fun getData() {
        if (mIsOneKey) {
            KunLuHomeSdk.sceneImpl.getLinkageSceneList(0, 999, { code, msg -> toastErrorMsg(code, msg) }, { mAdapter.setDiffNewData(it as MutableList<SceneLinkBean>) })
        } else {
            KunLuHomeSdk.sceneImpl.getOneKeySceneList({ code, msg -> toastErrorMsg(code, msg) }, { mLinkedAdapter.setDiffNewData(it as MutableList<SceneOneKeyBean>) })
        }
    }

    private fun initAdapter() {
        if (mIsOneKey) {
            mAdapter = SelectSceneListAdapter(arrayListOf())
            mAdapter.setDiffCallback(DiffSceneLinkedListCallback())
            mBinding.list.adapter = mAdapter
            mAdapter.setOnItemClickListener { adapter, _, position ->
                val bean = adapter.getItem(position) as SceneLinkBean
                gotoNext(bean)
            }
        } else {
            mLinkedAdapter = SelectSceneLinkedListAdapter(arrayListOf())
            mLinkedAdapter.setDiffCallback(DiffOneKeyListCallback())
            mBinding.list.adapter = mLinkedAdapter
            mLinkedAdapter.setOnItemClickListener { adapter, _, position ->
                val bean = adapter.getItem(position) as SceneOneKeyBean
                gotoNextLink(bean)
            }
        }
    }

    private fun gotoNextLink(bean: SceneOneKeyBean) {
        val customParamBean = SceneOneKeyCustomParam()
        customParamBean.name = bean.sceneName
        customParamBean.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_scene)
        val eventData = SceneLinkedBean()
        eventData.customParam = customParamBean
        eventData.desc = "开启"
        eventData.type = "SCENETRIGGERSEND"
        eventData.iftttId = bean.sceneId
        eventData.enable = "ENABLE"
        setResult(Activity.RESULT_OK, intent.putExtra(SCENE_LINK, SCENE_LINK).putExtra(SCENE_LINK_BEAN, eventData))
        finish()
    }

    private fun gotoNext(bean: SceneLinkBean) {
        selector("场景开关设置", listOf("开启", "关闭")) { dialog, i ->
            val customParamBean = SceneOneKeyCustomParam()
            customParamBean.name = bean.ruleName
            customParamBean.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_scene)
            val eventData = SceneLinkedBean()
            eventData.customParam = customParamBean
            eventData.iftttId = bean.ruleId
            if (i == 0) {
                eventData.desc = "开启"
                eventData.enable = "ENABLE"
            } else {
                eventData.desc = "关闭"
                eventData.enable = "DISABLE"
            }
            eventData.type = "SCENETRIGGERSEND"
            setResult(Activity.RESULT_OK, intent.putExtra(SCENE, SCENE).putExtra(SCENE_BEAN, eventData))
            dialog.dismiss()
            finish()
        }
    }

    companion object {
        const val SCENE = "scene"
        const val SCENE_BEAN = "scene_bean"

        const val SCENE_LINK = "scene_link"
        const val SCENE_LINK_BEAN = "scene_link_bean"
    }
}