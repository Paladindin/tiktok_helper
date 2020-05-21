package com.zwb.auto.ui.fragment

import com.zwb.auto.R
import com.zwb.auto.base.BaseFragment
import com.zwb.auto.ui.activity.WebviewActivity
import kotlinx.android.synthetic.main.fragment_personal.*

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class PersonalFragment: BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_personal
    }

    override fun initView() {

        tv_tortoise.setOnClickListener {
            WebviewActivity.launch(getContext2(),"抖音教程","http://jc.xuerenwx.com/jiaocheng")
        }
    }

    override fun lazyLoad() {
    }

    companion object {
        fun newInstance(): PersonalFragment {
            return PersonalFragment()
        }
    }
}