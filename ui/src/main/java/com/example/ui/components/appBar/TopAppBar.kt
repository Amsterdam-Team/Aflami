package com.example.ui.components.appBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Icon
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.R

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    containerColor: Color = Color.Unspecified,
    subTitle: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    middleIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = containerColor,
                ).padding(PaddingValues(vertical = 8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leadingIcon?.invoke()
            Column {
                title?.invoke()
                subTitle?.invoke()
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            middleIcon?.invoke()
            trailingIcon?.invoke()
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun TopAppBarPreview() {
    AflamiTheme {
        TopAppBar(
            title = {
                Text(text = "fire")
            },
            subTitle = {
                Text("Fire2")
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_filter_vertical),
                    contentDescription = null,
                )
            },
            middleIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_filter_vertical),
                    contentDescription = null,
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_filter_vertical),
                    contentDescription = null,
                )
            },
        )
    }
}
