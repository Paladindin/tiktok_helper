package com.tiktok.auto.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tiktok.auto.R
import com.tiktok.auto.bean.BaseOperationBean
import com.tiktok.auto.bean.EditTextBean
import com.tiktok.auto.bean.OperationBean
import com.tiktok.auto.bean.OperationDetail

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class OperationDetailAdapter(data: List<BaseOperationBean>) :
    BaseMultiItemQuickAdapter<BaseOperationBean, BaseViewHolder>(data) {

    init {
        addItemType(BaseOperationBean.TYPE_COMMON,R.layout.item_operation_detail)
        addItemType(BaseOperationBean.TYPE_EDIT_TEXT,R.layout.item_operation_edit)
    }


    override fun convert(helper: BaseViewHolder, item: BaseOperationBean) {
        when(helper.itemViewType){
            BaseOperationBean.TYPE_COMMON ->{
                if (item is OperationBean){
                    val operationDetail = item.operationDetail
                    helper.setText(R.id.tv_title, operationDetail.title())
                    helper.setText(R.id.tv_area, item.selectType.desc())
                }
            }
            BaseOperationBean.TYPE_EDIT_TEXT ->{
                if (item is EditTextBean){
                    val editText = helper.getView<EditText>(R.id.et_count)
                    editText.addTextChangedListener(object : TextWatcher{
                        override fun afterTextChanged(s: Editable?) {
                            val num = s?.toString()?.toIntOrNull() ?: 1
                            item.operateCount = num
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                    })
                    if (item.operateCount > 0){
                        editText.setText("${item.operateCount}")
                    }else{
                        editText.hint = item.hint
                    }

                }
            }
        }

    }

    fun setSelectType(selectedType: OperationDetail.TYPE,position: Int) {
        if (position < 0 || position >= data.size){
            return
        }
        val baseOperationBean = data[position]
        if (baseOperationBean is OperationBean) {
            baseOperationBean.selectType = selectedType
            notifyItemChanged(position)
        }
    }
}