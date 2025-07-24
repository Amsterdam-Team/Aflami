package com.example.viewmodel.utils

fun <T, R, C> getMixedMediaItemsList(
        listA: List<T>,
        listB: List<R>,
        transformA: (T) -> C,
        transformB: (R) -> C,
    ): List<C> {
        val combinedList = listA.map(transformA) + listB.map(transformB)
        val combinedListBackward = listB.map(transformB) + listA.map(transformA)

        return combinedList
            .shuffled()
            .takeUnless { it == combinedListBackward || it == combinedListBackward }
            ?: getMixedMediaItemsList(listA, listB, transformA, transformB)
    }