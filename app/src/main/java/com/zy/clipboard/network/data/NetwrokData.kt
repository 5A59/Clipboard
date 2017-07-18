package com.zy.clipboard.network.data

import okhttp3.Cookie
import okhttp3.HttpUrl

/**
 * Created by zy on 17-5-29.
 */

object CookieStore {
//    val cookieStore = HashMap<HttpUrl, MutableList<Cookie>>()
    val cookieStore = ArrayList<Cookie>()

    fun saveCookie(url: HttpUrl?, cookies: MutableList<Cookie>?) {
        if (url == null || cookies == null) {
            return
        }
        cookieStore.clear()
        cookieStore.addAll(cookies)
    }

    fun getCookie(url: HttpUrl?): MutableList<Cookie> {
        return cookieStore
    }
}

data class GeneralRes (val res: String)

data class CopyMsg (var content: String, var date: String)

data class CopyMsgRes (var res: String, var msgs: List<CopyMsg>)

val BASE_URL = "http://100.66.4.175:8088"
//val BASE_URL = "http://192.168.23.139:8088"
//val BASE_URL = "http://127.0.0.1:8088"
