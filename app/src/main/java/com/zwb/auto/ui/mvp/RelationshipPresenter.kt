package com.zwb.auto.ui.mvp

import android.util.Log
import com.zwb.auto.base.BasePresenter
import com.zwb.auto.bean.BaseResponse
import com.zwb.auto.bean.ConnectBean
import com.zwb.auto.bean.IndustryBean
import com.zwb.auto.net.RetrofitHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/27
 */
class RelationshipPresenter: BasePresenter<RelationshipContract.View>(),RelationshipContract.Presenter {


    override fun getIndustryList(isInitial: Boolean) {
        RetrofitHelper.getApiService()
            .getIndustryList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse<List<IndustryBean>>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseResponse<List<IndustryBean>>) {
                    if (t.code == 0){
                        mRootView?.onGetIndustryList(isInitial,t.data)
                    }
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    override fun getConnectList(typeId: String, sex: String) {

        val paramsMap = HashMap<String, String>()
        paramsMap["typeid"] = typeId
        paramsMap["sex"] = if (sex == "未知") "" else sex
        paramsMap["project_id"] = "100001"
        paramsMap["num"] = 20.toString()
        RetrofitHelper.getApiService()
            .getConnectList(paramsMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseResponse<List<ConnectBean>>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseResponse<List<ConnectBean>>) {
                    if (t.code == 0){
                        mRootView?.onGetConnectList(t.data)
                    }else{
                        mRootView?.onGetConnectList(null)
                    }
                }

                override fun onError(e: Throwable) {
                    mRootView?.onGetConnectList(null)
                }
            })
    }
}