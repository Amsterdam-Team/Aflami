package com.amsterdam.ui.components

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun DottedSeparatedRow(
    vararg parts: String,
    modifier: Modifier = Modifier,
    dotSize: Dp = 4.dp,
    dotSpacing: Dp = 8.dp,
    textStyle: TextStyle = AppTheme.textStyle.label.small,
    textColor: Color = AppTheme.color.hint
) {
    val items = parts.filter { it.isNotBlank() }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, label ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(dotSpacing))
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .background(AppTheme.color.stroke, shape = CircleShape)
                )
                Spacer(Modifier.width(dotSpacing))
            }

            Text(
                text = label,
                style = textStyle,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}