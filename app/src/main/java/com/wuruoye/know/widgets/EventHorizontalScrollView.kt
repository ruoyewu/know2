package com.wuruoye.know.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView

/**
 * Created at 2019/4/13 16:10 by wuruoye
 * Description:
 */
class EventHorizontalScrollView : HorizontalScrollView {
    var isScrollEnable: Boolean = true

    private var onEventListener: OnEventListener? = null
    private var onScrollChangedListener: OnScrollChangedListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) onEventListener?.onEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isScrollEnable && super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isScrollEnable && super.onTouchEvent(ev)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollChangedListener?.onChanged(oldl, oldt, l, t)
    }

    fun setOnEventListener(listener: OnEventListener) {
        this.onEventListener = listener
    }

    fun setOnEventListener(action: (MotionEvent) -> Unit) {
        onEventListener = object : OnEventListener {
            override fun onEvent(event: MotionEvent) {
                action(event)
            }
        }
    }

    fun setOnScrollChangedListener(action: (Int, Int, Int, Int) -> Unit) {
        onScrollChangedListener = object  : OnScrollChangedListener {
            override fun onChanged(oldX: Int, oldY: Int, newX: Int, newY: Int) {
                action(oldX, oldY, newX, newY)
            }
        }
    }

    interface OnEventListener {
        fun onEvent(event: MotionEvent)
    }

    interface OnScrollChangedListener {
        fun onChanged(oldX: Int, oldY: Int, newX: Int, newY: Int)
    }
}