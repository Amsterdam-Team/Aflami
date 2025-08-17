package com.amsterdam.aflami.di

import com.amsterdam.aflami.AppVersionProviderImpl
import com.amsterdam.domain.utils.AppVersionProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppVersionProvider(
        appVersionProviderImpl: AppVersionProviderImpl
    ): AppVersionProvider
}
