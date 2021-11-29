package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySelectExecutionActionBinding
import com.example.kiotsdk.util.DemoUtils
import com.example.kiotsdk.widget.SelectTimeDialog
import com.kunluiot.sdk.bean.scene.SceneLinkedBean
import com.kunluiot.sdk.bean.scene.SceneOneKeyCustomParam
import org.jetbrains.anko.startActivity

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SelectExecutionActionActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySelectExecutionActionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySelectExecutionActionBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.delay.setOnClickListener { showSeekBarBottomDialog() }
        mBinding.device.setOnClickListener { startActivity<SceneSelectDevicesActivity>() }
        mBinding.scene.setOnClickListener { gotoAddScene.launch(Intent(this, SelectSceneActivity::class.java)) }
    }

    private val gotoAddScene = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val scene = it.data?.getStringExtra(SelectSceneActivity.SCENE) ?: ""
            val sceneBean = it.data?.getParcelableExtra(SelectSceneActivity.SCENE_BEAN) ?: SceneLinkedBean()
            if (scene == SelectSceneActivity.SCENE) {
                setResult(Activity.RESULT_OK, intent.putExtra(SCENE, SCENE).putExtra(SCENE_BEAN, sceneBean))
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
        eventData.time = time.toString()
        setResult(Activity.RESULT_OK, intent.putExtra(DELAY, DELAY).putExtra(DELAY_BEAN, eventData))
        finish()
    }

    companion object {
        const val DELAY = "delay"
        const val DELAY_BEAN = "delay_bean"

        const val SCENE = "scene"
        const val SCENE_BEAN = "scene_bean"
    }
}