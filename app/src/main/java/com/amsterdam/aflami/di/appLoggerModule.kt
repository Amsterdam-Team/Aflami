package com.amsterdam.aflami.di

import com.amsterdam.aflami.AppLogger
import com.example.domain.logger.Logger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appLoggerModule = module {
    singleOf<Logger>(::AppLogger)
}