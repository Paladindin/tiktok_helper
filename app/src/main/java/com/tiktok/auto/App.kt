package com.tiktok.auto

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.lzf.easyfloat.EasyFloat
import com.tencent.bugly.Bugly
import com.tiktok.auto.base.BaseActivity
import com.tiktok.auto.bean.FunctionType
import com.tiktok.auto.bean.OperationDetail
import com.tiktok.auto.config.Constants
import com.tiktok.auto.push.MsgService
import com.tiktok.auto.ui.service.AccessService
import com.tiktok.auto.utils.CommandUtils
import com.tuolu.subtitle.bean.UserManager

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/12
 */
class App : Application() {

    private var currentActivity: Activity? = null

    private var service: AccessService? = null

    private var startRun: Boolean = false

    private var mFunctionType: FunctionType? = null

    private var mOperationType: OperationDetail.TYPE? = null

    private var mOperationTarget: OperationDetail.TYPE? = null

    private var mOperationCount: Int = 1

    private var mNavBarHeight: Int = 0

    private var isAuth: Boolean = UserManager.instance.getAuth()

    companion object {
        private lateinit var instance: App

        @JvmStatic
        fun getInstance(): App {
            return instance
        }
    }

    fun setNavBarHeight(height: Int) {
        mNavBarHeight = height
    }

    fun getNavBarHeight(): Int {
        return mNavBarHeight
    }

    fun setAuth(auth: Boolean) {
        isAuth = auth
        UserManager.instance.setAuth(auth)
    }

    fun getAuth(): Boolean {
        return isAuth
    }

    fun cancelCommand() {
        CommandUtils.cancelJob()
    }

    fun doCommand() {
        if (service != null) {
            CommandUtils.doCommand(mFunctionType, mOperationType, mOperationTarget, mOperationCount)
        }
    }

    private fun initGlobeActivity() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(param1Activity: Activity, param1Bundle: Bundle?) {
                currentActivity = param1Activity
            }

            override fun onActivityDestroyed(param1Activity: Activity) {
            }

            override fun onActivityPaused(param1Activity: Activity) {
            }

            override fun onActivityResumed(param1Activity: Activity) {
                currentActivity = param1Activity
            }

            override fun onActivitySaveInstanceState(
                param1Activity: Activity,
                param1Bundle: Bundle
            ) {
            }

            override fun onActivityStarted(param1Activity: Activity) {
            }

            override fun onActivityStopped(param1Activity: Activity) {
            }
        })
    }

    fun getCurrentActivity(): BaseActivity? {
        val activity = this.currentActivity
        return if (activity is BaseActivity) activity else null
    }


    fun hideFloatWindows() {
        if (EasyFloat.appFloatIsShow()) {
            EasyFloat.hideAppFloat()
        }
    }

    fun holdApp(paramString: String) {
        showFloatWindows()
        startActivity(packageManager.getLaunchIntentForPackage(paramString))
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AccessService.get().init(this)
        initGlobeActivity()
        Bugly.init(this, Constants.BUGLY_APP_ID, true)
        MsgService.start(applicationContext)
    }

    override fun attachBaseContext(base: Context) {
        MultiDex.install(base)
        super.attachBaseContext(base)
    }

    fun setFunctionType(functionType: FunctionType) {
        mFunctionType = functionType
    }

    fun setOperationType(operationType: OperationDetail.TYPE?) {
        mOperationType = operationType
    }

    fun setOperationTarget(operationTarget: OperationDetail.TYPE?) {
        mOperationTarget = operationTarget
    }

    fun setOperationCount(operationCount: Int) {
        mOperationCount = operationCount
    }

    fun setService(paramAccessService: AccessService) {
        this.service = paramAccessService
        CommandUtils.init(paramAccessService)
    }

    fun getService(): AccessService? {
        return service
    }

    fun setStartRun(startRun: Boolean) {
        this.startRun = startRun
    }

    fun getStartRun(): Boolean {
        return startRun
    }

    fun showFloatWindows() {
        EasyFloat.showAppFloat()
    }
}