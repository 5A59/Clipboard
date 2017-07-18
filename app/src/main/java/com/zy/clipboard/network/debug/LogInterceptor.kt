package com.zy.clipboard.network.debug

import com.zy.clipboard.utils.Logger
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by zy on 17-6-2.
 */
class LogInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain?.request()
        var response = chain?.proceed(request)
        var responseBody = response?.peekBody(1024 * 1024)
        Logger.d(responseBody?.string())
        return response!!
    }
}