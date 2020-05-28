package com.tiktok.auto.base

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.tiktok.auto.App
import com.tiktok.auto.R


/**
 * desc:BaseActivity基类
 */
abstract class BaseActivity : AppCompatActivity() {
    @Nullable
    protected val TAG = javaClass.name

    protected var mImmersionBar: ImmersionBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            fixOrientation()
        }
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        setStatusBar()
        initView()
        initData()
        initListener()
    }

    private fun setStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
            .statusBarColor(if (isDarkActionBar()) R.color.color_title_bar else R.color.color_white)
            .fitsSystemWindows(true)
            .statusBarDarkFont(!isDarkActionBar(), 0.2f).apply {
                init()
            }
    }


    private fun fixOrientation(): Boolean {
        try {
            val field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o = field.get(this) as ActivityInfo
            o.screenOrientation = SCREEN_ORIENTATION_UNSPECIFIED
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes =
                Class.forName("com.android.internal.R\$styleable").getField("Window").get(null) as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m = ActivityInfo::class.java.getMethod(
                "isTranslucentOrFloating",
                TypedArray::class.java
            )
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    /**
     *  设置点击事件
     */
    abstract fun initListener()

    /**
     *  加载布局
     */
    abstract fun layoutId(): Int

    /**
     *  是否是黑色Acticonbar
     */
    open fun isDarkActionBar(): Boolean {
        return true
    }

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化 View
     */
    abstract fun initView()

    private fun getRunningAppProcessInfo(paramString: String): ActivityManager.RunningAppProcessInfo? {
        for (runningAppProcessInfo in (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses) {
            if (runningAppProcessInfo.processName == paramString)
                return runningAppProcessInfo
        }
        return null
    }

    private fun isForeground(paramString: String): Boolean {
        val runningAppProcessInfo = getRunningAppProcessInfo(paramString)
        val bool2 = false
        var bool1 = bool2
        if (runningAppProcessInfo != null) {
            bool1 = bool2
            if (ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == runningAppProcessInfo.importance)
                bool1 = true
        }
        return bool1
    }

    fun moveTaskToFront(): Boolean {
        (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).moveTaskToFront(taskId, 0)
        return isForeground(packageName)
    }

    override fun onResume() {
        super.onResume()
        App.getInstance().hideFloatWindows()
    }

}


