package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.DropdownMenuItem
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun CountriesDropdownMenu(
    items: List<String>,
    onItemClicked: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(true) }
    var selectedItem by remember { mutableStateOf(items[0]) }

    val textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(animationSpec = tween(200)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .width(with(density) { textFieldSize.width.toDp() })
                .padding(top = 4.dp),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = item,
                    onClick = {
                        selectedItem = item
                        onItemClicked(item)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun CountriesDropdownMenuPreview() {
    AflamiTheme {
        CountriesDropdownMenu(
            items = listOf("item1", "item2", "item3")
        ) { }
    }
}