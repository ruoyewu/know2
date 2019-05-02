@file:Suppress("NAME_SHADOWING")

package com.wuruoye.know.util

import com.wuruoye.know.util.orm.table.Record
import com.wuruoye.know.util.orm.table.ReviewStrategy

/**
 * Created at 2019-05-01 10:24 by wuruoye
 * Description:
 */
object ReviewUtil {
    private const val ONE_MINUTE = 60000L
    private const val ONE_HOUR = 3600000L
    private const val ONE_DAY = 86400000L
    private var isDebug = false

    fun isShow(record: Record, strategy: ReviewStrategy): Boolean {
        if (isDebug) return true

        val current = System.currentTimeMillis()
        val nextReviewTime = nextReviewTime(record, strategy)

        if (nextReviewTime in 1..current)
            return true
        return false
    }

    fun nextReviewTime(record: Record, strategy: ReviewStrategy): Long {
        val remTime = record.remNum * 2 + record.reviewNum
        if (remTime >= strategy.rememberTime) {
            return -1
        }

        val reviewTime = remTime - record.failNum
        val gapTime = realGapTime(strategy.gapTime * ONE_MINUTE, reviewTime+1)
        log("gap time : $gapTime")
        val lastReview =
            Math.max(Math.max(record.lastRemReview, record.lastReview),
                Math.max(record.lastFailReview, record.createTime))
        return lastReview + gapTime
    }

    private fun realGapTime(gapTime: Long, remTime: Int): Long {
        var gapTime = gapTime
        if (gapTime < ONE_MINUTE) gapTime = ONE_MINUTE

        gapTime *= remTime

        if (remTime > 2)
            gapTime *= 2

        if (remTime > 1)
            gapTime *= gapTime / ONE_MINUTE

        if (gapTime > ONE_DAY)
            gapTime = Math.sqrt((gapTime * ONE_DAY).toDouble()).toLong()

        return gapTime
    }
}