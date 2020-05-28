package com.tiktok.auto.utils

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object UiUtils {

    /**
     * 打开软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context?) {
        val imm = mContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    @JvmStatic
    fun closeKeyBord(mEditText: EditText, mContext: Context?) {
        val imm = mContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    @JvmStatic
    fun dp2px(context: Context? , dipValue: Int): Int {
        val scale = context?.resources?.displayMetrics?.density?:0f
        return (dipValue * scale + 0.5f).toInt()
    }

    fun sp2px(context: Context? , spValue: Int): Int {
        val fontScale = context?.resources?.displayMetrics?.scaledDensity?:0f
        return (spValue * fontScale + 0.5f).toInt()
    }

    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val metric = DisplayMetrics()
        val wm = context.getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        return metric.widthPixels
    }

    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val metric = DisplayMetrics()
        val wm =context.getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        return metric.heightPixels
    }

    @JvmStatic
    fun getScreenHeight2(context: Context): Int {
        val metric = DisplayMetrics()
        val wm =context.getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getRealMetrics(metric)
        return metric.heightPixels
    }
}