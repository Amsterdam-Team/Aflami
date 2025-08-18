package com.amsterdam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R

@Composable
fun DialogTitleRow(
    title: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = AppTheme.textStyle.title.large,
            color = AppTheme.color.title,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(
            painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
            contentDescription = stringResource(R.string.cancel),
            onClick = onDismiss,
            tint = AppTheme.color.title,
        )
    }
}