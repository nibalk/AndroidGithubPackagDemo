package com.nibalk.framework.network.retrofit

import android.net.ConnectivityManager
import com.nibalk.framework.base.data.BaseResult
import com.nibalk.framework.base.utils.BaseException
import com.nibalk.framework.network.utils.NetworkUtils
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

/*
 * Can modify this class to provide a custom retrofit client
 */
class RetrofitManagerImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : RetrofitManager {

    override fun createRetrofitClient(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    override suspend fun <T> getNetworkResult(
        call: suspend () -> Response<T>
    ): BaseResult<T> {
        if (NetworkUtils.isInternetAvailable(connectivityManager)) {
            try {
                val response = call()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) return BaseResult.success(body)
                }
                return error(NetworkUtils.mapWebServiceException(response))
                //return error(" ${response.code()} ${response.message()}")
            } catch (e: Exception) {
                return error(NetworkUtils.mapHttpConnectionExceptions(e))
                //return error(e.message ?: e.toString())
            }
        } else {
            return error(NetworkUtils.mapDeviceOfflineException())
        }
    }

    private fun <T> error(exception: BaseException?): BaseResult<T> {
        val fullMessage = "${exception!!.errorTitle}\n${exception.errorMessage}"
        Timber.e(fullMessage)
        return BaseResult.error(fullMessage)
    }
}