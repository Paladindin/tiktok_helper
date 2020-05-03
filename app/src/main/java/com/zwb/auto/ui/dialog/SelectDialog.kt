package com.video.subtitle.ui.dialog

import android.util.Log
import android.view.View
import com.weigan.loopview.LoopView
import com.zwb.auto.R
import com.zwb.auto.base.BaseDialog
import com.zwb.auto.base.ViewHolder

/**
 * Description:
 * Author: zwb
 * Date: 2020/3/10
 */
class SelectDialog : BaseDialog() {

    override fun setUpLayoutId(): Int {
        return R.layout.dialog_select
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        holder.setText(R.id.tv_title, if (type == TYPE_INDUSTRY) "选择行业" else "选择性别")
        val loopView = holder.getView<LoopView>(R.id.loopView)
        typeList?.takeIf { it.isNotEmpty() }?.let {
            loopView.apply {
                setNotLoop()
                setTextSize(15f)
                setItems(it)
                var index = it.indexOf(currentTag)
                if (index == -1) {
                    if (type == TYPE_INDUSTRY) index = 0 else index = 2
                }
                if (index in 0..it.lastIndex) {
                    setInitPosition(index)
                }

            }
        }
        holder.setOnClickListener(R.id.tv_cancel, View.OnClickListener {
            dismiss()
        })
        holder.setOnClickListener(R.id.tv_complete, View.OnClickListener {
            dismiss()
            val selectedItem = loopView.selectedItem
            Log.e("----", "selectedItem ${selectedItem}")
            typeList?.get(selectedItem)?.let {
                mListener?.onTagSeleted(type, it)
            }
        })
    }

    override fun onKeyBack() {
        dismiss()
    }

    companion object {
        const val TYPE_INDUSTRY = 0
        const val TYPE_SEX = 1
        private var type: Int = TYPE_INDUSTRY
        private var typeList: List<String>? = null
        private var currentTag: String = ""
        fun newInstance(type: Int, currentTag: String, typeList: List<String>?): SelectDialog {
            this.typeList = typeList
            this.type = type
            this.currentTag = currentTag
            return SelectDialog()
        }
    }


    private var mListener: OnOnSelectedListener? = null

    fun setOnSelectedListener(listener: OnOnSelectedListener): BaseDialog {
        mListener = listener
        return this
    }

    interface OnOnSelectedListener {
        fun onTagSeleted(type: Int, tag: String)
    }
}