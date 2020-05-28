package com.tiktok.auto.ui.mvp

import com.tiktok.auto.base.IBaseView
import com.tiktok.auto.base.IPresenter
import com.tiktok.auto.bean.ConnectBean
import com.tiktok.auto.bean.IndustryBean

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/27
 */
interface RelationshipContract {

    interface View: IBaseView{
        fun onGetIndustryList(isInitial: Boolean,industryList: List<IndustryBean>)

        fun onGetConnectList(industryList: List<ConnectBean>?)
    }
    interface Presenter: IPresenter<View>{
        fun getIndustryList(isInitial: Boolean)

        fun getConnectList(typeId: String,sex: String)
    }
}