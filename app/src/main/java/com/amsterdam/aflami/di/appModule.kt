package com.amsterdam.aflami.di

val appModule = listOf(
    localDataSourceModule,
    remoteDataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    appLoggerModule
)