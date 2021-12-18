package com.example.kiotsdk.ui.feedback

import android.app.ProgressDialog
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.RegexUtils
import com.example.kiotsdk.R
import com.example.kiotsdk.adapter.feedback.FeedbackAdapter
import com.example.kiotsdk.app.GlideEngine
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.bean.FeedbackImgBean
import com.example.kiotsdk.databinding.ActivityFeedbackBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import org.jetbrains.anko.toast

/**
 * User: Chris
 * Date: 2021/12/7
 * Desc:
 */

class FeedbackActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFeedbackBinding

    private lateinit var mAdapter: FeedbackAdapter

    private var mSelectImg = mutableListOf<LocalMedia>()

    private lateinit var mProgress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFeedbackBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.btn.setOnClickListener { gotoNext() }
        initAdapter()

        mProgress = ProgressDialog(this)
        mProgress.setCanceledOnTouchOutside(false)
    }

    private fun gotoNext() {
        val content = mBinding.etContent.text.toString().trim()
        val phone = mBinding.contact.text.toString().trim()
        if (content.isEmpty()) {
            toastMsg("content is empty")
            return
        }
        if (phone.isEmpty()) {
            toastMsg("phone is empty")
            return
        }
        if (!RegexUtils.isMobileSimple(phone)) {
            toast("phone error")
            return
        }
        mProgress.setMessage("正在加载中")
        mProgress.show()
        val images = mutableListOf<String>()
        val urls = mSelectImg.map { it.realPath }
        urls.forEach {
            KunLuHomeSdk.userImpl.uploadHeader(it, { _, _ -> }, { url ->
                images.add(url.fileCDNUrl)
                if (images.size == urls.size) {
                    KunLuHomeSdk.commonImpl.feedback(content, images, phone, { c, m ->
                        mProgress.hide()
                        toastErrorMsg(c, m)
                    }, {
                        mProgress.hide()
                        toastMsg("success")
                    })
                }
            })
        }
        if (urls.isEmpty()) {
            KunLuHomeSdk.commonImpl.feedback(content, listOf(), phone, { c, m ->
                mProgress.hide()
                toastErrorMsg(c, m)
            }, {
                mProgress.hide()
                toastMsg("success")
            })
        }
    }


    private fun initAdapter() {
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        mAdapter = FeedbackAdapter(arrayListOf())
        mBinding.list.layoutManager = manager
        mBinding.list.adapter = mAdapter
        val imgBean = FeedbackImgBean(type = "add")
        val list = listOf(imgBean)
        mAdapter.addData(list)
        mAdapter.setOnItemClickListener { _, _, _ -> selectImg() }
        mAdapter.addChildClickViewIds(R.id.cancel)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.getItem(position) as FeedbackImgBean
            if (view.id == R.id.cancel) {
                val info = mSelectImg.first { it.realPath == bean.url }
                mSelectImg.remove(info)
                val l = mSelectImg.map { FeedbackImgBean(type = "img", url = it.realPath) } as MutableList
                l.add(FeedbackImgBean(type = "add"))
                mAdapter.data.clear()
                mAdapter.addData(l)
            }
        }
    }

    private fun selectImg() {
        PictureSelector.create(this).openGallery(PictureMimeType.ofAll()).isCamera(false).isCompress(true).selectionData(mSelectImg).imageEngine(GlideEngine.createGlideEngine()).maxSelectNum(3).selectionMode(PictureConfig.MULTIPLE).forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: List<LocalMedia>) {
                mSelectImg = result as MutableList<LocalMedia>
                val list = mSelectImg.map { FeedbackImgBean(type = "img", url = it.realPath) } as MutableList
                list.add(FeedbackImgBean(type = "add"))
                mAdapter.data.clear()
                mAdapter.addData(list)
            }

            override fun onCancel() {

            }
        })
    }
}