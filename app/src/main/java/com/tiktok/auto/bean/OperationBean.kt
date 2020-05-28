package com.tiktok.auto.bean

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/11
 */
data class OperationBean(
    var operationDetail: OperationDetail, var selectType: OperationDetail.TYPE
) : BaseOperationBean() {

    override fun getItemType(): Int {
        return TYPE_COMMON
    }

}