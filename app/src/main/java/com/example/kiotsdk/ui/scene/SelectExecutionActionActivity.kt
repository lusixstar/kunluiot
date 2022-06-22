package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySelectExecutionActionBinding
import com.example.kiotsdk.ui.operation.OperationListActivity
import com.example.kiotsdk.util.DemoUtils
import com.example.kiotsdk.widget.SelectTimeDialog
import com.kunluiot.sdk.bean.scene.SceneCustomFieldsBeanNew
import com.kunluiot.sdk.bean.scene.SceneIftttTasksListBeanNew
import com.kunluiot.sdk.bean.scene.SceneIftttTasksParamBeanNew
import com.kunluiot.sdk.bean.scene.SceneOneKeyTaskListBean

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SelectExecutionActionActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySelectExecutionActionBinding

    private var mIsOneKey = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySelectExecutionActionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.delay.setOnClickListener { showSeekBarBottomDialog() }
        mBinding.device.setOnClickListener { gotoAddDevice.launch(Intent(this, SceneSelectDevicesActivity::class.java).putExtra(IS_ONE_KEY, mIsOneKey)) }
        mBinding.scene.setOnClickListener { gotoAddScene.launch(Intent(this, SelectSceneActivity::class.java).putExtra(IS_ONE_KEY, mIsOneKey)) }

        intent?.let { mIsOneKey = it.getBooleanExtra(IS_ONE_KEY, false) }
    }

    private val gotoAddDevice = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val device = it.data?.getStringExtra(OperationListActivity.DEVICE) ?: ""
            if (device == OperationListActivity.DEVICE) {
                if (mIsOneKey) {
                    val deviceBean = it.data?.getParcelableExtra(OperationListActivity.DEVICE_BEAN) ?: SceneOneKeyTaskListBean()
                    setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, deviceBean))
                    finish()
                } else {
                    val deviceBean = it.data?.getParcelableExtra(OperationListActivity.DEVICE_BEAN) ?: SceneIftttTasksListBeanNew()
                    setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, deviceBean))
                    finish()
                }
            }
        }
    }

    private val gotoAddScene = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val scene = it.data?.getStringExtra(SelectSceneActivity.SCENE) ?: ""
            if (scene == SelectSceneActivity.SCENE) {
                val sceneBean = it.data?.getParcelableExtra(SelectSceneActivity.SCENE_BEAN) ?: SceneOneKeyTaskListBean()
                setResult(Activity.RESULT_OK, intent.putExtra(SCENE, SCENE).putExtra(SCENE_BEAN, sceneBean))
                finish()
            }
            val sceneLink = it.data?.getStringExtra(SelectSceneActivity.SCENE_LINK) ?: ""
            if (sceneLink == SelectSceneActivity.SCENE_LINK) {
                val sceneLinkBean = it.data?.getParcelableExtra(SelectSceneActivity.SCENE_LINK_BEAN) ?: SceneIftttTasksListBeanNew()
                setResult(Activity.RESULT_OK, intent.putExtra(SCENE_LINK, SCENE_LINK).putExtra(SCENE_LINK_BEAN, sceneLinkBean))
                finish()
            }
        }
    }

    private fun showSeekBarBottomDialog() {
        val dialog = SelectTimeDialog(this)
        dialog.show()
        dialog.setTitleName(resources.getString(R.string.select_delay_time))
        dialog.setOnDialogClickListener(object : SelectTimeDialog.OnDialogClickListener {
            override fun onFinishClick(minute: String, second: String) {
                if (minute == "00" && second == "00") {
                    toastMsg(resources.getString(R.string.delay_cannot_be_0))
                    return
                }
                addSceneDelay(minute, second)
            }

            override fun onDismissClick() {

            }
        })
    }

    private fun addSceneDelay(selectMinute: String, selectSecond: String) {
        val minute = selectMinute.toInt()
        val second = selectSecond.toInt()
        val time = minute * 60 + second
        var desc = ""
        if (minute > 0) {
            desc = desc + minute + "m"
        }
        desc = desc + second + "s"

        if (mIsOneKey) {
            val customFields = SceneCustomFieldsBeanNew()
            customFields.name = resources.getString(R.string.delayed)
            customFields.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_delay)

            val oneKeyTask = SceneOneKeyTaskListBean()
            oneKeyTask.customParam = customFields
            oneKeyTask.time = time.toString()
            oneKeyTask.desc = desc
            setResult(Activity.RESULT_OK, intent.putExtra(DELAY, DELAY).putExtra(DELAY_BEAN, oneKeyTask))
            finish()
        } else {
            val customFields = SceneCustomFieldsBeanNew()
            customFields.name = resources.getString(R.string.delayed)
            customFields.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_delay)

            val iftttTasksParam = SceneIftttTasksParamBeanNew()
            iftttTasksParam.time = time.toString()

            val iftttTasksBean = SceneIftttTasksListBeanNew()
            iftttTasksBean.customParam = customFields
            iftttTasksBean.params = iftttTasksParam
            iftttTasksBean.desc = desc
            iftttTasksBean.type = "DALAYTIME"
            setResult(Activity.RESULT_OK, intent.putExtra(DELAY, DELAY).putExtra(DELAY_BEAN, iftttTasksBean))
            finish()
        }
    }

    companion object {
        const val DELAY = "delay"
        const val DELAY_BEAN = "delay_bean"

        const val SCENE = "scene"
        const val SCENE_BEAN = "scene_bean"

        const val SCENE_LINK = "scene_link"
        const val SCENE_LINK_BEAN = "scene_link_bean"

        const val DEVICE = "device"
        const val DEVICE_BEAN = "device_bean"

        const val IS_ONE_KEY = "is_one_key"
    }
}