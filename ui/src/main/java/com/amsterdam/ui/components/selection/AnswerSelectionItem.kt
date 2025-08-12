package com.amsterdam.ui.components.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.RadioButton
import com.amsterdam.designsystem.components.RadioState
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun AnswerSelectionItem(
    modifier: Modifier = Modifier,
    text: String = "",
    status: AnswerStatus = AnswerStatus.Unselected,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = status.backgroundColor(),
                shape = shape,
            )
            .border(
                color = status.borderColor(),
                width = 1.dp,
                shape = shape,
            )
            .clip(shape)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            text = text,
            color = AppTheme.color.body,
            style = AppTheme.textStyle.label.large,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        when (status) {
            AnswerStatus.Unselected -> {
                RadioButton(
                    state = RadioState.Default,
                )
            }

            AnswerStatus.Correct, AnswerStatus.Wrong -> {
                Icon(
                    painter = painterResource(status.icon),
                    contentDescription = null,
                    tint = status.borderColor(),
                )
            }
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun LanguageSelectionItemPreview() {
    AflamiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnswerSelectionItem(
                text = stringResource(R.string.light),
                status = AnswerStatus.Unselected,
            )
            AnswerSelectionItem(
                text = stringResource(R.string.light),
                status = AnswerStatus.Correct,
            )
            AnswerSelectionItem(
                text = stringResource(R.string.light),
                status = AnswerStatus.Wrong,
            )
        }
    }
}