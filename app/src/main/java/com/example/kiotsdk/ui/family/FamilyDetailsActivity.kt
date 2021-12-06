package com.example.kiotsdk.ui.family

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiotsdk.adapter.family.FamilyMemberListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityFamilyDetailsBinding
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.family.FamilyBean
import org.jetbrains.anko.startActivity

class FamilyDetailsActivity : BaseActivity() {

    private lateinit var mBinding: ActivityFamilyDetailsBinding

    private var mFamilyId = ""

    private var mFamilyBean: FamilyBean = FamilyBean()

    private lateinit var mAdapterMember: FamilyMemberListAdapter
    private lateinit var mAdapterVisitor: FamilyMemberListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityFamilyDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        mBinding.layoutName.setOnClickListener { gotoEdit.launch(Intent(this, FamilyEditActivity::class.java).putExtra(FamilyEditActivity.FAMILY_ID, mFamilyId)) }
        mBinding.layoutAddress.setOnClickListener { gotoEdit.launch(Intent(this, FamilyEditActivity::class.java).putExtra(FamilyEditActivity.FAMILY_ID, mFamilyId)) }
        mBinding.layoutRoom.setOnClickListener {
            gotoEdit.launch(Intent(this, FamilyRoomListActivity::class.java).putExtra(FamilyRoomListActivity.FAMILY_ID, mFamilyId))
        }
//        mBinding.delete.setOnClickListener { deleteFamily() }

        initAdapter()
        getFamilyDetails()
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mAdapterMember = FamilyMemberListAdapter(arrayListOf())
        mBinding.listMember.layoutManager = layoutManager
        mBinding.listMember.adapter = mAdapterMember

//        mAdapterVisitor = FamilyMemberListAdapter(arrayListOf())
//        mBinding.listVisitor.layoutManager = layoutManager
//        mBinding.listVisitor.adapter = mAdapterVisitor

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
    }

    private fun getFamilyDetails() {
        mFamilyId = intent.getStringExtra(FAMILY_ID) ?: ""
        if (mFamilyId.isNotEmpty()) KunLuHomeSdk.familyImpl.getFamilyDetails(mFamilyId, { code, msg -> toastErrorMsg(code, msg) }, { setFamilyData(it) })
    }

    companion object {
        const val FAMILY_ID = "family_id"
    }
}