package com.example.kiotsdk.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySplashBinding
import com.example.kiotsdk.ui.user.LoginActivity
import com.example.kiotsdk.ui.user.RegisterActivity
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.common.CommonProblemBean
import com.kunluiot.sdk.bean.common.CommonThirdPlatformBean
import com.kunluiot.sdk.bean.user.User
import com.kunluiot.sdk.callback.IResultCallback
import com.kunluiot.sdk.callback.common.ICommonProblemCallback
import com.kunluiot.sdk.callback.common.ICommonThirdPlatformCallback
import com.kunluiot.sdk.callback.user.ILoginCallback
import com.kunluiot.sdk.thirdlib.qrcode.util.LogUtils
import com.kunluiot.sdk.thirdlib.ws.websocket.util.LogUtil
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getVersion()

        mBinding.login.setOnClickListener { activityResultLauncher.launch(Intent(this, LoginActivity::class.java)) }
        mBinding.register.setOnClickListener { testGo() }
//        mBinding.register.setOnClickListener { startActivity<RegisterActivity>() }
    }

    private fun testGo() {
        KunLuHomeSdk.commonImpl.getBindThirdPlatformList(testCallback)
    }

    private val testCallback = object : ICommonThirdPlatformCallback {

        override fun onSuccess(bean: CommonThirdPlatformBean) {
            LogUtils.e("bean == $bean")
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            finish()
        }
    }

    private fun getVersion() {
        try {
            val pInfo = this.packageManager.getPackageInfo(this.packageName, 0)
            val version = String.format(getString(R.string.app_version_tips), pInfo.versionName)
            mBinding.version.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}