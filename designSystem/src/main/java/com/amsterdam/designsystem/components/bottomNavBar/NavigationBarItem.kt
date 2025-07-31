package com.amsterdam.designsystem.components.bottomNavBar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBarItem as MaterialNavigationBarItem


@Composable
fun RowScope.NavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable() (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    indicatorColor: Color = Color.Unspecified,
    interactionSource: MutableInteractionSource? = null
) {
    MaterialNavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(indicatorColor = indicatorColor),
        interactionSource = interactionSource
    )
}