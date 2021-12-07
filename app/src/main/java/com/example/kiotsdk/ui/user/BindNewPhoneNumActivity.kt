package com.example.kiotsdk.ui.user

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityUserBindNewPhoneBinding

/**
 * User: Chris
 * Date: 2021/12/4
 * Desc:
 */

class BindNewPhoneNumActivity : BaseActivity() {

    private lateinit var mBinding: ActivityUserBindNewPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUserBindNewPhoneBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.btnFinish.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        val phone = mBinding.etNewPhone.text.toString().trim()
        if (phone.isEmpty()) {
            toastMsg("phone is empty")
            return
        }
        setResult(Activity.RESULT_OK, intent.putExtra(NEW_PHONE, phone))
        finish()
    }

    companion object {
        const val NEW_PHONE = "new_phone"
    }
}