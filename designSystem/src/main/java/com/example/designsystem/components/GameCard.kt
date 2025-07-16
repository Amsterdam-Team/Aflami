package com.example.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.designsystem.R
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.designsystem.utils.dropShadow
import com.example.designsystem.utils.mirroredContent

@Composable
fun GameCard(
    title: String,
    description: String,
    containerColor: Color,
    borderColors: List<Color>,
    onCardClick: () -> Unit,
    gameCardImageContentType: GameCardImageContentType,
    modifier: Modifier = Modifier,
    isPlayable: Boolean = true,
    onPlayClick: () -> Unit = {},
    unlockPrice: String = ""
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = borderColors.first().copy(alpha = 0.12f),
                offsetY = 12.dp,
                blur = 12.dp
            )
            .clip(RoundedCornerShape(16.dp))
            .background(color = containerColor, shape = RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(colors = borderColors),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onCardClick() },
        verticalArrangement = Arrangement.Top
    ) {
        UnlockPromptContainer(
            isVisible = !isPlayable,
            unlockPrice = unlockPrice
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = AppTheme.textStyle.title.small,
                    color = AppTheme.color.title
                )

                Text(
                    modifier = Modifier.fillMaxWidth(.7f),
                    text = description,
                    maxLines = 3,
                    style = AppTheme.textStyle.body.small,
                    color = AppTheme.color.body
                )

                CircularButton(onClick = onPlayClick, clickable = isPlayable) {
                    if (isPlayable) PlayNowContent() else PlayLockContent()
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                GameCardBackgroundShapes(circleColor = borderColors.first().copy(alpha = .25f))
                getGameCardImageContentByType(gameCardImageContentType, title).invoke(this)
            }
        }
    }
}

@Composable
private fun UnlockPromptContainer(
    isVisible: Boolean,
    unlockPrice: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier.zIndex(10f),
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppTheme.color.surfaceHigh)
                .padding(vertical = 4.dp),
            text = "$unlockPrice " + stringResource(R.string.points_to_unlock),
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.yellowAccent,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GameCardBackgroundShapes(modifier: Modifier = Modifier, circleColor: Color) {
    Box(modifier = modifier.fillMaxSize()) {

        BlurredContent(blurRadius = 50.dp, modifier = Modifier.offset(x = 16.dp, y = (-16).dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopEnd)
                    .background(color = circleColor, shape = CircleShape)
            )
        }

        val layoutDirection = LocalLayoutDirection.current
        val isRtl = remember(layoutDirection) { layoutDirection == LayoutDirection.Rtl }

        BlurredContent(
            modifier = Modifier.graphicsLayer {
                rotationZ = if (isRtl) -45f else 45f
                scaleY = 4f
                scaleX = if (isRtl) 3f else 1f
            }
        ) { RectangleShapeSoftGlow() }

        BlurredContent(
            modifier = Modifier.graphicsLayer {
                rotationZ = if (isRtl) -45f else 45f
                translationX = 220f
                scaleY = 2f
                scaleX = 1f
            }
        ) { RectangleShapeSoftGlow() }
    }
}

enum class GameCardImageContentType {
    FUN_CLOWN,
    MANY_POSTERS,
    CALENDER,
    LAWN_CHAIR
}

@Composable
private fun getGameCardImageContentByType(
    gameCardImageContentType: GameCardImageContentType,
    contentDescription: String
): @Composable BoxScope.() -> Unit {
    return {
        when (gameCardImageContentType) {
            GameCardImageContentType.FUN_CLOWN -> {
                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .fillMaxHeight(.95f)
                        .mirroredContent(LocalLayoutDirection.current)
                        .graphicsLayer { translationX = -16f },
                    painter = painterResource(R.drawable.img_game_funclown),
                    contentScale = ContentScale.Crop,
                    contentDescription = contentDescription,
                )
            }

            GameCardImageContentType.MANY_POSTERS -> RotatedPostersStack(contentDescription = contentDescription)

            GameCardImageContentType.CALENDER -> {
                Image(
                    modifier = Modifier.align(alignment = Alignment.BottomCenter),
                    painter = painterResource(R.drawable.img_game_calender),
                    contentScale = ContentScale.Crop,
                    contentDescription = contentDescription,
                )
            }

            GameCardImageContentType.LAWN_CHAIR -> {
                Image(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(end = 8.dp)
                        .mirroredContent(LocalLayoutDirection.current),
                    painter = painterResource(R.drawable.img_game_chair_with_popcorn),
                    contentScale = ContentScale.Crop,
                    contentDescription = contentDescription,
                )
            }
        }
    }
}

@Composable
private fun BoxScope.RotatedPostersStack(contentDescription: String) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(y = 12.dp)
            .padding(end = 8.dp)
            .mirroredContent(LocalLayoutDirection.current)
    ) {
        RoundedBorderedImage(
            modifier = Modifier.graphicsLayer {
                translationY = 72f
                translationX = -140f
                rotationZ = -30f
            },
            imagePainter = painterResource(R.drawable.img_poster_big_hero),
            contentDescription = contentDescription,
        )

        RoundedBorderedImage(
            modifier = Modifier.graphicsLayer {
                translationY = 24f
                translationX = -70f
                rotationZ = -15f
            },
            imagePainter = painterResource(R.drawable.img_poster_luca),
            contentDescription = contentDescription,
        )

        RoundedBorderedImage(
            imagePainter = painterResource(R.drawable.img_poster_coco),
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun RoundedBorderedImage(
    imagePainter: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = AppTheme.color.stroke),
        painter = imagePainter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun RectangleShapeSoftGlow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(20.dp)
            .fillMaxHeight()
            .background(
                shape = RectangleShape,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AppTheme.color.softBlue.copy(alpha = 0.04f),
                        AppTheme.color.softBlue.copy(alpha = 0.04f),
                    )
                )
            )
    )
}

@Composable
private fun PlayNowContent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.play_now),
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.title
        )

        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(R.drawable.ic_play_circle),
            tint = AppTheme.color.title,
            contentDescription = stringResource(R.string.play_now)
        )
    }
}

@Composable
private fun PlayLockContent(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier.size(16.dp),
        painter = painterResource(R.drawable.ic_circle_lock),
        tint = AppTheme.color.title,
        contentDescription = stringResource(R.string.play_now)
    )
}
@Composable
private fun CircularButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .background(color = AppTheme.color.onPrimaryButton, shape = CircleShape)
            .border(
                width = .5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppTheme.color.onPrimaryButton, AppTheme.color.onPrimaryButton.copy(alpha = .24f)
                    )
                ),
                shape = CircleShape
            )
            .clickable(enabled = clickable, onClick = onClick)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun BlurredContent(
    modifier: Modifier = Modifier,
    blurRadius: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .blur(radius = blurRadius, edgeTreatment = BlurredEdgeTreatment.Unbounded),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
@ThemeAndLocalePreviews
@Composable
private fun GameCardPreview() {
    GameCard(
        title = "Guess Game",
        description = "Try to guess the movie preparation time!",
        containerColor = Color.White,
        borderColors = listOf(Color.Red, Color.Blue),
        onCardClick = {},
        gameCardImageContentType = GameCardImageContentType.FUN_CLOWN,
        isPlayable = false,
        unlockPrice = "120"
    )
}
