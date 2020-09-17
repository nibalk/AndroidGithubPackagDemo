package com.nibalk.trafficcameramap.map.data.api

import com.nibalk.trafficcameramap.map.data.model.Item
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TrafficCamerasService {

    companion object {
        const val BASE_URL = "https://api.data.gov.sg/v1/"
        const val API_STATUS = "healthy"
    }

    @GET("transport/traffic-images")
    suspend fun fetchTrafficImages(@Query("date_time") order: String? = null): Response<ResultsResponse<Item>>
}