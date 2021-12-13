package com.example.kiotsdk.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import coil.transform.CircleCropTransformation
import com.example.kiotsdk.app.GlideEngine
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityUserInfoBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.user.AvatarBean
import com.kunluiot.sdk.bean.user.User
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import org.jetbrains.anko.startActivity


class UserInfoActivity : BaseActivity() {

    private lateinit var mBinding: ActivityUserInfoBinding

    private var mName = ""
    private var mPhone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.avatarLayout.setOnClickListener { selectAvatar() }
        mBinding.nameLayout.setOnClickListener { gotoName.launch(Intent(this, UserEditNameActivity::class.java).putExtra(UserEditNameActivity.NAME, mName)) }
//        mBinding.phoneLayout.setOnClickListener { gotoPhone.launch(Intent(this, UserEditPhoneActivity::class.java).putExtra(UserEditPhoneActivity.PHONE, mPhone)) }
        mBinding.changeLayout.setOnClickListener { startActivity<ChangeAccountPasswordActivity>() }
//        mBinding.otherAccountLayout.setOnClickListener { startActivity<BindSocialAccountActivity>() }

        getUserInfoData()
    }

    private val gotoName = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getUserInfoData()
        }
    }

    private val gotoPhone = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getUserInfoData()
        }
    }

    private fun updateAvatar(filePath: String) {
        KunLuHomeSdk.userImpl.uploadHeader(filePath, { code, err -> toastErrorMsg(code, err) }, { updateHeader(it) })
    }

    private fun updateHeader(avatarBean: AvatarBean) {
        val url = avatarBean.fileCDNUrl.replace("//", "/")
        KunLuHomeSdk.userImpl.updateHeader(url, { c, m -> toastErrorMsg(c, m) }, { getUserInfoData() })
    }

    private fun selectAvatar() {
        PictureSelector.create(this).openGallery(PictureMimeType.ofAll()).imageEngine(GlideEngine.createGlideEngine()).isCompress(true).isEnableCrop(true).withAspectRatio(1, 1).selectionMode(PictureConfig.SINGLE).forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: List<LocalMedia>) {
                    val filePath: String = result[0].realPath
                    updateAvatar(filePath)
                }

                override fun onCancel() {

                }
            })
    }

    private fun getUserInfoData() {
        KunLuHomeSdk.userImpl.getUserInfo({ code, err -> toastErrorMsg(code, err) }, { setUserInfo(it) })
    }

    private fun setUserInfo(user: User) {
        mName = user.name
        mPhone = user.phoneNumber
        mBinding.nameValue.text = user.name
        mBinding.phoneValue.text = user.phoneNumber
        user.avatarUrl.small.let {
            if (it.isNotEmpty()) {
                mBinding.imgAvatar.load(user.avatarUrl.small) {
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}