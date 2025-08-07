package com.amsterdam.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.theme.shapes.FolderShape
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.ripple
import com.amsterdam.viewmodel.shared.uiStates.UserListItemUiState

@Composable
fun ListItem(
    list: UserListItemUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val folderShape = FolderShape()
    BoxWithConstraints(
        modifier =
            modifier
                .clip(folderShape,)
                .background(AppTheme.color.surfaceHigh)
                .clipToBounds()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick
                ),
        contentAlignment = Alignment.BottomStart,
    ) {
        val size = Size(this.maxWidth.value, this.maxHeight.value)
        val dynamicBottomPadding = folderShape.getBottomPadding(size)

        Column(
            modifier =
                Modifier
                    .padding(bottom = dynamicBottomPadding)
                    .padding(horizontal = 8.dp),
        ) {
            Text(
                text = list.name,
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
            )
            Text(
                text = stringResource(R.string.list_items, list.itemCount),
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
            modifier = Modifier.size(160.dp, 147.dp),
            list = UserListItemUiState(1, "list", "description", 10),
            onClick = {}
        )
    }
}
