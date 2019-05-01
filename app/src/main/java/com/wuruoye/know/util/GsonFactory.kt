package com.wuruoye.know.util

import com.google.gson.Gson

/**
 * Created at 2019/4/7 09:56 by wuruoye
 * Description:
 */
object GsonFactory {
    private var gson: Gson? = null

    val sInstance: Gson =
        if (gson == null) {
            synchronized(this) {
                if (gson == null) {
                    gson = Gson()
                    gson!!
                } else {
                    gson!!
                }
            }
        } else {
            gson!!
        }
}