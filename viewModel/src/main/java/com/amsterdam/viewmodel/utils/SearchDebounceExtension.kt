package com.amsterdam.viewmodel.utils

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@OptIn(FlowPreview::class)
suspend fun Flow<String>.debounceSearch(onCollect: suspend (String) -> Unit) {
    this.distinctUntilChanged()
        .debounce(300L)
        .map(String::trim)
        .filter(String::isNotBlank)
        .collectLatest { keyword -> onCollect(keyword) }
}