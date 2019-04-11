package com.wuruoye.know.util

import java.util.concurrent.Executors

/**
 * Created at 2019/4/11 12:02 by wuruoye
 * Description:
 */
object ThreadPool {
    private val pool = Executors.newCachedThreadPool()

    fun exec(runnable: Runnable) {
        pool.execute(runnable)
    }
}