package com.tiktok.auto.base
import android.content.Context

/**
 * desc:
 */
interface IBaseView {

    fun showLoading(message:String)

    fun dismissLoading()

    fun getCurContext(): Context

}
