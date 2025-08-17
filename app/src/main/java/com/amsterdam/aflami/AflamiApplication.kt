package com.amsterdam.aflami

import android.app.Application
import com.amsterdam.domain.logger.LogManager
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AflamiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeAppLogger()
    }

    private fun initializeAppLogger() {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            InitializerEntryPoint::class.java
        )
        LogManager.initialize(hiltEntryPoint.getLoggerFactory())
    }
}