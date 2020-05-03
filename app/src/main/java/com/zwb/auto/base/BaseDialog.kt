package com.zwb.auto.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.zwb.auto.R

/**
 * Dialog基类
 */
abstract class BaseDialog : DialogFragment(){

    @LayoutRes
    protected var mLayoutResId: Int = 0

    private var mDimAmount = 0f//背景昏暗度
    private var mShowBottomEnable: Boolean = false//是否底部显示
    private var mMargin = 0//左右边距
    private var mAnimStyle = 0//进入退出动画
    private var mOutCancel = true//点击外部取消
    private var mContext: Context? = null
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var misFocusAble = false //外部是否可以响应事件
    private var mSoftInputMode:Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.base_dialog_style)
        mLayoutResId = setUpLayoutId()
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(mLayoutResId, container, false)
        convertView(ViewHolder.create(view), this)
        return view
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mOnDismissListener?.onDismiss(dialog)
    }

    private var mOnDismissListener: DialogInterface.OnDismissListener? = null

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        this.mOnDismissListener = listener
    }

    private fun initParams() {
        val window = dialog?.window
        if (window != null) {
            val params = window.attributes
            params.dimAmount = mDimAmount

            if (mSoftInputMode > -1){
                params.softInputMode = mSoftInputMode
            }

            //设置外部响应事件
            if (misFocusAble) {
                params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            }

            //设置dialog显示位置
            if (mShowBottomEnable) {
                params.gravity = Gravity.BOTTOM
            }else{
                params.gravity = Gravity.CENTER
            }

            //设置dialog宽度
            if (mWidth == 0 || mWidth == WindowManager.LayoutParams.MATCH_PARENT) {
                params.width = getScreenWidth(context) - 2 * dp2px(context, mMargin.toFloat())
            } else if (mWidth == WindowManager.LayoutParams.WRAP_CONTENT){
                params.width = WindowManager.LayoutParams.WRAP_CONTENT
            }else{
                params.width = dp2px(context, mWidth.toFloat())
            }

            //设置dialog高度
            if (mHeight == 0 || mWidth == WindowManager.LayoutParams.WRAP_CONTENT) {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else if (mHeight == WindowManager.LayoutParams.MATCH_PARENT){
                params.height = WindowManager.LayoutParams.MATCH_PARENT
            } else {
                params.height = dp2px(context, mHeight.toFloat())
            }

            //设置dialog动画
            if (mAnimStyle != 0) {
                window.setWindowAnimations(mAnimStyle)
            }

            window.attributes = params
        }
        dialog?.setCanceledOnTouchOutside(mOutCancel)
        dialog?.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN){
                onKeyBack()
                return@OnKeyListener true
            }
            false
        })
    }

    fun isShowing(): Boolean{
        return dialog?.isShowing?:false
    }

    /**
     * 设置背景昏暗度
     *
     * @param dimAmount
     * @return
     */
    fun setDimAmout(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float): BaseDialog {
        mDimAmount = dimAmount
        return this
    }

    /**
     * 是否显示底部
     *
     * @param showBottom
     * @return
     */
    fun setShowBottom(showBottom: Boolean): BaseDialog {
        mShowBottomEnable = showBottom
        return this
    }

    /**
     * 设置宽高
     *
     * @param width
     * @param height
     * @return
     */
    fun setSize(width: Int, height: Int): BaseDialog {
        mWidth = width
        mHeight = height
        return this
    }

    /**
     * 设置左右margin
     *
     * @param margin
     * @return
     */
    fun setMargin(margin: Int): BaseDialog {
        mMargin = margin
        return this
    }

    /**
     * 设置进入退出动画
     *
     * @param animStyle
     * @return
     */
    fun setAnimStyle(@StyleRes animStyle: Int): BaseDialog {
        mAnimStyle = animStyle
        return this
    }

    /**
     * 设置是否点击外部取消
     *
     * @param outCancel
     * @return
     */
    fun setOutCancel(outCancel: Boolean): BaseDialog {
        mOutCancel = outCancel
        return this
    }

    /**
     * 设置外部是否响应事件
     *
     * @param focusAble
     * @return
     */
    fun setFocusAble(focusAble: Boolean): BaseDialog {
        misFocusAble = focusAble
        return this
    }

    /**
     * 设置键盘模式
     *
     * @param softInputMode
     * @return
     */
    fun setSoftInputMode(softInputMode: Int): BaseDialog {
        mSoftInputMode = softInputMode
        return this
    }

    fun show(manager: FragmentManager?): BaseDialog {
        manager?.let {
            super.show(it, System.currentTimeMillis().toString())
        }
        return this
    }

    fun showAllowingStateLoss(manager: FragmentManager?): BaseDialog {
        val ft = manager?.beginTransaction()
        ft?.add(this,System.currentTimeMillis().toString())
        ft?.commitAllowingStateLoss()
        return this
    }

    fun dismissDelay(milSec:Long): BaseDialog {
        Handler().postDelayed({
            if (isShowing()){
                dismissAllowingStateLoss()
            }
        },milSec)
        return this
    }

    /**
     * 设置dialog布局
     *
     * @return
     */
    abstract fun setUpLayoutId(): Int

    /**
     * 操作dialog布局
     *
     * @param holder
     * @param dialog
     */
    abstract fun convertView(holder: ViewHolder, dialog: BaseDialog)

    /**
     * 点击物理返回键
     */
    abstract fun onKeyBack()

    private fun getScreenWidth(context: Context?): Int {
        val displayMetrics = context!!.getResources().getDisplayMetrics()
        return displayMetrics.widthPixels
    }

    private fun dp2px(context: Context?, dipValue: Float): Int {
        val scale = context!!.getResources().getDisplayMetrics().density
        return (dipValue * scale + 0.5f).toInt()
    }
}