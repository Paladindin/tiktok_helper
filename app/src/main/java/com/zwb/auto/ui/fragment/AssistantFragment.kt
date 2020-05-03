package com.zwb.auto.ui.fragment

import com.zwb.auto.R
import com.zwb.auto.base.BaseFragment

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class AssistantFragment: BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_assistant
    }

    override fun initView() {
    }

    override fun lazyLoad() {
    }

    companion object {
        fun newInstance(): AssistantFragment {
            return AssistantFragment()
        }
    }
}