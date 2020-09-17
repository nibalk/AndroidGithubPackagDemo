package com.nibalk.trafficcameramap

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.nibalk.trafficcameramap.di.AppComponent
import com.nibalk.trafficcameramap.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class TrafficCameraMapApplication : Application(), HasActivityInjector {

    var appComponent: AppComponent? = null

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initTimberLogs()
    }

    private fun initDagger() {
        val component: AppComponent? = DaggerAppComponent.builder()
            .application(this)
            .build()
        component!!.inject(this)
        appComponent = component
    }

    private fun initTimberLogs() {
        Timber.plant(Timber.DebugTree())
        Timber.tag("TrafficImages:")
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }
}