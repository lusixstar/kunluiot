package com.example.kiotsdk.ui.user

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityUserEditNameBinding
import com.kunluiot.sdk.KunLuHomeSdk


class UserEditNameActivity : BaseActivity() {

    private lateinit var mBinding: ActivityUserEditNameBinding

    private var mName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUserEditNameBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let { mName = it.getStringExtra(NAME) ?: "" }
        mBinding.etName.setText(mName)

        mBinding.btnFinish.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        val name = mBinding.etName.text.toString().trim()
        if (name.isEmpty()) {
            toastMsg("name is empty")
            return
        }
        KunLuHomeSdk.userImpl.updateUserNick(name, { code, msg -> toastErrorMsg(code, msg) }, {
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

    companion object {
        const val NAME = "name"
    }
}