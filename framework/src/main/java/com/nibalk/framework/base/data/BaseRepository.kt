package com.nibalk.framework.base.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

abstract class BaseRepository {

    protected fun <T> resultLiveData(networkCall: suspend () -> BaseResult<T>): LiveData<BaseResult<T>> =
        liveData(Dispatchers.IO) {
            emit(BaseResult.loading())

            val responseStatus = networkCall.invoke()
            if (responseStatus.status == BaseResult.Status.SUCCESS) {
                emit(BaseResult.success(responseStatus.data!!))
            } else if (responseStatus.status == BaseResult.Status.ERROR) {
                emit(BaseResult.error(responseStatus.message!!))
            }
        }
}