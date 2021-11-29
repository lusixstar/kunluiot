package com.example.kiotsdk.ui.scene

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySceneOneKeyNameEditBinding

/**
 * User: Chris
 * Date: 2021/11/26
 * Desc:
 */

class SceneOneKeyNameEditActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySceneOneKeyNameEditBinding

    private var mName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySceneOneKeyNameEditBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mName = it.getStringExtra(NAME) ?: "" }

        mBinding.edit.setText(mName)
        mBinding.next.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        val name = mBinding.edit.text.toString().trim()
        if (name.isEmpty()) {
            toastMsg("name is empty")
            return
        }
        setResult(Activity.RESULT_OK, intent.putExtra(NAME, name))
        finish()
    }

    companion object {
        const val NAME = "name"
    }
}