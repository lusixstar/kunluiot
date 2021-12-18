package com.example.kiotsdk.ui.family

import android.app.Activity
import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyCreateBinding
import com.kunluiot.sdk.KunLuHomeSdk
import org.jetbrains.anko.toast

class FamilyCreateActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyCreateBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.create.setOnClickListener { gotoNext() }
    }

    private fun gotoNext() {
        val name = mBinding.name.text.toString()
        val city = mBinding.city.text.toString()
        if (name.isEmpty()) {
            toast("name is empty")
            return
        }
        if (city.isEmpty()) {
            toast("city is empty")
            return
        }
        KunLuHomeSdk.familyImpl.addFamily(name, city, { code, msg -> toastErrorMsg(code, msg) }, {
            setResult(Activity.RESULT_OK)
            finish()
        })
    }
}