package com.amsterdam.aflami.di

val appModule = listOf(
    localDataSourceModule,
    remoteDataSourceModule,
    mapperModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
    appLoggerModule
)