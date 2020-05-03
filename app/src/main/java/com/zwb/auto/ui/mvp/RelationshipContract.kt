package com.zwb.auto.ui.mvp

import com.zwb.auto.base.BaseView
import com.zwb.auto.base.IBaseView
import com.zwb.auto.base.IPresenter
import com.zwb.auto.bean.ConnectBean
import com.zwb.auto.bean.IndustryBean

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