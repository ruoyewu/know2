package com.wuruoye.know.util.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Created at 2019/4/10 15:22 by wuruoye
 * Description:
 */
class AppCache private constructor(context: Context) {
    private lateinit var sp: SharedPreferences

    init {
        sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    var typeTimeLimit: Int
        get() = sp.getInt(TYPE_LIMIT_TIME, 0)
        set(value) = sp.edit().putInt(TYPE_LIMIT_TIME, value).apply()

    var typeTypeLimit: Long
        get() = sp.getLong(TYPE_LIMIT_TYPE, -1)
        set(value) = sp.edit().putLong(TYPE_LIMIT_TYPE, value).apply()

    companion object {
        private const val NAME = "com.wuruoye.know.sp"
        private const val TYPE_LIMIT_TIME = "type_limit_time"
        private const val TYPE_LIMIT_TYPE = "type_limit_type"

        @Volatile
        private var sInstance: AppCache? = null

        fun getInstance(context: Context): AppCache {
            if (sInstance == null) {
                synchronized(this) {
                    if (sInstance == null) {
                        sInstance = AppCache(context)
                    }
                }
            }
            return sInstance!!
        }
    }
}