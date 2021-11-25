package com.example.kiotsdk.ui.user

import android.os.Bundle
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiotsdk.app.GlideEngine
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityUserInfoBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.user.User
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


class UserInfoActivity : BaseActivity() {

    private lateinit var mBinding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }


        mBinding.update.setOnClickListener { updateName() }
        mBinding.avatarUpdate.setOnClickListener { selectAvatar() }

        getUserInfoData()
    }

    private fun updateAvatar(filePath: String) {
        KunLuHomeSdk.userImpl.uploadHeader(filePath, { code, err -> toastErrorMsg(code, err) }, { toastMsg("update success $it") })
    }

    private fun selectAvatar() {
        PictureSelector.create(this).openGallery(PictureMimeType.ofAll()).imageEngine(GlideEngine.createGlideEngine()).selectionMode(PictureConfig.SINGLE).forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: List<LocalMedia>) {
                val filePath: String = result[0].realPath
                updateAvatar(filePath)
            }

            override fun onCancel() {

            }
        })
    }

    private fun updateName() {
        val name = mBinding.etName.text.toString()
        if (name.isEmpty()) return
        KunLuHomeSdk.userImpl.updateUserNick(name, { code, err -> toastErrorMsg(code, err) }, { toastMsg("update success") })
    }

    private fun getUserInfoData() {
        KunLuHomeSdk.userImpl.getUserInfo({ code, err -> toastErrorMsg(code, err) }, { setUserInfo(it) })
    }

    private fun setUserInfo(user: User) {
        mBinding.etName.setText(user.name)
        mBinding.phoneValue.text = user.phoneNumber
        mBinding.emailValue.text = user.email
        mBinding.countryValue.text = user.areaCode
        user.avatarUrl.small.let {
            if (it.isNotEmpty()) {
                mBinding.imgAvatar.load(user.avatarUrl.small) {
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}