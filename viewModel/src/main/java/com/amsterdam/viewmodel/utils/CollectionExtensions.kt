package com.amsterdam.viewmodel.utils

import kotlin.random.Random

fun <T, R, C> getMixedItemsList(
    firstList: List<T>,
    secondList: List<R>,
    transformFirst: (T) -> C,
    transformSecond: (R) -> C,
    seed: Long = 12345L
): List<C> {
    if (firstList.isEmpty() && secondList.isEmpty()) return emptyList()

    val combinedList = firstList.map(transformFirst) + secondList.map(transformSecond)
    val combinedListBackward = secondList.map(transformSecond) + firstList.map(transformFirst)

    return combinedList
        .shuffled(Random(seed))
        .takeUnless { it == combinedList || it == combinedListBackward }
        ?: getMixedItemsList(firstList, secondList, transformFirst, transformSecond)
}

fun <T, R, C> getLinearItemsList(
    firstList: List<T>,
    secondList: List<R>,
    transformFirst: (T) -> C,
    transformSecond: (R) -> C,
): List<C> {
    if (firstList.isEmpty() && secondList.isEmpty()) return emptyList()
    return firstList.map(transformFirst) + secondList.map(transformSecond)
}