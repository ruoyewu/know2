package com.wuruoye.know.util

import android.util.ArrayMap
import com.wuruoye.know.util.model.beans.NetResult
import okhttp3.*
import org.json.JSONObject

/**
 * Created at 2019-04-23 19:35 by wuruoye
 * Description:
 */
object NetUtil {
    private val HOST = "http://know.wuruoye.com/"
    val LOGIN = HOST + "user/login"
    val LOGOUT = HOST + "/user/logout"
    val VERIFY_CODE = HOST + "user/verify_code"
    val USER = HOST + "user/user"
    val UPLOAD_TOKEN = HOST + "user/token"

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

    private fun request(request: Request): NetResult {
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