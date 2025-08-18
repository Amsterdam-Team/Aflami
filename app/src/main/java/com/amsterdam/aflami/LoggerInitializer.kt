package com.amsterdam.aflami

import com.amsterdam.domain.logger.Logger
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface InitializerEntryPoint {
    fun getLoggerFactory(): @JvmSuppressWildcards () -> Logger
}