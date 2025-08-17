package com.amsterdam.aflami.di

import com.amsterdam.aflami.AppLogger
import com.amsterdam.domain.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Singleton
    @Provides
    fun provideLoggerFactory(): (String) -> Logger {
        return { tag: String -> AppLogger(tag) }
    }
}