package com.example.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews


@Composable
fun IconButton(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    paddingValues: PaddingValues = PaddingValues(10.dp),
    withBorder: Boolean = false,
    containerColor: Color = AppTheme.color.surfaceHigh,
    tint: Color = Color.Unspecified,
    shape: Shape = RoundedCornerShape(12.dp),
    isClickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    val borderModifier = if (withBorder) {
        Modifier.border(width = 1.dp, color = AppTheme.color.stroke, shape = shape)
    } else {
        Modifier
    }

    val clickableModifier = if (isClickable) {
        Modifier
            .clip(shape)
            .clickable(onClick = onClick)
    } else {
        Modifier
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(iconSize)
            .background(
                shape = shape,
                color = containerColor,
            )
            .then(borderModifier)
            .then(clickableModifier)
    ) {
        Icon(
            painter = painter,
            tint = tint,
            contentDescription = contentDescription,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun SearchIconPreview() {
    AflamiTheme {
        IconButton(
            painter = painterResource(R.drawable.ic_search_normal),
            contentDescription = stringResource(R.string.search),
            containerColor = AppTheme.color.primaryVariant,
            tint = AppTheme.color.body,
            withBorder = true,
            paddingValues = PaddingValues(8.dp),
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun BackIconPreview() {
    AflamiTheme {
        IconButton(
            painter = painterResource(R.drawable.ic__back_arrow),
            contentDescription = stringResource(R.string.search),
            containerColor = AppTheme.color.primaryVariant,
            tint = AppTheme.color.body,
            withBorder = true,
            paddingValues = PaddingValues(8.dp),
            modifier = Modifier.padding(8.dp)
        )
    }
}