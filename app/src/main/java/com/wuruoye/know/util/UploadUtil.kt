package com.wuruoye.know.util

import com.qiniu.android.common.AutoZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import com.wuruoye.know.util.model.beans.NetResult

/**
 * Created at 2019-04-24 13:32 by wuruoye
 * Description:
 */
object UploadUtil {
    private val uploadManager: UploadManager
        = UploadManager(
        Configuration.Builder()
            .zone(AutoZone.autoZone)
            .build()
    )

    fun uploadFile(file: String, key: String, token: String): NetResult {
        val response = uploadManager.syncPut(file, key, token, null)
        return if (response.isOK) {
            NetResult(200)
        } else {
            NetResult(response.statusCode, response.error)
        }
    }
}