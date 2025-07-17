package com.example.designsystem.components.guessGame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.Icon
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.theme.shapes.SkewedRectangleShape
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.designsystem.utils.modifierExtensions.autoMirroredContent

@Composable
fun GuessCard(
    points: Int,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val cornerRadius by animateDpAsState(
        targetValue = if (isHintVisible) 0.dp else 24.dp,
        animationSpec = tween(300),
    )

    Column {
        Box(
            modifier =
                modifier
                    .clip(
                        RoundedCornerShape(
                            topEnd = 24.dp,
                            topStart = 24.dp,
                            bottomStart = cornerRadius,
                            bottomEnd = cornerRadius,
                        ),
                    ).background(AppTheme.color.surface)
                    .background(
                        brush =
                            Brush.verticalGradient(
                                AppTheme.color.guessCardGradient,
                            ),
                        alpha = 0.32f,
                    ).fillMaxWidth()
                    .padding(8.dp),
        ) {
            content()
        }

        AnimatedVisibility(
            visible = isHintVisible,
            exit = fadeOut(tween(800)) + shrinkVertically(tween(600)),
        ) {
            BoxWithConstraints(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp))
                        .background(AppTheme.color.surface)
                        .fillMaxWidth()
                        .aspectRatio((336 / 32).toFloat())
                        .clickable(
                            onClick = onClick,
                        ),
            ) {
                val size = Size(maxWidth.value, maxHeight.value)

                val width = size.width / 9

                val repeatNum = size.width / 18

                repeat(repeatNum.toInt()) { count ->
                    Box(
                        modifier =
                            Modifier
                                .offset(x = (count * width).dp)
                                .clip(SkewedRectangleShape())
                                .size(width.dp, size.height.dp)
                                .background(AppTheme.color.primary.copy(alpha = 0.03f)),
                    )
                }
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.hint, points),
                        style = AppTheme.textStyle.label.small,
                        color = AppTheme.color.yellowAccent,
                        modifier = Modifier.padding(end = 2.dp),
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_user_pts),
                        contentDescription = "points",
                        tint = AppTheme.color.yellowAccent,
                        modifier =
                            Modifier
                                .size(12.dp)
                                .autoMirroredContent(),
                    )
                }
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessCardPreview() {
    AflamiTheme {
        GuessCard(
            points = 10,
            isHintVisible = true,
            onClick = {},
        ) {
        }
    }
}
