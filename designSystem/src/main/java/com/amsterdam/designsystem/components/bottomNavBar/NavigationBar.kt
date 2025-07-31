package com.amsterdam.designsystem.components.bottomNavBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBar as MaterialNavigationBar

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    content: @Composable() (RowScope.() -> Unit)
) {
    MaterialNavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        content = content
    )
}
