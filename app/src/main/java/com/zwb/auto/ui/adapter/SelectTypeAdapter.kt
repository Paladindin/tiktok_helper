package com.zwb.auto.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zwb.auto.R
import com.zwb.auto.bean.OperationDetail

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class SelectTypeAdapter(data: List<OperationDetail.TYPE>) :
    BaseQuickAdapter<OperationDetail.TYPE, BaseViewHolder>(R.layout.item_select_type,data) {

    override fun convert(helper: BaseViewHolder, item: OperationDetail.TYPE) {
        helper.setText(R.id.tv_type,item.desc())
    }
}