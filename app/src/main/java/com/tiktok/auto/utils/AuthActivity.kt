package com.tiktok.auto.utils

import android.content.Context
import android.content.Intent
import android.widget.EditText
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_auth

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initListener() {
        tv_auth.setOnClickListener {
            if (getEtText(et_auth_code).isEmpty()){
                ToastUtils.showShort(applicationContext,"请输入授权码")
                return@setOnClickListener
            }
        }
    }

    fun getEtText(et: EditText): String{
        return et.text.trim().toString()
    }

    override fun onBackPressed() {
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AuthActivity::class.java)
            context.startActivity(intent)
        }
    }

}
