package com.wuruoye.know.util

import com.wuruoye.know.util.model.beans.NetResult
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/**
 * Created at 2019-04-23 19:35 by wuruoye
 * Description:
 */
object NetUtil {
    private val HOST = "http://know.wuruoye.com/"
    val LOGIN = HOST + "user/login"
    val VERIFY_CODE = HOST + "user/verify_code"
    val USER = HOST + "user/user"

    private val mClient = OkHttpClient.Builder().build()

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

    private fun request(request: Request): NetResult {
        val response = mClient.newCall(request).execute()
        return if (response.isSuccessful) {
            val obj = JSONObject(response.body()!!.string())
            val res = obj.getInt("res")
            val msg = if (obj.has("msg")) obj.getString("msg") else null
            val data = if (obj.has("data")) obj.getString("data") else null
            NetResult(res, msg, data)
        } else {
            NetResult(400, "error in okhttp")
        }
    }
}