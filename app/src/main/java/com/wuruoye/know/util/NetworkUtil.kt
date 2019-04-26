package com.wuruoye.know.util

import android.content.Context
import android.net.ConnectivityManager
import com.wuruoye.know.util.base.WConfig

/**
 * Created at 2019-04-26 19:03 by wuruoye
 * Description:
 */
object NetworkUtil {
    fun isAvailable(): Boolean {
        val manager = WConfig.getAppContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

        val info = manager.activeNetworkInfo
        if (info == null || !info.isConnected) {
            return false
        }

        return true
    }
}