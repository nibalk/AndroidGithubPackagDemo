package com.nibalk.trafficcameramap.di

import android.app.Application
import com.nibalk.trafficcameramap.TrafficCameraMapApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent?
    }

    fun inject(app: TrafficCameraMapApplication?)
}