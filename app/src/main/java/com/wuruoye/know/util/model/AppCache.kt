package com.wuruoye.know.util.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created at 2019/4/10 15:22 by wuruoye
 * Description:
 */
class AppCache private constructor(context: Context) {
    private var sp: SharedPreferences
    private var edit: SharedPreferences.Editor

    init {
        sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        @SuppressLint("CommitPrefEdits")
        edit = sp.edit()
    }

    var typeTimeLimit: Int
        get() = sp.getInt(TYPE_LIMIT_TIME, 0)
        set(value) = edit.putInt(TYPE_LIMIT_TIME, value).apply()

    var typeTypeLimit: Long
        get() = sp.getLong(TYPE_LIMIT_TYPE, -1)
        set(value) = edit.putLong(TYPE_LIMIT_TYPE, value).apply()

    var typeTagLimit: Long
        get() = sp.getLong(TYPE_LIMIT_TAG, -1)
        set(value) = edit.putLong(TYPE_LIMIT_TAG, value).apply()

    var initRecordTag: Boolean
        get() = sp.getBoolean(TAG_INIT, false)
        set(value) = edit.putBoolean(TAG_INIT, value).apply()

    var userLogin: Boolean
        get() = sp.getBoolean(USER_LOGIN, false)
        set(value) = edit.putBoolean(USER_LOGIN, value).apply()

    var userId: String
        get() = sp.getString(USER_ID, "") ?: ""
        set(value) = edit.putString(USER_ID, value).apply()

    var userPwd: String
        get() = sp.getString(USER_PWD, "") ?: ""
        set(value) = edit.putString(USER_PWD, value).apply()

    var haptic: Boolean
        get() = sp.getBoolean(HAPTIC, true)
        set(value) = edit.putBoolean(HAPTIC, value).apply()

    companion object {
        private const val NAME = "com.wuruoye.know.sp"
        private const val TAG_INIT = "tag_init"
        private const val TYPE_LIMIT_TIME = "type_limit_time"
        private const val TYPE_LIMIT_TYPE = "type_limit_type"
        private const val TYPE_LIMIT_TAG = "type_limit_tag"
        private const val USER_LOGIN = "user_login"
        val USER_ID = "user_id"
        val USER_PWD = "user_pwd"
        val HAPTIC = "haptic"

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