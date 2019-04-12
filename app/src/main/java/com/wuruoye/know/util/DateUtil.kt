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
}