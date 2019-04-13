package com.wuruoye.know.ui.home.behaviors

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.wuruoye.know.util.DensityUtil

/**
 * @Created : wuruoye
 * @Date : 2018/7/12 16:40.
 * @Description :
 */
class UserTVBehavior : CoordinatorLayout.Behavior<View> {
    private var top = 0F
    private var startX = 0F
    private var startY = 0F
    private var endX = 0F
    private var endY = 0F

    constructor(context: Context, attr: AttributeSet) {
        top = DensityUtil.dp2px(context, 50F)
        startY = DensityUtil.dp2px(context, 170F)
        endX = DensityUtil.dp2px(context, 56F)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View,
                                 dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View,
                                        dependency: View): Boolean {
        val height = dependency.height - top
        val progress = (dependency.height - dependency.bottom) / height

        startX = ((dependency.width - child.width) / 2).toFloat()
        endY = (DensityUtil.dp2px(dependency.context, 50F) - child.height) / 2

        val x = (startX - (startX - endX) * progress).toInt()
        val y = (startY - (startY - endY) * progress).toInt()

        child.layout(x, y, x + child.width, y + child.height)
        return true
    }
}