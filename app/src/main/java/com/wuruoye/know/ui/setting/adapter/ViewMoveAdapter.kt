package com.wuruoye.know.ui.setting.adapter

import android.view.View

/**
 * Created at 2019/4/13 20:58 by wuruoye
 * Description:
 */
class ViewMoveAdapter(private val view: View) {
    private var targetPosition: Float = 0F
    private var current: Float = 0F
    private var moveUp: Boolean = false
    private var running: Boolean = false

    fun moveTo(target: Float) {
        targetPosition = target
        current = view.x
        moveUp = targetPosition > current

        if (target != current && !running) {
            running = true
            move()
        }
    }

    private fun move() {
        if (canMove()) {
            view.post {
                updateCurrent()
                splitIfShould()
                view.x = current
                move()
            }
        } else {
            running = false
        }
    }

    private fun canMove(): Boolean {
        return (moveUp && current < targetPosition) ||
                (!moveUp && current > targetPosition)
    }

    private fun updateCurrent() {
        current += getVelocity()
    }

    private fun getVelocity(): Float {
        val distance = Math.abs(targetPosition - current)
        val velocity = distance / MAX_TIME
        return (if (moveUp) 1 else -1) *
                (if (velocity > MIN_VELOCITY) velocity else MIN_VELOCITY)
    }

    private fun splitIfShould() {
        if ((moveUp && current > targetPosition) ||
            (!moveUp && current < targetPosition)) {
            current = targetPosition
        }
    }

    companion object {
        val MIN_VELOCITY = 20F
        val MAX_TIME = 8F
    }
}