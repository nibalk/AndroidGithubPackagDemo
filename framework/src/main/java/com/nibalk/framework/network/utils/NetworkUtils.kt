package com.nibalk.framework.network.utils

import android.net.ConnectivityManager
import android.util.MalformedJsonException
import com.nibalk.framework.base.utils.BaseException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkUtils {

    companion object {

        fun isInternetAvailable(connectivityManager: ConnectivityManager): Boolean {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun mapDeviceOfflineException(): BaseException? {
            var errorCode = NETWORK_NO_NETWORK_EXCEPTION
            return BaseException(errorCode, null, "OFFLINE Error\n($errorCode):\n",
                "Device is offline")
        }

        fun mapHttpConnectionExceptions(exception: Throwable?): BaseException? {
            var errorCode = ""

            if (exception is ConnectException) {
                errorCode = NETWORK_CONNECT_EXCEPTION
            } else if (exception is UnknownHostException) {
                errorCode = NETWORK_UNKNOWN_HOST_EXCEPTION
            } else if (exception is SocketException) {
                errorCode = NETWORK_SOCKET_EXCEPTION
            } else if (exception is SocketTimeoutException) {
                errorCode = NETWORK_SOCKET_TIMEOUT_EXCEPTION
            } else if (exception is MalformedJsonException) {
                errorCode = NETWORK_MALFORMED_JSON_EXCEPTION
            }
            return BaseException(errorCode, exception, "HTTP Error\n($errorCode):\n",
                exception!!.message ?: exception.toString())
        }

        fun mapWebServiceException(response: Response<*>): BaseException? {
            var errorCode = ""

            errorCode = when (response.code()) {
                400 -> HTTP_CLIENT_INVALID_REQUEST_EXCEPTION
                403 -> HTTP_CLIENT_ACCESS_FORBIDDEN_EXCEPTION
                404 -> HTTP_CLIENT_RESOURCE_NOT_FOUND_EXCEPTION
                405 -> HTTP_CLIENT_REQUEST_METHOD_NOT_SUPPORT_EXCEPTION
                500 -> HTTP_SERVER_INTERNAL_SERVER_ERROR_EXCEPTION
                else -> response.code().toString()
            }

            return BaseException(errorCode, null, "API Error\n($errorCode):\n",
                response.errorBody()!!.source().toString() ?: response.message())
        }

        const val NETWORK_NO_NETWORK_EXCEPTION = "NETWORK_NO_NETWORK_EXCEPTION"
        const val NETWORK_CONNECT_EXCEPTION = "NETWORK_CONNECT_EXCEPTION"
        const val NETWORK_UNKNOWN_HOST_EXCEPTION = "NETWORK_UNKNOWN_HOST_EXCEPTION"
        const val NETWORK_SOCKET_EXCEPTION = "NETWORK_SOCKET_EXCEPTION"
        const val NETWORK_SOCKET_TIMEOUT_EXCEPTION = "NETWORK_SOCKET_TIMEOUT_EXCEPTION"
        const val NETWORK_MALFORMED_JSON_EXCEPTION = "NETWORK_MALFORMED_JSON_EXCEPTION"

        const val HTTP_CLIENT_INVALID_REQUEST_EXCEPTION = "ERROR_400"
        const val HTTP_CLIENT_ACCESS_FORBIDDEN_EXCEPTION = "ERROR_403"
        const val HTTP_CLIENT_RESOURCE_NOT_FOUND_EXCEPTION = "ERROR_404"
        const val HTTP_CLIENT_REQUEST_METHOD_NOT_SUPPORT_EXCEPTION = "ERROR_405"
        const val HTTP_SERVER_INTERNAL_SERVER_ERROR_EXCEPTION = "ERROR_500"
    }
}