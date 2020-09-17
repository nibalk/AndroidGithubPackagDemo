package com.nibalk.framework.network.retrofit

import com.nibalk.framework.base.data.BaseResult
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit

interface RetrofitManager {

    fun createRetrofitClient(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit

    suspend fun <T> getNetworkResult(call: suspend () -> Response<T>): BaseResult<T>
}