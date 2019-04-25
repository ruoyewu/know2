package com.wuruoye.know.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.wuruoye.know.R

/**
 * Created by wuruoye on 2018/3/21.
 * this file is to
 */

class HeartBeatView : View {
    private var mColor: Int = 0
    private var mDuration: Int = 0
    private var mCount: Int = 0
    private var mPress: Float = 0.toFloat()
    private var mPiece: Float = 0.toFloat()
    private var mCurrent: Float = 0.toFloat()
    private var mStep: Float = 0.toFloat()

    private var mCenterX: Int = 0
    private var mCenterY: Int = 0

    private var mPaint: Paint? = null
    private val mData = FloatArray(8)
    private val mCtrl = FloatArray(16)
    private var mPath: Path? = null

    private var mDir = 1

    var color: Int
        get() = mColor
        set(color) {
            mColor = color
            mPaint!!.color = color
        }

    var press: Float
        get() = mPress
        set(press) {
            mPress = press
            initSize()
        }

    var count: Int
        get() = mCount
        set(count) {
            mCount = count
            initSize()
        }

    var duration: Int
        get() = mDuration
        set(duration) {
            mDuration = duration
            initSize()
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs == null) {
            initValue()
        } else {
            getAttr(attrs)
        }

        initSize()
        mPaint = Paint()
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        mPaint!!.isAntiAlias = true
        mPaint!!.color = mColor
        mPath = Path()
    }

    private fun initSize() {
        val radius = mCenterX.toFloat()
        val difference = radius * C

        mData[0] = 0f
        mData[1] = radius
        mData[2] = radius
        mData[3] = 0f
        mData[4] = 0f
        mData[5] = -radius
        mData[6] = -radius
        mData[7] = 0f

        mCtrl[0] = mData[0] + difference
        mCtrl[1] = mData[1]
        mCtrl[2] = mData[2]
        mCtrl[3] = mData[3] + difference
        mCtrl[4] = mData[2]
        mCtrl[5] = mData[3] - difference
        mCtrl[6] = mData[4] + difference
        mCtrl[7] = mData[5]
        mCtrl[8] = mData[4] - difference
        mCtrl[9] = mData[5]
        mCtrl[10] = mData[6]
        mCtrl[11] = mData[7] - difference
        mCtrl[12] = mData[6]
        mCtrl[13] = mData[7] + difference
        mCtrl[14] = mData[0] - difference
        mCtrl[15] = mData[1]

        mPiece = (mDuration / mCount).toFloat()
        mStep = mCenterX / mPress
        mCurrent = 0f
        mDir = 1
    }

    private fun initValue() {
        mColor = Color.BLACK
        mDuration = 1000
        mCount = 30
        mPress = 9f
    }

    private fun getAttr(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.HeartBeatView)
        mColor = array.getColor(R.styleable.HeartBeatView_heartColor, Color.BLACK)
        mDuration = array.getInt(R.styleable.HeartBeatView_duration, 1000)
        mCount = array.getInt(R.styleable.HeartBeatView_count, 30)
        mPress = array.getFloat(R.styleable.HeartBeatView_press, 9f)
        array.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(mCenterY.toFloat(), mCenterX.toFloat())
        canvas.scale(1f, -1f)

        mPath!!.reset()
        mPath!!.moveTo(mData[0], mData[1])

        mPath!!.cubicTo(mCtrl[0], mCtrl[1], mCtrl[2], mCtrl[3], mData[2], mData[3])
        mPath!!.cubicTo(mCtrl[4], mCtrl[5], mCtrl[6], mCtrl[7], mData[4], mData[5])
        mPath!!.cubicTo(mCtrl[8], mCtrl[9], mCtrl[10], mCtrl[11], mData[6], mData[7])
        mPath!!.cubicTo(mCtrl[12], mCtrl[13], mCtrl[14], mCtrl[15], mData[0], mData[1])

        canvas.drawPath(mPath!!, mPaint!!)

        mCurrent += mPiece * mDir
        if (mCurrent >= mDuration) {
            mDir = -mDir
        } else if (mCurrent <= 0) {
            mDir = -mDir
        }

        mData[1] -= mStep * 7 / mCount * mDir
        mCtrl[7] += mStep * 5 / mCount * mDir
        mCtrl[9] += mStep * 5 / mCount * mDir
        mCtrl[4] -= mStep / mCount * mDir
        mCtrl[10] += mStep / mCount * mDir

        if (isShown) {
            postInvalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w > h) {
            super.onSizeChanged(h, h, oldw, oldh)
        } else {
            super.onSizeChanged(w, w, oldw, oldh)
        }
        mCenterX = w / 2
        mCenterY = h / 2
        initSize()
    }

    companion object {
        const val C = 0.551915024494f
    }
}
