package com.example.designsystem.components.bottomNavBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun BottomNavBar(
    items: Map<BottomBarItems, Any>,
    selectedBottomBarItems: BottomBarItems,
    modifier: Modifier = Modifier,
    onDestinationClicked: (destination: Any) -> Unit = {},
) {
    NavigationBar(
        modifier = modifier,
        containerColor = AppTheme.color.surface
    ) {
        items.entries.forEach { destination ->
            val isSelected = selectedBottomBarItems == destination.key
            val labelColor by animateColorAsState(targetValue = if (isSelected) AppTheme.color.body else AppTheme.color.hint)
            val iconColor by animateColorAsState(targetValue = if (isSelected) AppTheme.color.primary else AppTheme.color.hint)
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onDestinationClicked(destination.value)
                },
                label = {
                    Text(
                        text = stringResource(destination.key.label),
                        color = labelColor,
                        style = AppTheme.textStyle.label.small
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.key.icon),
                        contentDescription = null,
                        tint = iconColor
                    )
                }, colors = NavigationBarItemDefaults.colors(
                    indicatorColor = AppTheme.color.primaryVariant
                )
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun BottomNavBarPreview() {
    AflamiTheme {
        BottomNavBar(
            items = mapOf(
                BottomBarItems.HOME to "home",
                BottomBarItems.LISTS to "lists",
                BottomBarItems.CATEGORIES to "categories",
                BottomBarItems.LETS_PLAY to "letsPlay",
                BottomBarItems.PROFILE to "profile"
            ),
            selectedBottomBarItems = BottomBarItems.HOME
        )
    }
}