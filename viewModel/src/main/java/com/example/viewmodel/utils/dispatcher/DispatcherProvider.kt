package com.example.viewmodel.utils.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val Main: CoroutineDispatcher
    val Default : CoroutineDispatcher
    val IO : CoroutineDispatcher
}