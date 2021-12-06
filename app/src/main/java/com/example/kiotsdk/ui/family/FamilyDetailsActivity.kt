package com.example.kiotsdk.ui.family

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiotsdk.adapter.family.FamilyMemberListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyDetailsBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import com.kunluiot.sdk.bean.family.FamilyMemberMapBean
import com.kunluiot.sdk.bean.family.FolderBean

class FamilyDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyDetailsBinding

    private var mFamilyId = ""
    private var mIsUser = true

    private var mFamilyBean: FamilyBean = FamilyBean()

    private lateinit var mAdapterMember: FamilyMemberListAdapter
    private lateinit var mAdapterVisitor: FamilyMemberListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.layoutName.setOnClickListener { if (mIsUser) gotoEdit.launch(Intent(this, FamilyEditActivity::class.java).putExtra(FamilyEditActivity.FAMILY_ID, mFamilyId)) }
        mBinding.layoutAddress.setOnClickListener { if (mIsUser) gotoEdit.launch(Intent(this, FamilyEditActivity::class.java).putExtra(FamilyEditActivity.FAMILY_ID, mFamilyId)) }
        mBinding.layoutRoom.setOnClickListener { if (mIsUser) gotoRoom.launch(Intent(this, FamilyRoomListActivity::class.java).putExtra(FamilyRoomListActivity.FAMILY_ID, mFamilyId)) }
        mBinding.layoutMember.setOnClickListener {
            if (mIsUser) {
                if (mAdapterMember.data.size < 3) {
                    toastMsg("please add member")
                    return@setOnClickListener
                }
                val list = arrayListOf<FolderBean>()
                mFamilyBean.folderList.forEach { list.add(it) }
                gotoEdit.launch(Intent(this, FamilyMemberManagerActivity::class.java)
                    .putExtra(FamilyMemberManagerActivity.FAMILY_ID, mFamilyId)
                    .putExtra(FamilyMemberManagerActivity.FAMILY_TYPE, "FAMILY_MEMBER")
                    .putExtra(FamilyMemberManagerActivity.FAMILY_FOLDER, list))
            }
        }
        mBinding.layoutVisitor.setOnClickListener {
            if (mIsUser) {
                if (mAdapterVisitor.data.size < 2) {
                    toastMsg("please add visitor")
                    return@setOnClickListener
                }
                val list = arrayListOf<FolderBean>()
                mFamilyBean.folderList.forEach { list.add(it) }
                gotoEdit.launch(Intent(this, FamilyMemberManagerActivity::class.java)
                    .putExtra(FamilyMemberManagerActivity.FAMILY_ID, mFamilyId)
                    .putExtra(FamilyMemberManagerActivity.FAMILY_TYPE, "VISITORS")
                    .putExtra(FamilyMemberManagerActivity.FAMILY_FOLDER, list))
            }
        }

        initAdapter()
        getFamilyDetails()
    }

    private fun initAdapter() {
        val memberLayoutManager = LinearLayoutManager(this)
        memberLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mAdapterMember = FamilyMemberListAdapter(arrayListOf())
        mBinding.listMember.layoutManager = memberLayoutManager
        mBinding.listMember.adapter = mAdapterMember
        mAdapterMember.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as FamilyMemberMapBean
            if (bean.gender == "add") {
                gotoAdd.launch(Intent(this, MemberCreateActivity::class.java).putExtra(MemberCreateActivity.FAMILY_ID, mFamilyId).putExtra(MemberCreateActivity.FAMILY_TYPE, "FAMILY_MEMBER"))
            }
        }

        val visitorLayoutManager = LinearLayoutManager(this)
        visitorLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mAdapterVisitor = FamilyMemberListAdapter(arrayListOf())
        mBinding.listVisitor.layoutManager = visitorLayoutManager
        mBinding.listVisitor.adapter = mAdapterVisitor
        mAdapterVisitor.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.getItem(position) as FamilyMemberMapBean
            if (bean.gender == "add") {
                gotoAdd.launch(Intent(this, MemberCreateActivity::class.java).putExtra(MemberCreateActivity.FAMILY_ID, mFamilyId).putExtra(MemberCreateActivity.FAMILY_TYPE, "VISITORS"))
            }
        }
    }

    private val gotoAdd = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getFamilyDetails()
        }
    }

    private val gotoEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getFamilyDetails()
        }
    }

    private val gotoRoom = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            getFamilyDetails()
        }
    }

    private fun setFamilyData(bean: FamilyBean) {
        mFamilyBean = bean
        mBinding.nameValue.text = bean.familyName
        mBinding.addressValue.text = bean.detailAddress
        mBinding.roomValue.text = "共${bean.folderList.size}房间"

        val visitList = mutableListOf<FamilyMemberMapBean>()
        val memberList = mutableListOf<FamilyMemberMapBean>()
        memberList.add(FamilyMemberMapBean(name = bean.name, gender = bean.logo, phoneNumber = bean.contact))
        bean.familyMemberMap.forEach { (_, u) ->
            if (u.type == "VISITORS") {
                visitList.add(u)
            }
            if (u.type == "FAMILY_MEMBER") {
                memberList.add(u)
            }
        }
        val user = KunLuHomeSdk.instance.getSessionBean()?.user ?: ""
        mIsUser = bean.uid == user
        if (mIsUser) {
            mBinding.imgName.visibility = View.VISIBLE
            mBinding.imgAddress.visibility = View.VISIBLE
            mBinding.imgRoom.visibility = View.VISIBLE
            mBinding.imgMember.visibility = View.VISIBLE
            mBinding.imgVisitor.visibility = View.VISIBLE
            memberList.add(FamilyMemberMapBean(name = "添加成员", gender = "add", phoneNumber = ""))
            visitList.add(FamilyMemberMapBean(name = "添加成员", gender = "add", phoneNumber = ""))
        } else {
            mBinding.imgName.visibility = View.INVISIBLE
            mBinding.imgAddress.visibility = View.INVISIBLE
            mBinding.imgRoom.visibility = View.INVISIBLE
            mBinding.imgMember.visibility = View.INVISIBLE
            mBinding.imgVisitor.visibility = View.INVISIBLE
        }
        mAdapterMember.data.clear()
        mAdapterVisitor.data.clear()
        mAdapterMember.addData(memberList)
        mAdapterVisitor.addData(visitList)
    }

    private fun getFamilyDetails() {
        mFamilyId = intent.getStringExtra(FAMILY_ID) ?: ""
        if (mFamilyId.isNotEmpty()) KunLuHomeSdk.familyImpl.getFamilyDetails(mFamilyId, { code, msg -> toastErrorMsg(code, msg) }, { setFamilyData(it) })
    }

    companion object {
        const val FAMILY_ID = "family_id"
    }
}