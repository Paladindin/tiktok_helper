package com.tiktok.auto.ui.dialog

import android.util.Log
import android.view.View
import android.widget.EditText
import com.tencent.bugly.Bugly.applicationContext
import com.tuolu.subtitle.bean.UserManager
import com.tiktok.auto.App
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseDialog
import com.tiktok.auto.base.ViewHolder
import com.tiktok.auto.bean.auth.BaseAuthBean
import com.tiktok.auto.bean.auth.BindBean
import com.tiktok.auto.net.RetrofitHelper
import com.tiktok.auto.push.MsgService
import com.tiktok.auto.utils.AppUtils
import com.tiktok.auto.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Description:
 * Author: zwb
 * Date: 2020/3/10
 */
class AuthDialog : BaseDialog() {

    override fun setUpLayoutId(): Int {
        return R.layout.dialog_auth
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        val etAuth = holder.getView<EditText>(R.id.et_auth_code)
        val etUsername = holder.getView<EditText>(R.id.et_username)
        etUsername.setText(mUserName)
        holder.setOnClickListener(R.id.tv_auth, View.OnClickListener {
            val username = getEtText(etUsername)
            if (username.isEmpty()) {
                ToastUtils.showShort(applicationContext, "请输入用户名")
                return@OnClickListener
            }
            val authCode = getEtText(etAuth)
            if (authCode.isEmpty()) {
                ToastUtils.showShort(applicationContext, "请输入授权码")
                return@OnClickListener
            }
            activate(username, authCode)
        })
    }

    private fun activate(username: String, authCode: String) {
        RetrofitHelper.getAuthService()
            .activate(3, username, authCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseAuthBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseAuthBean) {
                    if (t.status == 200) {
                        auth(username, authCode)
                    } else {
                        ToastUtils.showShort(applicationContext, "激活失败,${t.msg}")
                    }
                }

                override fun onError(e: Throwable) {
                    ToastUtils.showShort(applicationContext, "激活失败,请检查授权码是否正确")
                }
            })

    }

    private fun auth(username: String, authCode: String) {
        RetrofitHelper.getAuthService()
            .bind(4, username, authCode, AppUtils.getIMEI(App.getInstance()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BindBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BindBean) {
                    Log.e("----", "onNext ${t.status}")
                    if (t.status == 200) {
                        ToastUtils.showShort(applicationContext, "授权成功!")
                        onAuthListener?.onAuthSuccess()
                        UserManager.instance.setUserId(t.device?.userId?:-1)
                        MsgService.subscribe()
                        App.getInstance().setAuth(true)
                        dismiss()
                    } else {
                        ToastUtils.showShort(applicationContext, "授权失败,${t.msg}")
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e("----", "onError ${e.toString()}")
                    ToastUtils.showShort(applicationContext, "授权失败,请检查用户名和授权码是否正确!")
                }
            })
    }

    fun getEtText(et: EditText): String {
        return et.text.trim().toString()
    }

    override fun onKeyBack() {
        dismiss()
    }


    companion object {
        private var mUserName: String = ""
        fun newInstance(username: String = ""): AuthDialog {
            mUserName = username
            return AuthDialog()
        }
    }

    private var onAuthListener: OnAuthListener? = null

    fun setOnAuthListener(onAuthListener: OnAuthListener):BaseDialog{
        this.onAuthListener = onAuthListener
        return this
    }

    interface OnAuthListener {
        fun onAuthSuccess()
    }
}