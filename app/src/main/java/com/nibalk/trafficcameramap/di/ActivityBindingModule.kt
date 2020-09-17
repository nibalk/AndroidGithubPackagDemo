package com.nibalk.trafficcameramap.di

import com.nibalk.framework.di.ActivityScope
import com.nibalk.trafficcameramap.map.di.TrafficCameraMapActivityModule
import com.nibalk.trafficcameramap.map.ui.view.TrafficCameraMapActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [TrafficCameraMapActivityModule::class])
    internal abstract fun bindTrafficCameraMapActivity(): TrafficCameraMapActivity
}