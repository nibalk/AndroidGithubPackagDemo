package com.nibalk.trafficcameramap.di

import com.nibalk.framework.di.FrameworkModule
import com.nibalk.framework.network.retrofit.RetrofitManager
import com.nibalk.trafficcameramap.map.data.api.TrafficCamerasService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [FrameworkModule::class, ActivityBindingModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideNetworkService(retrofitManager: RetrofitManager,
                              okHttpClient: OkHttpClient,
                              converterFactory: GsonConverterFactory
    ) = provideRetrofitService(retrofitManager, okHttpClient,
        converterFactory, TrafficCamerasService::class.java)

    private fun <T> provideRetrofitService(retrofitManager: RetrofitManager,
                                           okHttpClient: OkHttpClient,
                                           converterFactory: GsonConverterFactory,
                                           clazz: Class<T>): T {
        return retrofitManager.createRetrofitClient(TrafficCamerasService.BASE_URL, okHttpClient,
            converterFactory).create(clazz)
    }

}