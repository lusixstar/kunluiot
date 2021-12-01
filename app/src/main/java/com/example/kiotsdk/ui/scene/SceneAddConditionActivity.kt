package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneAddConditionEditBinding
import com.kunluiot.sdk.bean.scene.AddTimeConditionEvent
import com.kunluiot.sdk.bean.scene.SceneConditionListParam

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneAddConditionActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneAddConditionEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneAddConditionEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.timeLayout.setOnClickListener { gotoTime.launch(Intent(this, SceneAddTimeConditionActivity::class.java)) }
        mBinding.deviceLayout.setOnClickListener { }
    }

    private val gotoTime = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val bean = it.data?.getParcelableExtra(SceneAddTimeConditionActivity.TIME_BEAN) ?: AddTimeConditionEvent()
            setResult(Activity.RESULT_OK, intent.putExtra(TIME, TIME).putExtra(TIME_BEAN, bean))
            finish()
        }
    }

    companion object {
        const val TIME = "time"
        const val TIME_BEAN = "time_bean"
    }
}