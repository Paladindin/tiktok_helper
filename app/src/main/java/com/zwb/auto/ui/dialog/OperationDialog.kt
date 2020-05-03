package com.zwb.auto.ui.dialog

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zwb.auto.R
import com.zwb.auto.base.BaseDialog
import com.zwb.auto.base.ViewHolder
import com.zwb.auto.bean.BaseOperationBean
import com.zwb.auto.bean.FunctionType
import com.zwb.auto.bean.OperationBean
import com.zwb.auto.bean.OperationDetail
import com.zwb.auto.ui.adapter.OperationDetailAdapter

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/10
 */
class OperationDialog : BaseDialog() {
    private lateinit var operationDetailAdapter: OperationDetailAdapter

    override fun setUpLayoutId(): Int {
        return R.layout.dialog_operation
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        functionType?.title()?.let {
            holder.setText(R.id.tv_title, it)
        }
        listOperation?.takeIf { !it.isNullOrEmpty() }?.let {
            initRvOperation(holder.getView<RecyclerView>(R.id.rv_operation), it)
        }
        holder.setOnClickListener(R.id.tv_confirm, View.OnClickListener {
            mListener?.onConfirm(operationDetailAdapter.data)
            dismiss()
        })
    }


    private fun initRvOperation(rvOperation: RecyclerView, listOperation: List<BaseOperationBean>) {
        rvOperation.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val decor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        decor.setDrawable(resources.getDrawable(R.drawable.shape_divider_transparent))
        rvOperation.addItemDecoration(decor)

        operationDetailAdapter = OperationDetailAdapter(listOperation)
        rvOperation.adapter = operationDetailAdapter
        operationDetailAdapter.setOnItemClickListener { adapter, view, position ->
            val operatonDetail = operationDetailAdapter.getItem(position)
            if (operatonDetail is OperationBean) {
                operatonDetail.let {
                    SelectTypeDialog.newInstance(it.operationDetail)
                        .setOnItemClickListener(object : SelectTypeDialog.OnItemClickListener {
                            override fun onItemSelect(type: OperationDetail.TYPE) {
                                operationDetailAdapter.setSelectType(type, position)
                            }
                        })
                        .setMargin(10)
                        .setDimAmout(0.6f)
                        .setShowBottom(true)
                        .showAllowingStateLoss(fragmentManager)
                }
            }
        }
    }

    override fun onKeyBack() {
        dismiss()
    }

    companion object {
        var functionType: FunctionType? = null
        var listOperation: List<BaseOperationBean>? = null
        fun newInstance(
            item: FunctionType,
            listOperation: List<BaseOperationBean>
        ): OperationDialog {
            this.listOperation = listOperation
            functionType = item
            return OperationDialog()
        }
    }

    private var mListener: OnConfirmClickListener? = null

    fun setOnConfirmClickListener(listener: OnConfirmClickListener): BaseDialog {
        mListener = listener
        return this
    }

    interface OnConfirmClickListener {
        fun onConfirm(listOperationBen: List<BaseOperationBean>)
    }
}