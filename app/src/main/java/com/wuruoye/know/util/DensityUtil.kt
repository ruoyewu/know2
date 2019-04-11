package com.wuruoye.know.util

import android.content.Context

/**
 * Created at 2019/4/10 20:52 by wuruoye
 * Description:
 */
object DensityUtil {
    fun dp2px(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun px2dp(context: Context, px: Float): Float {
        val scale = context.resources.displayMetrics.density
        return px / scale + 0.5f
    }

    fun sp2px(context: Context, sp: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, px: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (px / scale + 0.5f).toInt()
    }

    fun getStatusHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }
}