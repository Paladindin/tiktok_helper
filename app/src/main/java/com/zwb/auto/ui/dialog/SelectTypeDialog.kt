package com.zwb.auto.ui.dialog

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zwb.auto.R
import com.zwb.auto.base.BaseDialog
import com.zwb.auto.base.ViewHolder
import com.zwb.auto.bean.OperationDetail
import com.zwb.auto.ui.adapter.SelectTypeAdapter

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/10
 */
class SelectTypeDialog : BaseDialog() {

    override fun setUpLayoutId(): Int {
        return R.layout.dialog_select_type
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        operationDetail?.title()?.let {
            holder.setText(R.id.tv_title,it)
        }
        initRvOperation(holder.getView<RecyclerView>(R.id.rv_type))
    }

    private fun initRvOperation(rvType: RecyclerView) {
        rvType.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val decor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        decor.setDrawable(resources.getDrawable(R.drawable.shape_divider_1dp))
        rvType.addItemDecoration(decor)
        operationDetail?.type()?.let {
            val selectTypeAdapter = SelectTypeAdapter(it)
            rvType.adapter = selectTypeAdapter
            selectTypeAdapter.setOnItemClickListener { adapter, view, position ->
                selectTypeAdapter.getItem(position)?.let {
                    mListener?.onItemSelect(it)
                }
                dismiss()
            }
        }

    }

    override fun onKeyBack() {
        dismiss()
    }

    companion object {
        var operationDetail: OperationDetail? = null
        fun newInstance(operationDetail: OperationDetail): SelectTypeDialog {
            this.operationDetail = operationDetail
            return SelectTypeDialog()
        }
    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener): BaseDialog {
        mListener = listener
        return this
    }

    interface OnItemClickListener{
        fun onItemSelect(type: OperationDetail.TYPE)
    }
}