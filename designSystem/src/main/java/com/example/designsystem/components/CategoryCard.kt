package com.example.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    categoryName: String,
    categoryImage: Painter,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.size(width = 160.dp, height = 71.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = AppTheme.color.surfaceHigh,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
        )
        Row(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                modifier
                = Modifier
                    .padding(top = 8.dp)
                    .weight(1f),
                style = AppTheme.textStyle.label.medium,
                text = categoryName,
                color = AppTheme.color.title,
            )

            Image(
                modifier = Modifier
                    .size(64.dp, 71.dp)
                    .offset(y = (-8).dp),
                painter = categoryImage,
                contentDescription = null
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun CategoryCardPreview() {
    AflamiTheme {
        CategoryCard(
            categoryName = stringResource(R.string.family),
            categoryImage = painterResource(id = R.drawable.img_action)
        )
    }
}
