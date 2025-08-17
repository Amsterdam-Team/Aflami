package com.amsterdam.ui.components.appBar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    onSearchClicked: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.aflami_title),
                color = AppTheme.color.title,
                style = AppTheme.textStyle.appName.large,
            )
        },
        containerColor = containerColor,
        subTitle = {
            Text(
                modifier = Modifier.fillMaxWidth(0.85f),
                text = stringResource(R.string.aflami_description),
                color = AppTheme.color.body,
                style = AppTheme.textStyle.label.small,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = {
            IconButton(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = stringResource(R.string.app_name),
                containerColor = AppTheme.color.primaryVariant,
                paddingValues = PaddingValues(horizontal = 6.dp, vertical = 9.dp),
                withBorder = true,
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
                onClick = onSearchClicked,
            )
        },
    )
}

@ThemeAndLocalePreviews
@Composable
private fun HomeAppBarPreview() {
    AflamiTheme {
        HomeAppBar(
            containerColor = AppTheme.color.surface,
        )
    }
}
