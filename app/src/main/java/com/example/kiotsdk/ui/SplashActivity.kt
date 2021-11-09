package com.example.kiotsdk.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.kiotsdk.R
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivitySplashBinding
import com.example.kiotsdk.ui.user.LoginActivity
import com.example.kiotsdk.ui.user.RegisterActivity
import org.jetbrains.anko.startActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getVersion()

        mBinding.login.setOnClickListener { activityResultLauncher.launch(Intent(this, LoginActivity::class.java)) }
        mBinding.register.setOnClickListener { startActivity<RegisterActivity>() }
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