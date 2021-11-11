package com.example.kiotsdk.ui.family

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyCreateBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyCreateBean
import com.kunluiot.sdk.callback.family.ICreateFamilyCallback
import org.jetbrains.anko.toast

class FamilyCreateActivity :BaseActivity() {

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
        KunLuHomeSdk.familyImpl.createHome(name, city, createCallback)
    }

    private val createCallback = object : ICreateFamilyCallback {

        override fun onSuccess(bean: FamilyCreateBean) {
            toast("create success name: ${bean.familyName}")
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }
}