package com.tiktok.auto.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/13
 */
open class BaseOperationBean : MultiItemEntity {

    companion object{
        val TYPE_COMMON = 0x100
        val TYPE_EDIT_TEXT = 0x200
    }

    override fun getItemType(): Int {
        return 0
    }

}