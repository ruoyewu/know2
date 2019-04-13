package com.wuruoye.know.ui.home.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.wuruoye.know.util.DensityUtil

/**
 * @Created : wuruoye
 * @Date : 2018/7/12 15:52.
 * @Description :
 */
class UserIVBehavior : CoordinatorLayout.Behavior<View> {
    private var length = 0F
    private var top = 0F
    private var startX = 0F
    private var startY = 0F
    private var endX = 0F
    private var endY = 0F

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        length = DensityUtil.dp2px(context, 80F)
        top = DensityUtil.dp2px(context, 50F)
        endX = DensityUtil.dp2px(context, 16F)
        startY = DensityUtil.dp2px(context, 80F)
        endY = DensityUtil.dp2px(context, 10F)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View,
                                 dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View,
                                        dependency: View): Boolean {
        val height = dependency.height - top
        val progress = (dependency.height - dependency.bottom) / height
        val scale = 1 - progress * 0.625

        startX = (dependency.width - length) / 2

        val x = (startX - (startX - endX) * progress).toInt()
        val y = (startY - (startY - endY) * progress).toInt()

        child.layout(x, y, (x + length * scale).toInt(), (y + length * scale).toInt())
        return true
    }
}