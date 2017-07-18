package com.zy.clipboard.network

import com.zy.clipboard.network.data.BASE_URL
import com.zy.clipboard.network.data.CookieStore
import com.zy.clipboard.network.debug.LogInterceptor
import com.zy.clipboard.utils.Logger
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by zy on 17-5-31.
 */
object Network {

    fun getBaseRetrofit(url: String): Retrofit {
        val retrofit = Retrofit.Builder()
                .client(makeOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build()
        return retrofit
    }

    fun getDefaultServer(): Server {
        return getBaseRetrofit(BASE_URL).create(Server::class.java)
    }

    fun makeOkhttpClient(): OkHttpClient {
        var client = OkHttpClient.Builder()
                .addInterceptor(LogInterceptor())
                .cookieJar(object : CookieJar {

                    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
                        if (url == null || cookies == null)
                            return
                        CookieStore.saveCookie(url, cookies)
                    }

                    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
                        return CookieStore.getCookie(url)
                    }
                })
                .build()

        return client
    }
}
