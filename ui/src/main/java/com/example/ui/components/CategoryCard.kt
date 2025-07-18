package com.example.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.designsystem.utils.ripple

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun CategoryCard(
    categoryName: String,
    categoryImage: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.size(width = 160.dp, height = 71.dp),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    color = AppTheme.color.surfaceHigh,
                    shape = RoundedCornerShape(16.dp),
                )
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp),
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = ripple(color = AppTheme.color.hint),
                    onClick = onClick,
                ),
        )
        Row(
            modifier =
                Modifier
                    .padding(start = 8.dp)
                    .fillMaxSize(),
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(top = 8.dp)
                        .weight(1f),
                style = AppTheme.textStyle.label.medium,
                text = categoryName,
                color = AppTheme.color.title,
            )

            Image(
                modifier =
                    Modifier
                        .size(64.dp, 71.dp)
                        .offset(y = (-8).dp),
                painter = categoryImage,
                contentDescription = null,
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
            categoryImage = painterResource(id = R.drawable.img_action),
        )
    }
}
