package com.zwb.auto.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

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