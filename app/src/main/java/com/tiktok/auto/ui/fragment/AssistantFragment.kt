package com.tiktok.auto.ui.fragment

import com.tiktok.auto.R
import com.tiktok.auto.base.BaseFragment

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