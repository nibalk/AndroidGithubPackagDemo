package com.nibalk.framework.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.nibalk.framework.network.di.NetworkModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class FrameworkModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}