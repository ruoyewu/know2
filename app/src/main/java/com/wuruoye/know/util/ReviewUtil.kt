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
        val remTime = record.remNum * 2 + record.reviewNum

        if (remTime < strategy.rememberTime) {
            val reviewTime = remTime - record.failNum
            val gapTime = realGapTime(strategy.gapTime, reviewTime+1)
            val lastReview =
                Math.max(Math.max(record.lastRemReview, record.lastReview),
                    Math.max(record.lastFailReview, record.createTime))
            if (gapTime + lastReview <= current) {
                return true
            }
        }
        return false
    }

    private fun realGapTime(gapTime: Long, remTime: Int): Long {
        var gapTime = gapTime
        if (gapTime < ONE_MINUTE) gapTime = ONE_MINUTE

        gapTime *= remTime
        if (remTime > 1 && gapTime < ONE_HOUR) {
            gapTime *= gapTime
        } else if (remTime > 2 && gapTime < ONE_DAY) {
            gapTime *= 2
        }

        return gapTime
    }
}