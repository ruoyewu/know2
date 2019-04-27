package com.wuruoye.know.widgets.scrollview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.wuruoye.know.R
import com.wuruoye.know.util.log

/**
 * Created at 2019/4/19 09:02 by wuruoye
 * Description:
 */
class ScrollItemView : FrameLayout, ViewMoveAdapter.OnScrollChangedListener {
    private var mLeftColor: Int = 0
    private var mRightColor: Int = 0
    private var mLeftMax: Float = 0F
    private var mRightMax: Float = 0F
    private var mLeftView: View? = null
    private var mRightView: View? = null

    private var mMainView: View? = null
    private var mMaxLeft: Float = 0F
    private var mMaxRight: Float = 0F
    private var mMoveLeft: Float = 0F
    private var mMoveRight: Float = 0F
    private var mDeleteLength: Float = 0F

    private lateinit var mVmMain: ViewMoveAdapter
    private var mVmLeft: ViewMoveAdapter? = null
    private var mVmRight: ViewMoveAdapter? = null

    private var mOffsetX: Float = 0F
    private var mStartX: Float = 0F
    private var mStartY: Float = 0F
    private var mOnePass: Boolean = false
    private var mIsDragging: Boolean = false

    private var mClickListener: OnClickListener? = null
    private var mScrollListener: OnScrollListener? = null
    private var mTouchDownListener: OnTouchDownListener? = null

    var isScrollable = true

    constructor(context: Context) : super(context) {
        initAttr(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initAttr(context, attrs)
    }

    fun setOnClickListener(listener: OnClickListener) {
        mClickListener = listener
    }

    fun setOnScrollListener(listener: OnScrollListener) {
        mScrollListener = listener
    }

    fun setOnTouchDownListener(action: () -> Unit) {
        mTouchDownListener = object : OnTouchDownListener {
            override fun onActionDown() {
                action()
            }
        }
    }

    fun openLeft() {
        mVmMain.moveTo(mMoveLeft)
    }

    fun openRight() {
        mVmMain.moveTo(mDeleteLength-mMoveRight)
    }

    fun close() {
        mVmMain.moveTo(0F)
    }

    fun closeDirectly() {
        mMainView?.x = 0F
        mLeftView?.x = -mMoveLeft
        mRightView?.x = mDeleteLength
    }

    fun deleteLeft() {
        mOnePass = true
        mVmMain.moveTo(mDeleteLength)
    }

    fun deleteRight() {
        mOnePass = true
        mVmMain.moveTo(-mDeleteLength)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.ScrollItemView)
        mLeftColor = arr.getColor(R.styleable.ScrollItemView_left_color, 0)
        mRightColor = arr.getColor(R.styleable.ScrollItemView_right_color, 0)
        mLeftMax = arr.getFloat(R.styleable.ScrollItemView_left_max, 0F)
        mRightMax = arr.getFloat(R.styleable.ScrollItemView_right_max, 0F)
        val lv = arr.getResourceId(R.styleable.ScrollItemView_left_view, 0)
        if (lv != 0) {
            mLeftView = LayoutInflater.from(context).inflate(lv, this, false)
            addView(mLeftView)
        }
        val rv = arr.getResourceId(R.styleable.ScrollItemView_right_view, 0)
        if (rv != 0) {
            mRightView = LayoutInflater.from(context).inflate(rv, this, false)
            addView(mRightView)
        }
        arr.recycle()

        post {
            initLengthAndPosition()
        }
    }

    private fun initLengthAndPosition() {
        val width = this.width

        mDeleteLength = width.toFloat()

        mMainView = getChildAt(childCount-1)

        val lp = mMainView!!.layoutParams
        lp.width = width
        mMainView!!.layoutParams = lp

        val lv = mLeftView
        if (lv != null) {
            mMoveLeft = lv.width.toFloat()
            lv.x = -mMoveLeft
            mMaxLeft = width * mLeftMax
            mVmLeft = ViewMoveAdapter(lv)
            lv.setOnClickListener { mClickListener?.onLeftClick() }
        }

        val rv = mRightView
        if (rv != null) {
            mMoveRight = rv.width.toFloat()
            rv.x = width.toFloat()
            mMaxRight = width * mRightMax
            mVmRight = ViewMoveAdapter(rv)
            rv.setOnClickListener { mClickListener?.onRightClick() }
        }

        mVmMain = ViewMoveAdapter(mMainView!!)
        mVmMain.setOnScrollChangedListener(this)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_MOVE && mIsDragging) {
            return true
        }

        when(ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = ev.x
                mStartY = ev.y
                mOffsetX = mMainView!!.x
                mVmMain.cancel()
                mTouchDownListener?.onActionDown()
            }
            MotionEvent.ACTION_MOVE -> {
                val slotX = ev.x - mStartX
                val slotY = ev.y - mStartY
                mIsDragging = Math.abs(slotX) > Math.abs(slotY)
                if (mIsDragging) {
                    mStartX = ev.x
                    mStartY = ev.y
                }
            }
        }
        return isScrollable && mIsDragging
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!isScrollable) return false
        parent.requestDisallowInterceptTouchEvent(true)
        when(event?.action) {
            MotionEvent.ACTION_MOVE -> {
                val offsetX = event.x - mStartX + mOffsetX
                if ((offsetX > 0 && mVmLeft != null) ||
                    (offsetX < 0 && mVmRight != null)) {
                    val realX = splitIfNeed(if (offsetX > 0) mMaxLeft else -mMaxRight, offsetX)
                    onChanged(mMainView!!.x, realX)
                    mMainView!!.x = realX
                    if (offsetX > 0) {
                        setBackgroundColor(mLeftColor)
                    } else {
                        setBackgroundColor(mRightColor)
                    }
                } else {
                    val realX = 0F
                    onChanged(mMainView!!.x, realX)
                    mMainView!!.x = realX
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                mIsDragging = false
                val offsetX = mMainView!!.x
                if (offsetX > 0) {
                    when {
                        offsetX > mMaxLeft -> {
                            mVmMain.moveTo(mDeleteLength)
                            mScrollListener?.onPreLeft()
                        }
                        offsetX > mMoveLeft/2 -> mVmMain.moveTo(mMoveLeft)
                        else -> mVmMain.moveTo(0F)
                    }
                } else {
                    when {
                        offsetX < -mMaxRight -> {
                            log("review do pre right")
                            mVmMain.moveTo(-mDeleteLength)
                            mScrollListener?.onPreRight()
                        }
                        offsetX < -mMoveRight/2 -> mVmMain.moveTo(-mMoveRight)
                        else -> mVmMain.moveTo(0F)
                    }
                }
            }
        }
        return true
    }

    override fun onChanged(last: Float, cur: Float) {
        if (!mOnePass) {
            if (last <= mMaxLeft && cur > mMaxLeft) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                mVmLeft?.moveTo(mMaxLeft - mMoveLeft)
            } else if (last > mMaxLeft && cur <= mMaxLeft) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                mVmLeft?.moveTo(0F)
            } else if (last >= -mMaxRight && cur < -mMaxRight) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                mVmRight?.moveTo(mDeleteLength-mMaxRight)
            } else if (last < -mMaxRight && cur >= -mMaxRight) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                mVmRight?.moveTo(mDeleteLength - mMoveRight)
            }

            if (cur > 0) {
                when {
                    cur < mMoveLeft -> mVmLeft?.moveTo(cur-mMoveLeft)
                    cur > mMaxLeft -> mVmLeft?.moveTo(cur-mMoveLeft)
                    else -> mVmLeft?.moveTo(0F)
                }
            } else {
                when {
                    cur > -mMoveRight -> mVmRight?.moveTo(mDeleteLength+cur)
                    cur < -mMaxRight -> mVmRight?.moveTo(mDeleteLength+cur)
                    else -> mVmRight?.moveTo(mDeleteLength - mMoveRight)
                }
            }
        } else {
            if (cur > 0) {
                mVmLeft?.moveTo(cur-mMoveLeft)
            } else {
                mVmRight?.moveTo(mDeleteLength+cur)
            }
        }


        if (last <= mDeleteLength && cur == mDeleteLength) {
            mOnePass = false
            postDelayed( { closeDirectly() }, DELAY)
            mScrollListener?.onLeft()
        } else if (last >= -mDeleteLength && cur == -mDeleteLength) {
            mOnePass = false
            postDelayed( { closeDirectly() }, DELAY)
            mScrollListener?.onRight()
        }
    }

    private fun splitIfNeed(max: Float, cur: Float): Float {
        return if (Math.abs(cur) > Math.abs(max)) {
            max + (cur - max) * RESISTANCE
        } else {
            cur
        }
    }

    interface OnClickListener {
        fun onLeftClick()
        fun onRightClick()
    }

    abstract class OnScrollListener {
        open fun onPreLeft() {}
        open fun onPreRight() {}
        open fun onLeft() {}
        open fun onRight() {}
    }

    interface OnTouchDownListener {
        fun onActionDown()
    }

    companion object {
        private const val RESISTANCE = 0.3F
        private const val DELAY = 200L
    }
}
