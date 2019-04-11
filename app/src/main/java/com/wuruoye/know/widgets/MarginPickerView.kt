package com.wuruoye.know.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.VelocityTracker
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import com.wuruoye.know.R

/**
 * Created at 2019/3/18 18:30 by wuruoye
 * Description:
 */
class MarginPickerView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mTop: Float = 0.toFloat()
    private var mBottom: Float = 0.toFloat()
    private var mLeft: Float = 0.toFloat()
    private var mRight: Float = 0.toFloat()

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mLineWidth: Int = 0
    private var mLineHeight: Int = 0
    private var mOffset: Int = 0

    private var mPaint: Paint? = null

    private var mCurType: Int = 0
    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()
    private val mVelocityTracker: VelocityTracker
    private val mAnimation: FlingAnimation

    private var mListener: OnMarginChangedListener? = null

    val margin: IntArray
        get() = intArrayOf(mLeft.toInt(), mTop.toInt(), mRight.toInt(), mBottom.toInt())

    init {
        initPaint()

        mVelocityTracker = VelocityTracker.obtain()
        mAnimation = FlingAnimation(FloatValueHolder())
        mAnimation.addUpdateListener { _
                                       , v, _ ->
            when (mCurType) {
                TYPE_TOP -> mTop = v
                TYPE_BOTTOM -> mBottom = v
                TYPE_LEFT -> mLeft = v
                TYPE_RIGHT -> mRight = v
            }
            marginChanged()
        }
    }

    fun setMargin(left: Int, top: Int, right: Int, bottom: Int) {
        this.mTop = top.toFloat()
        this.mBottom = bottom.toFloat()
        this.mLeft = left.toFloat()
        this.mRight = right.toFloat()
        marginChanged()
    }

    fun setOnMarginChangedListener(listener: OnMarginChangedListener) {
        mListener = listener
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        mPaint!!.isAntiAlias = true
        mPaint!!.alpha = 100
        mPaint!!.strokeWidth = 15f
        mPaint!!.strokeCap = Paint.Cap.ROUND
        mPaint!!.color = ActivityCompat.getColor(context, R.color.mountain_mist)
    }

    private fun initSize(w: Int, h: Int) {
        this.mWidth = w
        this.mHeight = h
        this.mLineWidth = w / 3
        this.mLineHeight = h / 3
        mOffset = 20
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // draw mTop
        var startX = mLineWidth.toFloat()
        var endX = (mLineWidth shl 1).toFloat()
        var startY = mLineHeight.toFloat() * mTop * 1f / MAX_TOP
        var endY = startY
        canvas.drawLine(startX, startY + mOffset, endX, endY + mOffset, mPaint!!)

        // draw mBottom
        startY = mHeight - mLineHeight.toFloat() * mBottom * 1f / MAX_TOP
        endY = startY
        canvas.drawLine(startX, startY - mOffset, endX, endY - mOffset, mPaint!!)

        // draw mLeft
        startX = mLineWidth.toFloat() * mLeft * 1f / MAX_LEFT
        endX = startX
        startY = mLineHeight.toFloat()
        endY = (mLineHeight shl 1).toFloat()
        canvas.drawLine(startX + mOffset, startY, endX + mOffset, endY, mPaint!!)

        // draw mRight
        startX = mWidth - mLineWidth.toFloat() * mRight * 1f / MAX_LEFT
        endX = startX
        canvas.drawLine(startX - mOffset, startY, endX - mOffset, endY, mPaint!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)
        mVelocityTracker.computeCurrentVelocity(1000)
        val velocityX = mVelocityTracker.xVelocity
        val velocityY = mVelocityTracker.yVelocity
        val x = event.x
        val y = event.y
        when (event.action) {
            ACTION_DOWN -> {
                mAnimation.cancel()
                mCurType = TYPE_NONE
                mStartX = x
                mStartY = y
            }
            ACTION_UP, ACTION_CANCEL -> {
                var startValue = 0f
                val minValue = 0f
                var maxValue = 0f
                var velocity = 0f
                when (mCurType) {
                    TYPE_TOP -> {
                        startValue = mTop
                        maxValue = MAX_TOP.toFloat()
                        velocity = velocityY
                    }
                    TYPE_BOTTOM -> {
                        startValue = mBottom
                        maxValue = MAX_TOP.toFloat()
                        velocity = -velocityY
                    }
                    TYPE_LEFT -> {
                        startValue = mLeft
                        maxValue = MAX_LEFT.toFloat()
                        velocity = velocityX
                    }
                    TYPE_RIGHT -> {
                        startValue = mRight
                        maxValue = MAX_LEFT.toFloat()
                        velocity = -velocityX
                    }
                }
                mAnimation.setMinValue(minValue)
                        .setMaxValue(maxValue)
                        .setStartValue(startValue)
                        .setStartVelocity(velocity / 10)
                        .start()
            }
            ACTION_MOVE -> {
                if (mCurType == TYPE_NONE) {
                    if (Math.abs(velocityX) > Math.abs(velocityY)) {
                        if (mStartX > mWidth / 2)
                            mCurType = TYPE_RIGHT
                        else
                            mCurType = TYPE_LEFT
                    } else {
                        if (mStartY > mHeight / 2)
                            mCurType = TYPE_BOTTOM
                        else
                            mCurType = TYPE_TOP
                    }
                }
                val distance = (if (mCurType > 2) velocityX else velocityY) * MIN_TIME
                when (mCurType) {
                    TYPE_TOP -> mTop = addWithLimit(mTop, distance, MAX_TOP)
                    TYPE_BOTTOM -> mBottom = addWithLimit(mBottom, -distance, MAX_TOP)
                    TYPE_LEFT -> mLeft = addWithLimit(mLeft, distance, MAX_LEFT)
                    TYPE_RIGHT -> mRight = addWithLimit(mRight, -distance, MAX_LEFT)
                }
                marginChanged()
            }
        }
        super.onTouchEvent(event)
        return true
    }

    private fun addWithLimit(distance: Float, offset: Float, max: Int): Float {
        val result = distance + offset
        if (result > max) return max.toFloat()
        return if (result < 0) 0f else result
    }

    private fun marginChanged() {
        if (mListener != null) {
            mListener!!.onMarginChanged(mLeft.toInt(), mTop.toInt(), mRight.toInt(), mBottom.toInt())
        }
        postInvalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initSize(w, h)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun recycler() {
        mVelocityTracker.recycle()
        mAnimation.cancel()
        mListener = null
    }

    interface OnMarginChangedListener {
        fun onMarginChanged(left: Int, top: Int, right: Int, bottom: Int)
    }

    companion object {
        var MAX_TOP = 60
        var MAX_LEFT = 50
        val TYPE_NONE = 0
        val TYPE_TOP = 1
        val TYPE_BOTTOM = 2
        val TYPE_LEFT = 3
        val TYPE_RIGHT = 4
        val MIN_TIME = 0.001f
    }
}
