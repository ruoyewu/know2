package com.wuruoye.know.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created at 2019/4/5 15:31 by wuruoye
 * Description:
 */
object DateUtil {
    fun milli2Date(milli: Long): String {
        return DateUtil.milli2Date("yyyy-MM-dd HH:mm", milli)
    }

    @SuppressLint("SimpleDateFormat")
    fun milli2Date(format: String, milli: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milli
        val df = SimpleDateFormat(format)
        return df.format(calendar.time)
    }

    fun num2cn(num: Long): String {
        var num = num
        var level = 0
        val builder = StringBuilder()
        while (num > 0) {
            val n = (num % 10000).toInt()
            val clot = chFromNum(n)
            if (clot.isNotEmpty()) {
                builder.append(LEVEL_ITEM[level]).append(clot.reversed())
            }

            num /= 10000
            ++level
        }

        if (builder.isNotEmpty() && builder.last() == '零') {
            builder.setLength(builder.length-1)
        }
        return builder.reverse().toString()
    }

    private fun chFromNum(num: Int): String {
        var num = num
        var decimal = 0
        val build = StringBuilder()
        while (num > 0) {
            val n = num % 10
            val clot = if (n > 0) {
                "${NUM_ITEM[n]}${DECIMAL_ITEM[decimal]}"
            } else {
                if (build.isEmpty() || build.last().toString() == NUM_ITEM[0]) ""
                else NUM_ITEM[0]
            }
            build.append(clot.reversed())

            num /= 10
            ++decimal
        }

        // change 一十二 to 十二 for 12
        if (build.length > 1 && build.last() == '一' && build.elementAt(build.length-2) == '十')
            build.setLength(build.length-1)


        if (num == 0 && decimal < 4) {
            build.append('零')
        }

        return build.reverse().toString()
    }

    private val NUM_ITEM = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    private val DECIMAL_ITEM = arrayOf("", "十", "百", "千")
    private val LEVEL_ITEM = arrayOf("", "万", "亿", "兆")
}