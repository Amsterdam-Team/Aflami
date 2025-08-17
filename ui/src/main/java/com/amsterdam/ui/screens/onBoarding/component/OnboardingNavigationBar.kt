package com.amsterdam.ui.screens.onBoarding.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.viewmodel.onboarding.OnboardingInteractionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BoxScope.OnboardingNavigationBar(
    currentPageIndex: Int,
    totalPages: Int,
    pagerState: PagerState,
    interactionListener: OnboardingInteractionListener,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 24.dp,end = 24.dp, bottom = 16.dp,top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val nextButtonTargetOffsetX by animateDpAsState(
                targetValue = if (currentPageIndex > 0) 38.dp else 0.dp,
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                label = ""
            )
            val backButtonTargetOffsetX by animateDpAsState(
                targetValue = if (currentPageIndex > 0) -38.dp else 72.dp,
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                label = ""
            )
            val backButtonAlpha by animateFloatAsState(
                targetValue = if (currentPageIndex > 0) 1f else 0f,
                animationSpec = tween(600, easing = FastOutSlowInEasing),
                label = ""
            )

            NavigationButton(
                icon = painterResource(id = R.drawable.ic__back_arrow),
                onClick = {
                    if (currentPageIndex == 0) return@NavigationButton
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(currentPageIndex - 1)
                    }
                    interactionListener.onPreviousPageClicked()
                },
                modifier = Modifier
                    .offset(x = backButtonTargetOffsetX)
                    .graphicsLayer {
                        alpha = backButtonAlpha
                    }
            )

            NavigationButton(
                icon = painterResource(id = R.drawable.ic_arrow_right),
                onClick = {
                    if (currentPageIndex == totalPages - 1) {
                        interactionListener.onGetStartedClicked()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(currentPageIndex + 1)
                        }
                        interactionListener.onNextPageClicked()
                    }
                },
                modifier = Modifier.offset(x = nextButtonTargetOffsetX)
            )
        }
    }
}