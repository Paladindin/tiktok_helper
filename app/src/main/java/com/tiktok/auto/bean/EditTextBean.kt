package com.tiktok.auto.bean

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/13
 */
data class EditTextBean(var title: String, var hint: String,var operateCount: Int = 0) : BaseOperationBean(){

    override fun getItemType(): Int {
        return TYPE_EDIT_TEXT
    }

}