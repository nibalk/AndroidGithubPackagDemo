package com.nibalk.trafficcameramap.map.data.repository

import com.nibalk.framework.base.data.BaseRepository
import com.nibalk.trafficcameramap.map.data.datasource.TrafficCamerasRemoteDataSource
import javax.inject.Inject


class TrafficCamerasRepository @Inject constructor(
    private val dataSource: TrafficCamerasRemoteDataSource
) : BaseRepository() {
    val fetchTrafficImages = resultLiveData(networkCall = { dataSource.fetchTrafficImages() })
}