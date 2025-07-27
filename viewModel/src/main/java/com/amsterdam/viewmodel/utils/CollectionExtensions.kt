package com.amsterdam.viewmodel.utils

//fun <T, R, C> getMixedItemsList(
//    firstList: List<T>,
//    secondList: List<R>,
//    transformFirst: (T) -> C,
//    transformSecond: (R) -> C
//): List<C> {
//    if (firstList.isEmpty() && secondList.isEmpty()) return emptyList()
//
//    val combinedList = firstList.map(transformFirst) + secondList.map(transformSecond)
//    val combinedListBackward = secondList.map(transformSecond) + firstList.map(transformFirst)
//
//    return combinedList
//        .shuffled()
//        .takeUnless { it == combinedList || it == combinedListBackward }
//        ?: getMixedItemsList(firstList, secondList, transformFirst, transformSecond)
//}

fun <T, R, C> getMixedItemsListGuaranteed(
    firstList: List<T>,
    secondList: List<R>,
    transformFirst: (T) -> C,
    transformSecond: (R) -> C
): List<C> {
    val originalOrder = firstList.map(transformFirst) + secondList.map(transformSecond)

    if (originalOrder.size <= 1) {
        return emptyList()
    }

    var shuffledList: List<C>
    do {
        shuffledList = originalOrder.shuffled()
    } while (shuffledList == originalOrder) // Keep re-shuffling only if it matches the original

    return shuffledList
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