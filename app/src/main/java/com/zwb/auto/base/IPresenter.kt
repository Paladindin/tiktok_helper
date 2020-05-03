package com.zwb.auto.base


/**
 * desc: Presenter 基类
 */


interface IPresenter<in V: IBaseView> {

    fun attachView(mRootView: V)

    fun detachView()

}
