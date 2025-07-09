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
    modifier: Modifier = Modifier,
    selectedDestination: Destination = Destination.HOME,
    onDestinationClicked: (destination: Destination) -> Unit = {},
) {
    NavigationBar(
        modifier = modifier,
        containerColor = AppTheme.color.surface
    ) {
        Destination.entries.forEach { destination ->
            val isSelected = selectedDestination == destination
            val labelColor by animateColorAsState(targetValue = if (isSelected) AppTheme.color.body else AppTheme.color.hint)
            val iconColor by animateColorAsState(targetValue = if (isSelected) AppTheme.color.primary else AppTheme.color.hint)
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onDestinationClicked(destination)
                },
                label = {
                    Text(
                        text = stringResource(destination.label),
                        color = labelColor,
                        style = AppTheme.textStyle.label.small
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = null,
                        tint = iconColor
                    )
                }, colors = NavigationBarItemDefaults.colors(
                    indicatorColor = AppTheme.color.primaryVariant
                ))
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun BottomNavBarPreview() {
    AflamiTheme {
            BottomNavBar()
    }
}