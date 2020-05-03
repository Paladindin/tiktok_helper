package com.zwb.auto.ui.adapter

import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zwb.auto.R
import com.zwb.auto.bean.FunctionType

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class FunctionTypeAdapter(data: List<FunctionType>) :
    BaseQuickAdapter<FunctionType, BaseViewHolder>(R.layout.item_operation_type,data) {

    override fun convert(helper: BaseViewHolder, item: FunctionType) {
        helper.setImageResource(R.id.iv_icon,item.drawableRes())
        helper.setText(R.id.tv_title,item.title())
        helper.setText(R.id.tv_desc,item.description())
        helper.getView<CardView>(R.id.card_view).setCardBackgroundColor(item.bgColor())
    }
}