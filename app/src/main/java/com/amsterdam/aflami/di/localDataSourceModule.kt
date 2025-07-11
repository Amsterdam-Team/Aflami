package com.amsterdam.aflami.di

import com.example.localdatasource.roomDataBase.AflamiDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataSourceModule = module {
    single { AflamiDatabase.getInstance(androidApplication()) }
}