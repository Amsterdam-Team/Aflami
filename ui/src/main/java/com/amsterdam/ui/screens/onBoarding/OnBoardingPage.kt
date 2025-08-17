package com.amsterdam.ui.screens.onBoarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun OnboardingPage(
    @DrawableRes imageResId: Int,
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
    titleCoordinates: (LayoutCoordinates) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AppTheme.color.onBoardingGradient.first().copy(alpha = 0.5f),
                            AppTheme.color.onBoardingGradient.last()
                        )
                    )
                )
        )

        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(bottom = if (isLandscape) 64.dp else 80.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 24.dp)
                    .width(345.dp)
            ) {
                Text(
                    text = stringResource(id = titleResId),
                    style = AppTheme.textStyle.headline.small,
                    color = AppTheme.color.onPrimary,
                    fontSize = 20.sp,
                    maxLines = 1,
                    minLines = 1,
                    modifier = Modifier.onGloballyPositioned(titleCoordinates)
                )
                Text(
                    text = stringResource(id = descriptionResId),
                    style = AppTheme.textStyle.body.medium,
                    color = AppTheme.color.onPrimaryBody,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 12.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 3,
                    minLines = 3
                )
            }
        }
    }
}