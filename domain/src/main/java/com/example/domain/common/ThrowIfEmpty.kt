package com.example.domain.common

fun <T> List<T>.throwIfEmpty(exception: () -> Exception): List<T> {
    return this.ifEmpty { throw exception() }
}