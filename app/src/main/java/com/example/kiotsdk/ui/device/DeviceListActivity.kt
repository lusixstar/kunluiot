package com.example.kiotsdk.ui.device

import android.os.Bundle
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.kiotsdk.adapter.device.DeviceProductListAdapter
import com.example.kiotsdk.adapter.device.DeviceTabListAdapter
import com.example.kiotsdk.base.BaseActivity
import com.example.kiotsdk.databinding.ActivityDeviceListBinding
import com.example.kiotsdk.util.DemoUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kunluiot.sdk.KunLuHomeSdk
import com.kunluiot.sdk.bean.device.DeviceListProductBean
import com.kunluiot.sdk.bean.device.DeviceProductTabBean
import com.kunluiot.sdk.callback.device.IDeviceListProductCallback
import com.kunluiot.sdk.util.JsonUtils
import com.kunluiot.sdk.util.SPUtil
import org.jetbrains.anko.toast


class DeviceListActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDeviceListBinding

    private lateinit var mProductAdapter: DeviceProductListAdapter

    private lateinit var mTabAdapter: DeviceTabListAdapter

    private var mNetType = ""
    private var mApModel = false

    private var mAllProductList: List<DeviceListProductBean> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityDeviceListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolBar)
        mBinding.toolBar.setNavigationOnClickListener { onBackPressed() }

        intent?.let {
            mNetType = it.getStringExtra(NET_TYPE) ?: ""
            mApModel = it.getBooleanExtra(NET_TYPE_AP, false)
        }

        initAdapter()
        getDeviceList()
    }

    private fun getDeviceList() {
//        val str = SPUtil.get(this, SPUtil.DEVICE_LIST, "") as String
//        if (str.isNotEmpty()) {
//            val list: List<DeviceListProductBean> = Gson().fromJson(str, object : TypeToken<List<DeviceListProductBean>>() {}.type)
//            if (!list.isNullOrEmpty()) {
//                mAllProductList = list
//                setListData(list)
//            }
//        }
        KunLuHomeSdk.deviceImpl.getDeviceProducts(true, listCallback)
    }

    private fun initAdapter() {
        mTabAdapter = DeviceTabListAdapter(arrayListOf())
        (mBinding.listTab.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.listTab.adapter = mTabAdapter
        mTabAdapter.setOnItemClickListener { adapter, _, position ->
            val tabBean = adapter.data[position] as DeviceProductTabBean
            mTabAdapter.selectPosition(position)
            if (mAllProductList.isNotEmpty()) {
                val parentList = mAllProductList.filter { it.category.parent.id == tabBean.id }
                val list = mutableListOf<DeviceListProductBean>()
                parentList.forEach { item ->
                    val l = item.products.filter { it.bindType == mNetType }
                    item.products = l
                    if (!l.isNullOrEmpty()) {
                        list.add(item)
                    }
                }
                mProductAdapter.data.clear()
                mProductAdapter.addData(list)
            }
        }

        mProductAdapter = DeviceProductListAdapter(arrayListOf())
        mProductAdapter.setApType(mApModel)
        (mBinding.listItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.listItem.adapter = mProductAdapter
    }


    private fun setListData(bean: List<DeviceListProductBean> = arrayListOf()) {
        if (bean.isNullOrEmpty()) {
            toast("data get error, please once again")
            return
        }
        mAllProductList = bean
        SPUtil.apply(this, SPUtil.DEVICE_LIST, JsonUtils.toJson(bean))

        val tabList = mutableListOf<DeviceProductTabBean>()
        bean.forEachIndexed { index, item ->
            val info = item.category.parent
            info.categoryName = item.categoryParentName
            if (index == 0) info.select = true
            tabList.add(info)
        }
        if (mTabAdapter.data.isNullOrEmpty()) {
            mTabAdapter.data.clear()
            val list = DemoUtils.getTabSingle(tabList)
            mTabAdapter.addData(list)
        }

        if (bean.isNotEmpty() && mProductAdapter.data.isNullOrEmpty()) {
            val parentId = tabList.first().id
            val parentList = bean.filter { it.category.parent.id == parentId }
            val list = mutableListOf<DeviceListProductBean>()
            parentList.forEach { item ->
                val l = item.products.filter { it.bindType == mNetType }
                item.products = l
                if (!l.isNullOrEmpty()) {
                    list.add(item)
                }
            }
            mProductAdapter.data.clear()
            mProductAdapter.addData(list)
        }
    }

    private val listCallback = object : IDeviceListProductCallback {

        override fun onSuccess(bean: List<DeviceListProductBean>) {
            setListData(bean)
        }

        override fun onError(code: String, error: String) {
            toast("code == $code, error == $error")
        }
    }

    companion object {
        const val NET_TYPE = "type"
        const val NET_TYPE_WIFI = "WIFI"
        const val NET_TYPE_ZIG_BEE = "ZIG_BEE"
        const val NET_TYPE_AP = "type_ap"
    }
}