package com.tiktok.auto.ui.fragment

import android.view.View
import com.tuolu.subtitle.bean.UserManager
import com.tiktok.auto.ui.dialog.AuthDialog
import com.tiktok.auto.App
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseFragment
import com.tiktok.auto.push.MsgBean
import com.tiktok.auto.push.MsgConfig
import com.tiktok.auto.push.MsgEvent
import com.tiktok.auto.ui.activity.LoginActivity
import com.tiktok.auto.ui.activity.RegisterActivity
import com.tiktok.auto.ui.activity.WebviewActivity
import com.tiktok.auto.ui.dialog.AuthCodeDialog
import com.tiktok.auto.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_personal.*
import org.greenrobot.eventbus.EventBus

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class PersonalFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_personal
    }

    override fun initView() {

        tv_tortoise.setOnClickListener {
            WebviewActivity.launch(getContext2(), "抖音教程", "http://jc.xuerenwx.com/jiaocheng")
        }

        tv_version.text = AppUtils.getVersionCode(getContext2())
        updateAuthStatus()
        tv_go_auth.setOnClickListener {
            showAuthDialog()
        }
        tv_login.setOnClickListener {
            LoginActivity.launch(getContext2())
        }
        tv_register.setOnClickListener {
            RegisterActivity.launch(getContext2())
        }
        tv_control.setOnClickListener {
            val msgEvent = MsgEvent()
            msgEvent.setStatus(MsgEvent.STATUS_SENDING)
            val msgBean = MsgBean()
            msgBean.msg_type = MsgConfig.MSG_CONTENT_CONTRAL
            msgBean.from_id = "u${UserManager.instance.getId()}"
            msgBean.to_id = "u${UserManager.instance.getId()}"
            msgEvent.msg = msgBean
            EventBus.getDefault().postSticky(msgEvent)
        }
        tv_logout.setOnClickListener {
            UserManager.instance.logout()
            showLogButton()
            updateAuthStatus()
            updateLoginStatus()
            initAuthCodeView()
        }
    }

    private fun initAuthCodeView() {
        if (UserManager.instance.isRootUser()){
            tv_get_auth_code.visibility = View.VISIBLE
            tv_get_auth_code.setOnClickListener {
                AuthCodeDialog.newInstance()
                    .setShowBottom(false)
                    .setMargin(20)
                    .setDimAmout(0.7f)
                    .showAllowingStateLoss(fragmentManager)
            }
        }else{
            tv_get_auth_code.visibility = View.GONE
        }
    }

    private fun showLogButton() {
        if (UserManager.instance.isLogin()) {
            tv_login.visibility = View.GONE
            tv_register.visibility = View.GONE
            tv_logout.visibility = View.VISIBLE
            tv_control.visibility = View.VISIBLE
        } else {
            tv_login.visibility = View.VISIBLE
            tv_register.visibility = View.VISIBLE
            tv_logout.visibility = View.GONE
            tv_control.visibility = View.GONE
        }
    }

    private fun updateAuthStatus() {
        tv_auth.text = if (App.getInstance().getAuth()) "已授权" else "未授权"
        tv_go_auth.visibility = if (App.getInstance().getAuth()) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        updateAuthStatus()
        updateLoginStatus()
        showLogButton()
        initAuthCodeView()
    }

    private fun updateLoginStatus() {
        if (UserManager.instance.isLogin()){
            tv_login_status.text = UserManager.instance.getUser()?.username
        }else{
            tv_login_status.text = "未登录"
        }
    }

    private fun showAuthDialog() {
        val username = UserManager.instance.getUser()?.username ?: ""
        AuthDialog.newInstance(username)
            .setOnAuthListener(object : AuthDialog.OnAuthListener {
                override fun onAuthSuccess() {
                }
            })
            .setShowBottom(false)
            .setMargin(20)
            .setDimAmout(0.7f)
            .showAllowingStateLoss(fragmentManager)
    }


    override fun lazyLoad() {
    }

    companion object {
        fun newInstance(): PersonalFragment {
            return PersonalFragment()
        }
    }
}