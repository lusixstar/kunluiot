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
import com.kunluiot.sdk.bean.scene.*
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
            KunLuHomeSdk.sceneImpl.getLinkageSceneList({ code, msg -> toastErrorMsg(code, msg) }, { mAdapter.setDiffNewData(it as MutableList<SceneLinkBeanNew>) })
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
                val bean = adapter.getItem(position) as SceneLinkBeanNew
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
        val customParamBean = SceneCustomFieldsBeanNew()
        customParamBean.name = bean.sceneName
        customParamBean.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_scene)

        val iftttTasksParam = SceneIftttTasksParamBeanNew()
        iftttTasksParam.sceneId = bean.sceneId

        val iftttTasksBean = SceneIftttTasksListBeanNew()
        iftttTasksBean.customParam = customParamBean
        iftttTasksBean.params = iftttTasksParam
        iftttTasksBean.desc = "执行场景"
        iftttTasksBean.type = "SCENETRIGGERSEND"
        setResult(Activity.RESULT_OK, intent.putExtra(SCENE_LINK, SCENE_LINK).putExtra(SCENE_LINK_BEAN, iftttTasksBean))
        finish()
    }

    private fun gotoNext(bean: SceneLinkBeanNew) {
        selector("场景开关设置", listOf("开启", "关闭")) { dialog, i ->
            val customParamBean = SceneCustomFieldsBeanNew()
            customParamBean.name = bean.ruleName
            customParamBean.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_scene)
            val oneKeyTask = SceneOneKeyTaskListBean()
            oneKeyTask.customParam = customParamBean
            oneKeyTask.iftttId = bean.ruleId
            if (i == 0) {
                oneKeyTask.desc = "开启"
                oneKeyTask.enable = "ENABLE"
            } else {
                oneKeyTask.desc = "关闭"
                oneKeyTask.enable = "DISABLE"
            }
            setResult(Activity.RESULT_OK, intent.putExtra(SCENE, SCENE).putExtra(SCENE_BEAN, oneKeyTask))
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