package com.wuruoye.know.widgets.scrollview

import android.view.Choreographer
import android.view.View

/**
 * Created at 2019/4/13 20:58 by wuruoye
 * Description:
 */
class ViewMoveAdapter(private val view: View) {
    private var mChoreographer: Choreographer = Choreographer.getInstance()

    private var mTarget: Float = 0F
    private var mCurrent: Float = 0F
    private var isMoveUp: Boolean = false
    private var isRunning: Boolean = false
    private var isCancel: Boolean = false
    private var mListener: OnScrollChangedListener? = null

    fun moveTo(target: Float) {
        isCancel = false
        mTarget = target
        mCurrent = view.x
        isMoveUp = mTarget > mCurrent

        if (target != mCurrent && !isRunning) {
            isRunning = true
            move()
        }
    }

    fun cancel() {
        this.isCancel = true
    }

    fun setOnScrollChangedListener(listener: OnScrollChangedListener) {
        mListener = listener
    }

    private fun move() {
        if (!isCancel && canMove()) {
            mChoreographer.postFrameCallback {
                updateCurrent()
                splitIfShould()
                mListener?.onChanged(view.x, mCurrent)
                view.x = mCurrent
                move()
            }
        } else {
            isRunning = false
        }
    }

    private fun canMove(): Boolean {
        return (isMoveUp && mCurrent < mTarget) ||
                (!isMoveUp && mCurrent > mTarget)
    }

    private fun updateCurrent() {
        mCurrent += getVelocity()
    }

    private fun getVelocity(): Float {
        val distance = Math.abs(mTarget - mCurrent)
        val velocity = distance / MAX_TIME
        return (if (isMoveUp) 1 else -1) *
                (if (velocity > MIN_VELOCITY) velocity else MIN_VELOCITY)
    }

    private fun splitIfShould() {
        if ((isMoveUp && mCurrent > mTarget) ||
            (!isMoveUp && mCurrent < mTarget)) {
            mCurrent = mTarget
        }
    }

    interface OnScrollChangedListener {
        fun onChanged(last: Float, cur: Float)
    }

    companion object {
        const val MIN_VELOCITY = 20F
        const val MAX_TIME = 8F
    }
}