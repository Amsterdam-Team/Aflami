package com.example.ui.screen.search.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.designsystem.R
import com.example.designsystem.components.Chip
import com.example.designsystem.components.Icon
import com.example.designsystem.components.IconButton
import com.example.designsystem.components.Text
import com.example.designsystem.components.buttons.PrimaryButton
import com.example.designsystem.components.buttons.SecondaryButton
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onApply: () -> Unit,
    onClear: () -> Unit
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(
                    color = AppTheme.color.surface,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.filter_result),
                        color = AppTheme.color.title,
                        fontStyle = AppTheme.textStyle.title.large.fontStyle,
                        style = AppTheme.textStyle.title.large,
                        modifier = Modifier
                            .weight(1f)
                    )

                    IconButton(
                        painter = painterResource(R.drawable.ic_cancel),
                        contentDescription = null,
                        onClick = onDismiss,
                        tint = AppTheme.color.title
                    )
                }
                Text(
                    text = stringResource(R.string.imdb_rating),
                    color = AppTheme.color.title,
                    fontStyle = AppTheme.textStyle.title.small.fontStyle,
                    style = AppTheme.textStyle.title.small,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )
                RatingBar(
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                )
                Text(
                    text = stringResource(R.string.genre),
                    color = AppTheme.color.title,
                    fontStyle = AppTheme.textStyle.title.small.fontStyle,
                    style = AppTheme.textStyle.title.small,
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(10) {
                        Chip(
                            icon = painterResource(R.drawable.ic_cat_romance),
                            label = stringResource(R.string.romance),
                            isSelected = true,
                            onClick = {}
                        )
                    }
                }
                PrimaryButton(
                    title = stringResource(R.string.apply),
                    onClick = onApply,
                    isEnabled = true,
                    isLoading = false,
                    isNegative = false,
                    modifier = Modifier.padding(12.dp),
                )
                SecondaryButton(
                    title = stringResource(R.string.clear),
                    onClick = onClear,
                    isEnabled = true,
                    isLoading = false,
                    isNegative = false,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        }
    }
}

@Composable
fun RatingBar(
    modifier: Modifier = Modifier
) {
    var rating by remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier
    ) {
        repeat(10) { index ->
            val starIndex = index + 1
            Icon(
                painter = painterResource(
                    id = if (rating >= starIndex)
                        R.drawable.ic_filled_star
                    else
                        R.drawable.ic_outlined_star
                ),
                contentDescription = null,
                tint = AppTheme.color.yellowAccent,
                modifier = Modifier
                    .size(24.dp)
                    .weight(1f)
                    .clickable(
                        onClick = { rating = starIndex },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }
    }
}


@Composable
@ThemeAndLocalePreviews
fun FilterDialogPreview() {
    AflamiTheme {
        FilterDialog({}, {}, {})
    }
}
