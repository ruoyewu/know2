package com.wuruoye.know.util

import android.util.ArrayMap
import com.qiniu.android.common.AutoZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import com.wuruoye.know.util.base.FileUtil
import com.wuruoye.know.util.model.beans.NetResult
import com.wuruoye.know.util.model.beans.TokenResult
import okhttp3.*
import org.json.JSONObject

/**
 * Created at 2019-04-23 19:35 by wuruoye
 * Description:
 */
object NetUtil {
    private val HOST = "http://know.wuruoye.com/"
    private const val FILE_PATH = "http://qiniu.wuruoye.com/"
    val LOGIN = HOST + "user/login"
    val LOGOUT = HOST + "/user/logout"
    val VERIFY_CODE = HOST + "user/verify_code"
    val USER = HOST + "user/user"
    val UPLOAD_TOKEN = HOST + "user/token"
    val BACKUP = HOST + "backup/backup"
    val BACKUP_INFO = HOST + "backup/info"

    private val cookieMap = ArrayMap<String, MutableList<Cookie>>()
    private val mClient = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                cookieMap[url.host()] = cookies
            }

            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                return cookieMap[url.host()] ?: mutableListOf()
            }

        })
        .build()
    private val uploadManager: UploadManager
            = UploadManager(
        Configuration.Builder()
            .zone(AutoZone.autoZone)
            .build()
    )

    fun get(url: String, values: Map<String, String>): NetResult {
        val builder = StringBuilder(url)
        builder.append("?")
        for (entry in values.entries) {
            builder.append(entry.key).append("=")
                .append(entry.value).append("&")
        }

        val request = Request.Builder()
            .url(builder.toString())
            .build()

        return request(request)
    }

    fun post(url: String, values: Map<String, String>): NetResult {
        val builder = FormBody.Builder()
        for (entry in values.entries) {
            builder.add(entry.key, entry.value)
        }

        val request = Request.Builder()
            .url(url)
            .post(builder.build())
            .build()

        return request(request)
    }

    fun put(url: String, values: Map<String, String>): NetResult {
        val builder = FormBody.Builder()
        for (entry in values.entries) {
            builder.add(entry.key, entry.value)
        }

        val request = Request.Builder()
            .url(url)
            .put(builder.build())
            .build()

        return request(request)
    }

    fun uploadFile(file: String): NetResult {
        val tokenResult = get(UPLOAD_TOKEN, mapOf())
        if (tokenResult.successful) {
            val token = GsonFactory.getInstance()
                .fromJson(tokenResult.data!!, TokenResult::class.java)
            val response = uploadManager
                .syncPut(file, token.key, token.token, null)
            if (response.isOK) {
                return NetResult(200, "ok", "$FILE_PATH${token.key}")
            } else {
                return NetResult(response.statusCode, response.error)
            }
        } else {
            return tokenResult
        }
    }

    fun downloadFile(url: String, file: String): NetResult {
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response = mClient.newCall(request).execute()
            if (response.isSuccessful) {
                val inStream = response.body()!!.byteStream()
                val result = FileUtil.writeInputStream(file, inStream)
                if (result) {
                    return NetResult(200, "ok")
                } else {
                    return NetResult(400, "error in write file")
                }
            } else {
                return NetResult(400, "error in download file")
            }
        } catch (e: Exception) {
            return NetResult(400, e.message!!)
        }
    }

    private fun request(request: Request): NetResult {
        if (!NetworkUtil.isAvailable()) {
            return NetResult(-1, "no network available")
        }

        val response = mClient.newCall(request).execute()
        if (response.isSuccessful) {
            val obj = JSONObject(response.body()!!.string())
            val res = obj.getInt("res")
            val msg = if (obj.has("msg")) obj.getString("msg") else null
            val data = if (obj.has("data")) obj.getString("data") else null
            return NetResult(res, msg, data)
        } else {
            return NetResult(400, response.message())
        }
    }
}