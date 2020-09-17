package com.nibalk.trafficcameramap.map.di

import androidx.lifecycle.ViewModelProvider
import com.nibalk.framework.base.ui.ViewModelProviderFactory
import com.nibalk.framework.network.retrofit.RetrofitManager
import com.nibalk.trafficcameramap.map.data.api.TrafficCamerasService
import com.nibalk.trafficcameramap.map.data.datasource.TrafficCamerasRemoteDataSource
import com.nibalk.trafficcameramap.map.data.repository.TrafficCamerasRepository
import com.nibalk.trafficcameramap.map.ui.viewmodel.TrafficCameraMapViewModel
import dagger.Module
import dagger.Provides

@Module
class TrafficCameraMapActivityModule {

    @Provides
    fun trafficCameraMapViewModel(repository: TrafficCamerasRepository): TrafficCameraMapViewModel {
        return TrafficCameraMapViewModel(repository)
    }

    @Provides
    fun provideTrafficCameraMapViewModel(viewModel: TrafficCameraMapViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(viewModel)
    }

    @Provides
    fun provideTrafficCameraMapRepository(dataSource: TrafficCamerasRemoteDataSource): TrafficCamerasRepository {
        return TrafficCamerasRepository(dataSource)
    }

    @Provides
    fun provideTrafficCamerasRemoteDataSource(service: TrafficCamerasService,
                                              retrofitManager: RetrofitManager
    ): TrafficCamerasRemoteDataSource {
        return TrafficCamerasRemoteDataSource(service, retrofitManager)
    }
}