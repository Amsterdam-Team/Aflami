package com.amsterdam.aflami.di

import com.example.localdatasource.roomDataBase.di.localDataSourceModule
import com.example.remotedatasource.di.remoteDataSourceModule
import com.example.repository.di.repositoryModule

val appModule = listOf(
    localDataSourceModule,
    remoteDataSourceModule,
    repositoryModule,
    useCaseModule,
    viewModelModule,
)

