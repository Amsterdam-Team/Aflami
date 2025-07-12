package com.amsterdam.aflami

import android.app.Application
import com.amsterdam.aflami.di.appModule
import com.amsterdam.aflami.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class AflamiApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AflamiApplication)
            modules(
                appModule
            )
        }
    }
}