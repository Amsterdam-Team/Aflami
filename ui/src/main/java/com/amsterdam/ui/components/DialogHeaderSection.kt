package com.amsterdam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun DialogHeaderSection(
    title: String,
    onDismiss: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = AppTheme.textStyle.title.large,
            color = AppTheme.color.title,
        )

        IconButton(
            painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
            contentDescription = null,
            tint = AppTheme.color.title,
            onClick = onDismiss,
        )
    }
}
