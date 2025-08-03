package com.amsterdam.ui.screens.onBoarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.screens.onBoarding.component.PageIndicator
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme

@Composable
fun OnboardingPage(
    @DrawableRes imageResId: Int,
    @StringRes titleResId: Int,
    @StringRes descriptionResId: Int,
    pageCount: Int,
    currentPage: Int,
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

        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        0f to AppTheme.color.onBoardingGradient.first(),
                        1f to AppTheme.color.onBoardingGradient.last()
                    )
                )
                .align(Alignment.BottomStart)
                .padding(horizontal = 24.dp)
                .padding(bottom = 120.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            PageIndicator(
                pageCount = pageCount,
                currentPage = currentPage,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Text(
                text = stringResource(id = titleResId),
                style = AppTheme.textStyle.headline.small,
                color = AppTheme.color.onPrimary,
                maxLines = 2,
                minLines = 1
            )
            Text(
                text = stringResource(id = descriptionResId),
                style = AppTheme.textStyle.body.medium,
                color = AppTheme.color.onPrimaryBody,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Start,
                maxLines = 3,
                minLines = 2
            )
        }
    }
}