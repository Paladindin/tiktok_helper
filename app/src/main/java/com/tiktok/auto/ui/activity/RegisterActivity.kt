package com.tiktok.auto.ui.activity

import android.content.Context
import android.content.Intent
import android.widget.EditText
import com.tiktok.auto.ui.dialog.AuthDialog
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseActivity
import com.tiktok.auto.bean.auth.SignInOrUpBean
import com.tiktok.auto.net.RetrofitHelper
import com.tiktok.auto.utils.ToastUtils
import com.tuolu.subtitle.bean.UserManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {


    override fun layoutId() = R.layout.activity_register

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }

        tv_register.setOnClickListener {
            val username = getEtText(et_username)
            if (username.isEmpty()){
                ToastUtils.showShort(applicationContext,"请输入账号")
                return@setOnClickListener
            }
            val pwd = getEtText(et_pwd)
            if (pwd.isEmpty()){
                ToastUtils.showShort(applicationContext,"请输入密码")
                return@setOnClickListener
            }
            register(username,pwd)
        }
    }

    private fun register(username: String, pwd: String) {
        val authService = RetrofitHelper.getAuthService()
        authService
            .signInOrUp(1,username,pwd)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SignInOrUpBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: SignInOrUpBean) {
                    if (t.status == 200){
                        ToastUtils.showShort(applicationContext,"注册成功,开始授权!")
                        t.user.username?.let {
                            AuthDialog.newInstance(it)
                                .setOnAuthListener(object: AuthDialog.OnAuthListener{
                                    override fun onAuthSuccess() {
                                        UserManager.instance.setUser(t.user)
                                        finish()
                                    }
                                })
                                .setShowBottom(false)
                                .setMargin(20)
                                .setDimAmout(0.7f)
                                .showAllowingStateLoss(supportFragmentManager)
                        }
                    }else{
                        ToastUtils.showShort(applicationContext,"注册失败,请重新设置!")
                    }
                }

                override fun onError(e: Throwable) {
                    ToastUtils.showShort(applicationContext,"注册失败,请重新设置!")
                }
            })
    }

    fun getEtText(et: EditText): String{
        return et.text.trim().toString()
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

}
