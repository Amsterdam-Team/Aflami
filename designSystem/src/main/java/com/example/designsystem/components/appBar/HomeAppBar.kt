package com.example.designsystem.components.appBar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.IconButton
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews


@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    onSearchClicked: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = AppTheme.color.title,
                style = AppTheme.textStyle.appName.large
            )
        },
        containerColor = containerColor,
        subTitle = {
            Text(
                text = stringResource(R.string.aflami_description),
                color = AppTheme.color.body,
                style = AppTheme.textStyle.label.small
            )
        },
        leadingIcon = {
            IconButton(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = stringResource(R.string.app_name),
                containerColor = AppTheme.color.primaryVariant,
                paddingValues = PaddingValues(horizontal = 6.dp, vertical = 9.dp),
                withBorder = true,
                isClickable = false
            )
        },
        trailingIcon = {
            IconButton(
                painter = painterResource(R.drawable.ic_search_normal),
                contentDescription = stringResource(R.string.search),
                containerColor = AppTheme.color.primaryVariant,
                tint = AppTheme.color.body,
                paddingValues = PaddingValues(8.dp),
                withBorder = true,
                onClick = onSearchClicked
            )
        }
    )
}

@ThemeAndLocalePreviews
@Composable
private fun HomeAppBarPreview() {
    AflamiTheme {
        HomeAppBar(
            containerColor = AppTheme.color.surface
        )
    }
}