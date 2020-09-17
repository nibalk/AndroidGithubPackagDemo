package com.nibalk.trafficcameramap.map.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nibalk.trafficcameramap.map.data.repository.TrafficCamerasRepository
import javax.inject.Inject

class TrafficCameraMapViewModel @Inject constructor(
    private val repository: TrafficCamerasRepository
) : ViewModel() {

    fun fetchTrafficImages() = repository.fetchTrafficImages
}