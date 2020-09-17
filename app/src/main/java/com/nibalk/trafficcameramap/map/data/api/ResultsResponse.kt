package com.nibalk.trafficcameramap.map.data.api

data class ResultsResponse<T>(
    val api_info: ApiInfo,
    val items: List<T>
)

data class ApiInfo(
    val status: String
)

