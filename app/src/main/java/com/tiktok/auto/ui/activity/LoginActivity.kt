package com.tiktok.auto.ui.activity

import android.content.Context
import android.content.Intent
import android.widget.EditText
import com.tencent.bugly.Bugly
import com.tuolu.subtitle.bean.UserManager
import com.tiktok.auto.ui.dialog.AuthDialog
import com.tiktok.auto.App
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseActivity
import com.tiktok.auto.bean.auth.SignInOrUpBean
import com.tiktok.auto.net.RetrofitHelper
import com.tiktok.auto.push.MsgService
import com.tiktok.auto.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_login

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }

        tv_login.setOnClickListener {
            val username = getEtText(et_usename)
            if (username.isEmpty()) {
                ToastUtils.showShort(applicationContext, "请输入账号")
                return@setOnClickListener
            }
            val pwd = getEtText(et_pwd)
            if (pwd.isEmpty()) {
                ToastUtils.showShort(applicationContext, "请输入密码")
                return@setOnClickListener
            }
            login(username, pwd)
        }
    }

    private fun login(username: String, pwd: String) {
        val authService = RetrofitHelper.getAuthService()
        authService
            .signInOrUp(0, username, pwd)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SignInOrUpBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: SignInOrUpBean) {
                    if (t.status == 200) {
                        if (t.user?.authCode.isNullOrEmpty()) {
                            ToastUtils.showShort(Bugly.applicationContext, "该账号未授权,前往授权!")
                            //未授权
                            AuthDialog.newInstance(t.user.username)
                                .setOnAuthListener(object : AuthDialog.OnAuthListener {
                                    override fun onAuthSuccess() {
                                        UserManager.instance.setUserId(t.user?.id?:-1)
                                        finish()
                                    }
                                })
                                .setShowBottom(false)
                                .setMargin(20)
                                .setDimAmout(0.7f)
                                .showAllowingStateLoss(supportFragmentManager)
                        } else {
                            //已授权
                            ToastUtils.showShort(applicationContext, "登录成功,该账号已授权!")
                            App.getInstance().setAuth(true)
                            UserManager.instance.setUser(t.user)
                            UserManager.instance.setUserId(t.user?.id?:-1)
                            MsgService.subscribe()
                            finish()
                        }
                    } else {
                        ToastUtils.showShort(applicationContext, "登录失败,请检查账号密码是否正确!")
                    }
                }

                override fun onError(e: Throwable) {
                    ToastUtils.showShort(applicationContext, "登录失败,请检查账号密码是否正确!")
                }
            })
    }

    fun getEtText(et: EditText): String {
        return et.text.trim().toString()
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

}
