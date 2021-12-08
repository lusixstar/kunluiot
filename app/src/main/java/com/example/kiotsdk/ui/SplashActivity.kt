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
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySplashBinding

    private var mIsFinish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        intent?.let { mIsFinish = it.getBooleanExtra(GO_FINISH, false) }
        getVersion()

        mBinding.login.setOnClickListener { activityResultLauncher.launch(Intent(this, LoginActivity::class.java)) }
        mBinding.register.setOnClickListener { startActivity<RegisterActivity>() }

        refreshLoginToken()
    }

    private fun refreshLoginToken() {
        val token = KunLuHomeSdk.instance.getSessionBean()?.refreshToken ?: ""
        if (token.isNotEmpty()) {
            KunLuHomeSdk.userImpl.refreshToken(token, { code, err -> toastErrorMsg(code, err) }, {
                setResult(Activity.RESULT_OK, intent)
                startActivity<MainNewActivity>()
                finish()
                toast("login success")
            })
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            finish()
        }
    }

    override fun onBackPressed() {
        if (mIsFinish) {
            finish()
        } else {
            super.onBackPressed()
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

    companion object {
        const val GO_FINISH = "go_finish"
    }
}