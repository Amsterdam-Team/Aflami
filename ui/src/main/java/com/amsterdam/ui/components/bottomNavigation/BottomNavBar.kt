package com.amsterdam.ui.components.bottomNavigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.bottomNavBar.NavigationBar
import com.amsterdam.designsystem.components.bottomNavBar.NavigationBarItem
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.navigation.Route

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    items: List<BottomBarItems> = BottomBarItems.entries,
    selectedBottomBarItems: BottomBarItems = BottomBarItems.HOME,
    onDestinationClicked: (destination: Route) -> Unit = {},
    ) {
    NavigationBar(
        modifier = modifier,
        containerColor = AppTheme.color.surface,
    ) {
        items.forEach { destination ->
            val isSelected = selectedBottomBarItems == destination
            val labelColor by animateColorAsState(
                targetValue = if (isSelected) AppTheme.color.body else AppTheme.color.hint,
            )
            val iconColor by animateColorAsState(
                targetValue = if (isSelected) AppTheme.color.primary else AppTheme.color.hint,
            )
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onDestinationClicked(destination.route)
                },
                label = {
                    Text(
                        text = stringResource(destination.label),
                        color = labelColor,
                        style = AppTheme.textStyle.label.small,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = null,
                        tint = iconColor,
                    )
                },
                indicatorColor = AppTheme.color.primaryVariant,
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun BottomNavBarPreview() {
    AflamiTheme {
        BottomNavBar(

        )
    }
}
