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
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyCustomParam

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
        mBinding.device.setOnClickListener { gotoAddDevice.launch(Intent(this, SceneSelectDevicesActivity::class.java)) }
        mBinding.scene.setOnClickListener { gotoAddScene.launch(Intent(this, SelectSceneActivity::class.java).putExtra(IS_ONE_KEY, mIsOneKey)) }

        intent?.let { mIsOneKey = it.getBooleanExtra(IS_ONE_KEY, false) }
    }

    private val gotoAddDevice = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val device = it.data?.getStringExtra(OperationListActivity.DEVICE) ?: ""
            val deviceBean = it.data?.getParcelableExtra(OperationListActivity.DEVICE_BEAN) ?: SceneLinkedBean()
            if (device == OperationListActivity.DEVICE) {
                setResult(Activity.RESULT_OK, intent.putExtra(DEVICE, DEVICE).putExtra(DEVICE_BEAN, deviceBean))
                finish()
            }
        }
    }

    private val gotoAddScene = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val scene = it.data?.getStringExtra(SelectSceneActivity.SCENE) ?: ""
            if (scene == SelectSceneActivity.SCENE) {
                val sceneBean = it.data?.getParcelableExtra(SelectSceneActivity.SCENE_BEAN) ?: SceneLinkedBean()
                setResult(Activity.RESULT_OK, intent.putExtra(SCENE, SCENE).putExtra(SCENE_BEAN, sceneBean))
                finish()
            }
            val sceneLink = it.data?.getStringExtra(SelectSceneActivity.SCENE_LINK) ?: ""
            if (sceneLink == SelectSceneActivity.SCENE_LINK) {
                val sceneLinkBean = it.data?.getParcelableExtra(SelectSceneActivity.SCENE_LINK_BEAN) ?: SceneLinkedBean()
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
        val customParamBean = SceneOneKeyCustomParam()
        customParamBean.name = resources.getString(R.string.delayed)
        customParamBean.icon = "data:image/png;base64," + DemoUtils.bitmapToBase64(this, R.mipmap.ic_scene_select_delay)
        val eventData = SceneLinkedBean()
        eventData.customParam = customParamBean
        eventData.desc = desc
        eventData.time = time
        eventData.type = "DALAYTIME"
        setResult(Activity.RESULT_OK, intent.putExtra(DELAY, DELAY).putExtra(DELAY_BEAN, eventData))
        finish()
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