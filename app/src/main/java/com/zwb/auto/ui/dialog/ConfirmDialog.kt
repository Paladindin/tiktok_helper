package com.video.subtitle.ui.dialog

import android.view.View
import com.zwb.auto.R
import com.zwb.auto.base.BaseDialog
import com.zwb.auto.base.ViewHolder
import com.zwb.auto.bean.*
import com.zwb.auto.ui.dialog.OperationDialog.Companion.functionType
import java.lang.StringBuilder

/**
 * Description:
 * Author: zwb
 * Date: 2020/3/10
 */
class ConfirmDialog : BaseDialog() {

    override fun setUpLayoutId(): Int {
        return R.layout.dialog_confirm
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        initTitle(holder)

        holder.setOnClickListener(R.id.tv_cancel, object : View.OnClickListener {
            override fun onClick(p0: View?) {
                dismiss()
                mListener?.onDisagree()
            }

        })
        holder.setOnClickListener(R.id.tv_confirm, object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var operationType: OperationDetail.TYPE? = null
                var operationTarget: OperationDetail.TYPE? = null
                var operationCount: Int = 1
                listOperationBean?.forEach {
                    if (it is OperationBean) {
                        when (it.selectType) {
                            OperationDetail.TYPE.STAR -> operationType = OperationDetail.TYPE.STAR
                            OperationDetail.TYPE.MESSAGE -> operationType = OperationDetail.TYPE.MESSAGE
                            OperationDetail.TYPE.STAR_AND_MESSAGE -> operationType = OperationDetail.TYPE.STAR_AND_MESSAGE

                            OperationDetail.TYPE.FANS_LIST -> operationTarget = OperationDetail.TYPE.FANS_LIST
                            OperationDetail.TYPE.STARS_LIST -> operationTarget = OperationDetail.TYPE.STARS_LIST

                            /**
                             * 评论专区的4种类型
                             */
                            OperationDetail.TYPE.RECOMMEND_VDIEO -> operationType = OperationDetail.TYPE.RECOMMEND_VDIEO
                            OperationDetail.TYPE.WORKS_OF_USER -> operationType = OperationDetail.TYPE.WORKS_OF_USER
                            OperationDetail.TYPE.LIVE_SQUARE -> operationType = OperationDetail.TYPE.LIVE_SQUARE
                            OperationDetail.TYPE.SAME_CITY_LIST ->  operationType = OperationDetail.TYPE.SAME_CITY_LIST

                            /**
                             * 好友列表发送的3种类型
                             */
                            OperationDetail.TYPE.WORD -> operationType = OperationDetail.TYPE.WORD
                            OperationDetail.TYPE.PICTURE -> operationType = OperationDetail.TYPE.PICTURE
                            OperationDetail.TYPE.WORD_AND_PICTURE -> operationType = OperationDetail.TYPE.WORD_AND_PICTURE

                            /**
                             * 点赞的4种类型
                             */
                            OperationDetail.TYPE.HOME_RECOMMEND_VDIEO -> operationType = OperationDetail.TYPE.HOME_RECOMMEND_VDIEO
                            OperationDetail.TYPE.PERSONAL_ALL_VIDEO -> operationType = OperationDetail.TYPE.PERSONAL_ALL_VIDEO
                            OperationDetail.TYPE.SAME_CITY_VIDEO -> operationType = OperationDetail.TYPE.SAME_CITY_VIDEO
                            OperationDetail.TYPE.CURRENT_WORK_COMMENTER ->  operationType = OperationDetail.TYPE.CURRENT_WORK_COMMENTER
                            OperationDetail.TYPE.LIVE ->  operationType = OperationDetail.TYPE.LIVE

                            /**
                             * 批量操作
                             */
                            OperationDetail.TYPE.BATCH_UNSTAR -> operationType = OperationDetail.TYPE.BATCH_UNSTAR
                            OperationDetail.TYPE.BATCH_STAR -> operationType = OperationDetail.TYPE.BATCH_STAR
                        }
                    }
                    if (it is EditTextBean) {
                        operationCount = it.operateCount
                    }
                }
                mListener?.onAgree(operationType, operationTarget,operationCount)
                dismiss()
            }

        })
    }

    private fun initTitle(holder: ViewHolder) {
        val sb = StringBuilder("先打开")
        var target = functionType?.target() ?: ""
        if (functionType == FunctionType.ASSIGN) {
            listOperationBean?.forEach {
                if (it is OperationBean) {
                    when (it.selectType) {
                        OperationDetail.TYPE.MINE -> target = it.selectType.desc()
                        OperationDetail.TYPE.SPECIAL_USER -> target = it.selectType.desc()
                        OperationDetail.TYPE.STARS_LIST -> target += it.selectType.desc()
                        OperationDetail.TYPE.FANS_LIST ->  target += it.selectType.desc()
                    }
                }
            }
        }else if (functionType == FunctionType.COMMENT_AREA){
            val operationBean = listOperationBean?.get(0)
            if (operationBean is OperationBean){
                target = operationBean.selectType.desc()
                when(operationBean.selectType){
                    OperationDetail.TYPE.SAME_CITY_LIST -> target += "的第一个视频"
                    OperationDetail.TYPE.WORKS_OF_USER -> target += "的第一个视频"
                }
            }
        }else if (functionType == FunctionType.PARISE){
            val operationBean = listOperationBean?.get(0)
            if (operationBean is OperationBean){
                target = operationBean.selectType.desc()
                when(operationBean.selectType){
                    OperationDetail.TYPE.PERSONAL_ALL_VIDEO -> target += "的第一个视频"
                    OperationDetail.TYPE.SAME_CITY_VIDEO -> target += "的第一个视频"
                }
            }
        }
        sb.append(target).append(",再点击开始按钮")
        holder.setText(R.id.tv_tip, sb.toString())
    }

    override fun onKeyBack() {
        dismiss()
    }

    companion object {
        var functionType: FunctionType? = null
        var listOperationBean: List<BaseOperationBean>? = null
        fun newInstance(item: FunctionType, content: List<BaseOperationBean>?): ConfirmDialog {
            functionType = item
            listOperationBean = content
            return ConfirmDialog()
        }
    }

    private var mListener: OnButtonClickListener? = null

    fun setOnButtonClickListener(listener: OnButtonClickListener): BaseDialog {
        mListener = listener
        return this
    }

    interface OnButtonClickListener {
        fun onAgree(
            operationType: OperationDetail.TYPE?,
            operationTarget: OperationDetail.TYPE?,
            operationCount: Int
        )

        fun onDisagree()
    }
}