package com.example.kiotsdk.ui

import android.os.Bundle
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityMainNewBinding
import com.example.kiotsdk.ui.device.DeviceGotoActivity
import com.example.kiotsdk.ui.scene.SceneManagerActivity
import com.example.kiotsdk.ui.user.MineManagerActivity
import org.jetbrains.anko.startActivity

/**
 * User: Chris
 * Date: 2021/12/4
 * Desc:
 */

class MainNewActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainNewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnDeviceManager.setOnClickListener { startActivity<DeviceGotoActivity>() }
        mBinding.btnSceneLinked.setOnClickListener { startActivity<SceneManagerActivity>() }
        mBinding.btnMineCenter.setOnClickListener { startActivity<MineManagerActivity>() }
    }
}