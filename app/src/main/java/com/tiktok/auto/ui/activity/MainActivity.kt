package com.tiktok.auto.ui.activity

import android.os.Handler
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.widget.TextView
import com.gyf.immersionbar.ImmersionBar
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnInvokeView
import com.tuolu.subtitle.bean.UserManager
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import com.tiktok.auto.App
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseActivity
import com.tiktok.auto.base.BaseFragment
import com.tiktok.auto.bean.auth.CheckAuthBean
import com.tiktok.auto.net.RetrofitHelper
import com.tiktok.auto.push.MsgService
import com.tiktok.auto.ui.fragment.HomeFragment
import com.tiktok.auto.ui.fragment.PersonalFragment
import com.tiktok.auto.ui.fragment.RelationshipFragment
import com.tiktok.auto.utils.AppUtils
import com.tiktok.auto.utils.ToastUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private lateinit var mFragmentMap: SparseArray<BaseFragment>

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        initAppFloat()
        checkPermissions()
    }

    private fun checkPermissions() {
        val hasPermissions = AndPermission.hasPermissions(this, Permission.READ_PHONE_STATE)
        if (hasPermissions) {
            checkAuth()
        } else {
            AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_PHONE_STATE)
                // 用户给权限了
                .onGranted { permissions ->
                    checkAuth()
                }
                // 用户拒绝权限，包括不再显示权限弹窗也在此列
                .onDenied { permissions ->
                    // 判断用户是不是不再显示权限弹窗了，若不再显示的话进入权限设置页
                    if (AndPermission.hasAlwaysDeniedPermission(this@MainActivity, permissions)) {
                        ToastUtils.showShort(applicationContext, "需要授予读取手机识别码权限,以获取设备授权状态")
                    }
                }
                .start()
        }
    }

    private fun checkAuth() {
        val imei = AppUtils.getIMEI(this)
        Log.e("----","imei $imei")
        RetrofitHelper.getAuthService()
            .checkAuthStatus(7, imei)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CheckAuthBean> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: CheckAuthBean) {
                    Log.e("----", "onNext ${t.status}")
                    if(t.status == 200) {
                        App.getInstance().setAuth(true)
                        UserManager.instance.setUserId(t.device?.userId ?: -1)
                        MsgService.subscribe()
                    }
                }

                override fun onError(e: Throwable) {
                    Log.e("----", "onError ${e.toString()}")
                }
            })
    }


    private fun initAppFloat() {

        EasyFloat.with(this)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setSidePattern(SidePattern.RESULT_SIDE)
            .setGravity(Gravity.CENTER)
            .setLayout(com.tiktok.auto.R.layout.float_window, OnInvokeView { floatView ->
                floatView.findViewById<TextView>(com.tiktok.auto.R.id.tv_back).setOnClickListener {
                    floatView.findViewById<TextView>(com.tiktok.auto.R.id.tv_start)?.setText("开始")
                    Handler().postDelayed({
                        EasyFloat.hideAppFloat()
                    }, 200)
                    App.getInstance().apply {
                        setStartRun(false)
                        cancelCommand()
                        Handler().postDelayed({
                            getCurrentActivity()?.moveTaskToFront()
                        }, 500)
                    }
                }
                floatView.findViewById<TextView>(com.tiktok.auto.R.id.tv_start).let { tvStart ->
                    tvStart.setOnClickListener {
                        App.getInstance().apply {
                            setStartRun(!getStartRun())
                            if (getStartRun()) {
                                tvStart.setText("结束")
                                doCommand()
                            } else {
                                tvStart.setText("开始")
                                cancelCommand()
                            }
                        }
                    }
                }
            })
            .registerCallback {
                createResult { isCreated, msg, view ->
                    if (isCreated) {
                        Handler().postDelayed({
                            //                            if (EasyFloat.appFloatIsShow())
                            EasyFloat.hideAppFloat()
                        }, 100)
                    }
                }
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        App.getInstance().setNavBarHeight(ImmersionBar.getNavigationBarHeight(this))
    }

    override fun initView() {
        mFragmentMap = SparseArray()
        showFragment(R.id.rb_home)
    }

    override fun initListener() {
        initRadioButton()
    }

    private fun initRadioButton() {
        rg_tab.setOnCheckedChangeListener { rg, idRes ->
            showFragment(idRes)
        }
    }

    private fun getFragment(index: Int): BaseFragment {
        return when (index) {
            R.id.rb_home -> HomeFragment.newInstance()
//            R.id.rb_assistant -> AssistantFragment.newInstance()
            R.id.rb_relationship -> RelationshipFragment.newInstance()
            R.id.rb_personal -> PersonalFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }


    private fun showFragment(idRes: Int) {
        var fragment = mFragmentMap.get(idRes)
        if (fragment == null) {
            fragment = getFragment(idRes)
            mFragmentMap.put(idRes, fragment)
        }
        mFragmentMap.takeIf { it.size() > 0 }?.let {
            for (i in 0 until mFragmentMap.size()) {
                val baseFragment = mFragmentMap.valueAt(i)
                if (baseFragment.isVisible) {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.hide(baseFragment).commit()
                }
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded()) { //  判断传入的fragment是否已经被add()过
            transaction.add(R.id.fl_fragment_container, fragment).show(fragment)
                .commit();
        } else {
            transaction.show(fragment).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EasyFloat.dismissAppFloat()
    }

}
