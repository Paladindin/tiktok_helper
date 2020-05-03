package com.zwb.auto.base

import android.widget.TextView
import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes


/**
 * Description: 各种编辑弹框的viewholder
 */
class ViewHolder private constructor(private val convertView: View) {
    private val views: SparseArray<View> = SparseArray()

    /**
     * 获取View
     *
     * @param viewId
     * @param <T>
     * @return
    </T> */
    fun <T : View> getView(@IdRes viewId: Int): T {
        var view: View? = views.get(viewId)
        if (view == null) {
            view = convertView.findViewById<T>(viewId)
            views.put(viewId, view)
        }
        return view as T
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    fun setText(viewId: Int, text: String) {
        val textView = getView<TextView>(viewId)
        textView.text = text
    }

    /**
     * 设置字体颜色
     *
     * @param viewId
     * @param colorId
     */
    fun setTextColor(viewId: Int, colorId: Int) {
        val textView = getView<TextView>(viewId)
        textView.setTextColor(colorId)
    }

    /**
     * 设置背景图片
     *
     * @param viewId
     * @param resId
     */
    fun setBackgroundResource(viewId: Int, resId: Int) {
        val view = getView<View>(viewId)
        view.setBackgroundResource(resId)
    }

    /**
     * 设置背景颜色
     *
     * @param viewId
     * @param colorId
     */
    fun setBackgroundColor(viewId: Int, colorId: Int) {
        val view = getView<View>(viewId)
        view.setBackgroundColor(colorId)
    }

    fun setVisible(viewId: Int, visible:Boolean) {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    fun setOnClickListener(viewId: Int, listener: View.OnClickListener) {
        val view = getView<View>(viewId)
        view.setOnClickListener(listener)
    }

    companion object {

        fun create(view: View): ViewHolder {
            return ViewHolder(view)
        }
    }
}