package com.tiktok.auto.ui.dialog

import android.view.View
import com.tencent.bugly.Bugly.applicationContext
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseDialog
import com.tiktok.auto.base.ViewHolder
import com.tiktok.auto.bean.auth.AuthCodeBean
import com.tiktok.auto.net.RetrofitHelper
import com.tiktok.auto.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_get_auth_code.*

/**
 * Description:获取授权码
 * Author: zwb
 * Date: 2020/3/10
 */
class AuthCodeDialog : BaseDialog() {

    override fun setUpLayoutId(): Int {
        return R.layout.dialog_get_auth_code
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        holder.setOnClickListener(R.id.tv_get_auth, View.OnClickListener {
            getAuthCode()
        })
    }

    private fun getAuthCode() {
        RetrofitHelper.getAuthService()
            .getAuthCode(5)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<AuthCodeBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: AuthCodeBean) {
                    if (t.status == 200) {
                        et_auth_code.setText(t.code.authCode + "")
                    } else {
                        ToastUtils.showShort(applicationContext, "获取激活码失败,${t.msg}")
                    }
                }

                override fun onError(e: Throwable) {
                    ToastUtils.showShort(applicationContext, "获取激活码失败")
                }
            })

    }

    override fun onKeyBack() {
        dismiss()
    }


    companion object {
        fun newInstance(): AuthCodeDialog {
            return AuthCodeDialog()
        }
    }
}