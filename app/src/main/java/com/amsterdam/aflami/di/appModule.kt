package com.amsterdam.aflami.di

val appModule = listOf(
    localDataSourceModule,
    remoteDataSourceModule,
    mapperModule,
    serviceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    appLoggerModule
)