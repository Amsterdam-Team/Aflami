package com.amsterdam.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AdaptiveGrid(
    items: List<Any>,
    itemMinWidth: Int,
    modifier: Modifier = Modifier,
    itemsHorizontalPadding: Dp = 8.dp,
    itemsVerticalPadding: Dp = 8.dp,
    cardContent: @Composable RowScope.(Any) -> Unit
) {
    val deviceWidth = LocalConfiguration.current.screenWidthDp
    var chunks by remember { mutableIntStateOf(deviceWidth / itemMinWidth) }
    if (chunks == 0) chunks = 1

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        items.chunked(chunks).forEach { gallery ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = itemsVerticalPadding),
                horizontalArrangement = Arrangement.spacedBy(itemsHorizontalPadding)
            ) {
                gallery.forEach { item ->
                    cardContent(item)
                }
                (1..(chunks - gallery.size)).forEach { _ ->
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}