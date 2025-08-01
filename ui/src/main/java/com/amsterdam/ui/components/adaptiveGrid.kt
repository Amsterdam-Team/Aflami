package com.amsterdam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun <T> LazyListScope.adaptiveGrid(
    deviceWidth: Int,
    items: List<T>,
    itemMinWidth: Int,
    modifier: Modifier = Modifier,
    itemsHorizontalPadding: Dp = 8.dp,
    itemsVerticalPadding: Dp = 8.dp,
    cardContent: @Composable RowScope.(T) -> Unit
) {
    var chunks = deviceWidth / itemMinWidth
    if (chunks == 0) chunks = 1

    item {
        Column(
            modifier = modifier
        ) {
            items.chunked(chunks).forEachIndexed { index, rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = itemsVerticalPadding),
                    horizontalArrangement = Arrangement.spacedBy(itemsHorizontalPadding)
                ) {
                    rowItems.forEach { item ->
                        key("$index$item") {
                            cardContent(item)
                        }
                    }
                    repeat(chunks - rowItems.size) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }

            }
        }
    }
}