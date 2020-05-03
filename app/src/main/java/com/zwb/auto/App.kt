package com.zwb.auto

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.TextView
import com.lzf.easyfloat.EasyFloat
import com.yhao.floatwindow.FloatWindow
import com.zwb.auto.base.BaseActivity
import com.zwb.auto.bean.FunctionType
import com.zwb.auto.bean.OperationDetail
import com.zwb.auto.ui.service.AccessService
import com.zwb.auto.utils.CommandUtils

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

    private lateinit var tvBack: TextView

    private lateinit var tvStart: TextView

    companion object {
        private lateinit var instance: App

        @JvmStatic
        fun getInstance(): App {
            return instance
        }
    }

    private fun initFloatWindow() {
        if (FloatWindow.get() == null) {
            val view = LayoutInflater.from(this).inflate(R.layout.float_window, null)
            tvBack = view.findViewById(R.id.tv_back) as TextView
            tvStart = view.findViewById(R.id.tv_start) as TextView
            tvBack.setOnClickListener {
                setStartRun(false)
                cancelCommand()
                Handler().postDelayed({
                    resetStartUI()
                    getCurrentActivity()?.moveTaskToFront()
                }, 500)
            }
            tvStart.setOnClickListener {
                setStartRun(true)
                doCommand()
            }
//            FloatWindow.with(applicationContext)
//                .setView(view)
//                .setWidth(UiUtils.dp2px(this,120))
//                .setHeight(UiUtils.dp2px(this,60))
//                .setX(UiUtils.getScreenWidth(this) / 2)
//                .setY(Screen.height, 0.1f)
//                .setMoveType(MoveType.slide)
//                .setMoveStyle(500L, null)
//                .setPermissionListener(object : PermissionListener {
//                    override fun onFail() {
//                        Toast.makeText(
//                            applicationContext,
//                            "需要浮动窗权限",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    override fun onSuccess() {
//                        Toast.makeText(
//                            applicationContext,
//                            "获取到浮动窗权限",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }).setDesktopShow(true).build()
        }
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
//        if (FloatWindow.get().view.visibility == View.VISIBLE)
//            FloatWindow.get().view.visibility = View.INVISIBLE
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
        initFloatWindow()
        initGlobeActivity()
    }

    fun resetStartUI() {
        try {
            this.tvStart.text = "开始"
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

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
//        if (FloatWindow.get().view.visibility == View.INVISIBLE)
//            FloatWindow.get().view.visibility = View.VISIBLE
        EasyFloat.showAppFloat()
    }
}