package com.example.kiotsdk.ui.family

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.SimpleItemAnimator
import com.elvishew.xlog.XLog
import com.example.kiotsdk.adapter.family.FamilyMemberListMoreAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyMemberManagerBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FamilyMemberMapBean
import com.kunluiot.sdk.bean.family.FolderBean
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

class FamilyMemberManagerActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyMemberManagerBinding

    private lateinit var mAdapter: FamilyMemberListMoreAdapter

    private var mFamilyId = ""
    private var mType = ""

    private var mFolderList = arrayListOf<FolderBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyMemberManagerBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mFamilyId = it.getStringExtra(FAMILY_ID) ?: ""
            mType = it.getStringExtra(FAMILY_TYPE) ?: ""
            mFolderList = it.getParcelableArrayListExtra(FAMILY_FOLDER) ?: arrayListOf()
        }

        initAdapter()
        getFamilyList()
    }

    private fun getFamilyList() {
        mFamilyId = intent.getStringExtra(FamilyDetailsActivity.FAMILY_ID) ?: ""
        if (mFamilyId.isNotEmpty()) KunLuHomeSdk.familyImpl.getFamilyDetails(mFamilyId, { code, msg -> toastErrorMsg(code, msg) }, { setFamilyData(it) })
    }

    private fun setFamilyData(bean: FamilyBean) {
        val list = mutableListOf<FamilyMemberMapBean>()
        bean.familyMemberMap.forEach { (_, u) ->
            if (u.type == mType) {
                list.add(u)
            }
        }
        mAdapter.data.clear()
        mAdapter.addData(list)
    }

    private fun initAdapter() {
        mAdapter = FamilyMemberListMoreAdapter(arrayListOf())
        (mBinding.list.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.list.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as FamilyMemberMapBean
            gotoEdit.launch(Intent(this, MemberEditActivity::class.java)
                .putExtra(MemberEditActivity.MEMBER_DEVICE_BEAN, bean)
                .putExtra(MemberEditActivity.MEMBER_TYPE, mType)
                .putExtra(MemberEditActivity.MEMBER_FAMILY_ID, mFamilyId)
                .putExtra(MemberEditActivity.MEMBER_FOLDER, mFolderList))
        }
        mAdapter.setOnItemLongClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as FamilyMemberMapBean
            delete(bean.uid)
            false
        }
    }

    private val gotoEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            getFamilyList()
        }
    }

    private fun delete(uid: String) {
        alert("是否删除") {
            positiveButton("确定") { dialog ->
                KunLuHomeSdk.familyImpl.deleteFamilyMember(mFamilyId, uid, { c, m -> toastErrorMsg(c, m) }, {
                    setResult(Activity.RESULT_OK)
                    getFamilyList()
                })
                dialog.dismiss()
            }
        }.show()
    }

    companion object {
        const val FAMILY_FOLDER = "family_folder"
        const val FAMILY_TYPE = "family_type"
        const val FAMILY_ID = "family_id"
    }
}