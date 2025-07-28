package com.amsterdam.aflami.di

import com.amsterdam.aflami.AppLogger
import com.amsterdam.domain.logger.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule {

    @Binds
    abstract fun bindLogger(appLogger: AppLogger): Logger
}
