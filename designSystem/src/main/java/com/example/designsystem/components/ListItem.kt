package com.example.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.shapes.FolderShape
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun ListItem(
    title: String,
    count: Int,
    modifier: Modifier = Modifier,
) {
    val folderShape = FolderShape()
    BoxWithConstraints(
        modifier =
            modifier
                .clip(
                    folderShape,
                ).background(AppTheme.color.surfaceHigh)
                .clipToBounds(),
        contentAlignment = Alignment.BottomStart,
    ) {
        val size = Size(maxWidth.value, maxHeight.value)
        val dynamicBottomPadding = folderShape.getBottomPadding(size)

        Column(
            modifier =
                Modifier
                    .padding(bottom = dynamicBottomPadding)
                    .padding(horizontal = 8.dp),
        ) {
            Text(
                text = title,
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
            )
            Text(
                text = stringResource(R.string.list_items, count),
                style = AppTheme.textStyle.label.large,
                color = AppTheme.color.hint,
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListItemPreview() {
    AflamiTheme {
        ListItem(
            title = "My Favourites",
            count = 12,
            modifier = Modifier.size(160.dp, 147.dp),
        )
    }
}
