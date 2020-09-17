package com.nibalk.trafficcameramap.map.data.datasource

import com.nibalk.framework.network.retrofit.RetrofitManager
import com.nibalk.trafficcameramap.map.data.api.TrafficCamerasService
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TrafficCamerasRemoteDataSource @Inject constructor(
    private val service: TrafficCamerasService,
    private val retrofitManager: RetrofitManager
) {

    suspend fun fetchTrafficImages() = retrofitManager.getNetworkResult {
        val currentTime = Calendar.getInstance().time

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        //simpleDateFormat.timeZone = TimeZone.getTimeZone("SGT")

        service.fetchTrafficImages(simpleDateFormat.format(currentTime))
    }
}