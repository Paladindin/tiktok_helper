package com.zwb.auto.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zwb.auto.R
import com.zwb.auto.bean.ConnectBean
import com.zwb.auto.bean.OperationDetail

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class ConnectAdapter : BaseQuickAdapter<ConnectBean, BaseViewHolder>(R.layout.item_connect) {
    private var mSelectAll = false

    override fun convert(helper: BaseViewHolder, item: ConnectBean) {
        helper.setText(R.id.tv_id,"抖音号: ${item.phone}")
            .setText(R.id.tv_type,"类型: ${item.type_title}")
        helper.getView<ImageView>(R.id.iv_select).isSelected = item.checked
    }

    fun selectItem(position: Int) {
        data[position].checked = !data[position].checked
        notifyItemChanged(position)
    }

    fun toggleSelectAll() {
        mSelectAll = !mSelectAll
        data.forEach {
            it.checked = mSelectAll
        }
        notifyDataSetChanged()
    }

    fun getSelectedList(): List<String> {
        val phoneList = ArrayList<String>()
        data.forEach {
            if (it.checked) {
                phoneList.add(it.phone)
            }
        }
        return phoneList
    }

    fun isSelectAll() = mSelectAll
}