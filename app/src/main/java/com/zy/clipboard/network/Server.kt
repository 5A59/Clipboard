package com.zy.clipboard.network

import com.zy.clipboard.network.data.CopyMsgRes
import com.zy.clipboard.network.data.GeneralRes
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by zy on 17-5-29.
 */
interface Server {
    @GET("board/login/")
    fun login(@Query("username") username: String?): Observable<GeneralRes>

    @GET("board/getCopyMsg/")
    fun getCopyMsg(): Observable<CopyMsgRes>
}