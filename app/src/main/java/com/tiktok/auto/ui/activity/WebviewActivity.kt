package com.tiktok.auto.ui.activity

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseActivity
import com.tiktok.auto.config.Constants
import kotlinx.android.synthetic.main.activity_setting.iv_back
import kotlinx.android.synthetic.main.activity_setting.tv_title
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : BaseActivity() {

    private lateinit var mAgentWeb: AgentWeb

    override fun initListener() {
        iv_back.setOnClickListener { finish() }
    }

    override fun layoutId(): Int {
        return R.layout.activity_webview
    }

    override fun initData() {
    }

    override fun initView() {
        intent.getStringExtra(Constants.KEY_TITLE)?.apply {
            tv_title.text = this
        }
        val policyUrl = intent.getStringExtra(Constants.KEY_URL)
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                fl_container,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(-1, 3)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .go(policyUrl)
    }

    override fun onBackPressed() {
        if (!mAgentWeb.back()) {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle?.onDestroy()
        super.onDestroy()
    }

    companion object {

        fun launch(context: Context, title: String, url: String) {
            val intent = Intent(context, WebviewActivity::class.java)
            intent.putExtra(Constants.KEY_TITLE, title)
            intent.putExtra(Constants.KEY_URL, url)
            context.startActivity(intent)
        }
    }
}

