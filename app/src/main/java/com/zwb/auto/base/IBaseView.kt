package com.zwb.auto.base
import android.app.Activity
import android.content.Context

/**
 * desc:
 */
interface IBaseView {

    fun showLoading(message:String)

    fun dismissLoading()

    fun getCurContext(): Context

}
