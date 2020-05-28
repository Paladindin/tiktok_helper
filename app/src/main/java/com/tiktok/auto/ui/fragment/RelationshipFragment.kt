package com.tiktok.auto.ui.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiktok.auto.ui.dialog.SelectDialog
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseFragment
import com.tiktok.auto.bean.ConnectBean
import com.tiktok.auto.bean.IndustryBean
import com.tiktok.auto.ui.adapter.ConnectAdapter
import com.tiktok.auto.ui.mvp.RelationshipContract
import com.tiktok.auto.ui.mvp.RelationshipPresenter
import kotlinx.android.synthetic.main.fragment_relationship.*

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class RelationshipFragment : BaseFragment(), RelationshipContract.View,
    SelectDialog.OnOnSelectedListener {

    private val mPresenter by lazy { RelationshipPresenter() }

    private var mAdapter: ConnectAdapter? = null

    private var mIndustryTitleList = ArrayList<String>()

    private var mIndustryList = ArrayList<IndustryBean>()

    private var mCurrentIndustryId: Int = 0

    private var mCurrentSex: String = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_relationship
    }

    override fun initView() {
        mPresenter.attachView(this)
        srl.setColorSchemeResources(R.color.color_blue)
        initRvConnect()
        initListener()
    }


    override fun lazyLoad() {
        mPresenter.getIndustryList(true)
    }


    private fun initRvConnect() {
        rv_connect.layoutManager = LinearLayoutManager(getContext2())
        mAdapter = ConnectAdapter().apply {
            rv_connect.adapter = this
            setOnItemClickListener { adapter, view, position ->
                selectItem(position)
            }
        }
    }

    private fun initListener() {
        srl.setOnRefreshListener {
            refresh(mCurrentIndustryId.toString(), mCurrentSex)
        }
        tv_select_all.setOnClickListener {
            mAdapter?.toggleSelectAll()
            tv_select_all.text = if (mAdapter?.isSelectAll() == true) "取消全选" else "全选"

        }
        tv_export.setOnClickListener {

        }
        tv_refresh.setOnClickListener {
            refresh(mCurrentIndustryId.toString(),mCurrentSex)
        }
        tv_select_industry.setOnClickListener {
            if (mIndustryTitleList.isNotEmpty()) {
                SelectDialog
                    .newInstance(
                        SelectDialog.TYPE_INDUSTRY,
                        tv_select_industry.text.toString(), mIndustryTitleList
                    )
                    .setOnSelectedListener(this)
                    .setDimAmout(0.6f)
                    .setShowBottom(true)
                    .show(fragmentManager)
            } else {
                mPresenter.getIndustryList(false)
            }

        }
        tv_select_sex.setOnClickListener {
            val sexList = ArrayList<String>()
            sexList.add("男")
            sexList.add("女")
            sexList.add("未知")
            SelectDialog
                .newInstance(SelectDialog.TYPE_SEX, tv_select_sex.text.toString(), sexList)
                .setOnSelectedListener(this)
                .setDimAmout(0.6f)
                .setShowBottom(true)
                .show(fragmentManager)
        }
    }

    private fun refresh(typeId: String, sex: String) {
        if (!srl.isRefreshing)
            srl.isRefreshing = true
        mPresenter.getConnectList(typeId, sex)
    }

    private fun stopRefrsh() {
        if (srl.isRefreshing) {
            srl.isRefreshing = false
        }
    }

    override fun onTagSeleted(type: Int, tag: String) {
        if (type == SelectDialog.TYPE_SEX) {
            mCurrentSex = tag
            tv_select_sex.text = tag
            refresh(mCurrentIndustryId.toString(), tag)
        } else {
            mCurrentIndustryId = getIndustryIdByTitle(tag)
            tv_select_industry.text = tag
            refresh(mCurrentIndustryId.toString(), mCurrentSex)
        }
    }

    private fun getIndustryIdByTitle(title: String): Int {
        var id = 0
        mIndustryList.forEach {
            if (title == it.title) {
                id = it.id
                return@forEach
            }
        }
        return id
    }

    override fun onGetIndustryList(isInitial: Boolean, industryList: List<IndustryBean>) {
        if (industryList.isEmpty())
            return
        mIndustryList.clear()
        mIndustryList.addAll(industryList)
        mIndustryTitleList.clear()
        industryList.forEach {
            if (it.title.isNotEmpty()) {
                mIndustryTitleList.add(it.title)
            }
        }
        if (!isInitial) {
            SelectDialog
                .newInstance(
                    SelectDialog.TYPE_INDUSTRY,
                    tv_select_industry.text.toString(),
                    mIndustryTitleList
                )
                .setOnSelectedListener(this)
                .setDimAmout(0.6f)
                .setShowBottom(true)
                .show(fragmentManager)
        } else {
            mCurrentIndustryId = industryList[0].id
            refresh(industryList[0].id.toString(), "")
        }
    }

    override fun onGetConnectList(industryList: List<ConnectBean>?) {
        stopRefrsh()
        if (!industryList.isNullOrEmpty()) {
            mAdapter?.setNewData(industryList)
        }
    }

    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }

    override fun getCurContext(): Context {
        return getContext2()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    companion object {
        fun newInstance(): RelationshipFragment {
            return RelationshipFragment()
        }
    }
}