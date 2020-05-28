package com.tiktok.auto.ui.adapter

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout.VERTICAL
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class SpaceDecoration(private var space: Int) : RecyclerView.ItemDecoration() {

    private val headerCount = -1
    private val footerCount = Integer.MAX_VALUE
    private var mPaddingEdgeSide = true
    private var mPaddingStart = true
    private var mPaddingHeaderFooter = false


    fun setPaddingEdgeSide(mPaddingEdgeSide: Boolean) {
        this.mPaddingEdgeSide = mPaddingEdgeSide
    }

    fun setPaddingStart(mPaddingStart: Boolean) {
        this.mPaddingStart = mPaddingStart
    }

    fun setPaddingHeaderFooter(mPaddingHeaderFooter: Boolean) {
        this.mPaddingHeaderFooter = mPaddingHeaderFooter
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        var spanCount = 0
        var orientation = 0
        var spanIndex = 0
        var headerCount = 0
        var footerCount = 0

        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager) {
            orientation = layoutManager.orientation
            spanCount = layoutManager.spanCount
            spanIndex = (view.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams).spanIndex
        } else if (layoutManager is GridLayoutManager) {
            orientation = layoutManager.orientation
            spanCount = layoutManager.spanCount
            spanIndex = (view.getLayoutParams() as GridLayoutManager.LayoutParams).spanIndex
        } else if (layoutManager is LinearLayoutManager) {
            orientation = layoutManager.orientation
            spanCount = 1
            spanIndex = 0
        }

        /**
         * 普通Item的尺寸
         */
        if (position >= headerCount && position < parent.adapter!!.itemCount - footerCount) {

            if (orientation == VERTICAL) {
                val expectedWidth = (parent.width - space * (spanCount + if (mPaddingEdgeSide) 1 else -1)).toFloat() / spanCount
                val originWidth = parent.width.toFloat() / spanCount
                val expectedX = (if (mPaddingEdgeSide) space else 0) + (expectedWidth + space) * spanIndex
                val originX = originWidth * spanIndex
                outRect.left = (expectedX - originX).toInt()
                outRect.right = (originWidth - outRect.left - expectedWidth).toInt()
                if (position - headerCount < spanCount && mPaddingStart) {
                    outRect.top = space
                }
                outRect.bottom = space
                return
            } else {
                val expectedHeight = (parent.height - space * (spanCount + if (mPaddingEdgeSide) 1 else -1)).toFloat() / spanCount
                val originHeight = parent.height.toFloat() / spanCount
                val expectedY = (if (mPaddingEdgeSide) space else 0) + (expectedHeight + space) * spanIndex
                val originY = originHeight * spanIndex
                outRect.bottom = (expectedY - originY).toInt()
                outRect.top = (originHeight - outRect.bottom - expectedHeight).toInt()
                if (position - headerCount < spanCount && mPaddingStart) {
                    outRect.left = space
                }
                outRect.right = space
                return
            }
        } else if (mPaddingHeaderFooter) {
            if (orientation == VERTICAL) {
                outRect.left = if (mPaddingEdgeSide) space else 0
                outRect.right = outRect.left
                outRect.top = if (mPaddingStart) space else 0
            } else {
                outRect.bottom = if (mPaddingEdgeSide) space else 0
                outRect.top = outRect.bottom
                outRect.left = if (mPaddingStart) space else 0
            }
        }

    }
}