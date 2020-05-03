package com.zwb.auto.ui.fragment

import com.zwb.auto.R
import com.zwb.auto.base.BaseFragment

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
    }

    override fun lazyLoad() {
    }

    companion object {
        fun newInstance(): PersonalFragment {
            return PersonalFragment()
        }
    }
}