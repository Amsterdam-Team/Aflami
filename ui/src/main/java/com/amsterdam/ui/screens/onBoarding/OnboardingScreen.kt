package com.amsterdam.ui.screens.onBoarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.screens.onBoarding.component.AnimatedSkipText
import com.amsterdam.ui.screens.onBoarding.component.OnboardingNavigationBar
import com.amsterdam.viewmodel.onboarding.OnboardingEffect
import com.amsterdam.viewmodel.onboarding.OnboardingInteractionListener
import com.amsterdam.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue

@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val navigationManager = LocalNavManager.current
    val state by onboardingViewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        initialPage = state.currentPageIndex,
        pageCount = { OnboardingData.screens.size })

    LaunchedEffect(Unit) {
        onboardingViewModel.effect.collectLatest { effect ->
            when (effect) {
                is OnboardingEffect.NavigateToLoginScreen -> {
                    navigationManager.toLogin()
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (state.currentPageIndex != pagerState.currentPage) {
            onboardingViewModel.setCurrentPage(pagerState.currentPage)
        }
    }

    LaunchedEffect(state.currentPageIndex) {
        pagerState.animateScrollToPage(state.currentPageIndex)
    }

    OnboardingScreenContent(
        currentPageIndex = state.currentPageIndex,
        totalPages = state.totalPages,
        interactionListener = onboardingViewModel,
        pagerState = pagerState
    )
}

@Composable
private fun OnboardingScreenContent(
    currentPageIndex: Int,
    totalPages: Int,
    interactionListener: OnboardingInteractionListener,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var indicatorYOffset = remember { mutableIntStateOf(-100) }
    val layoutDirection = LocalLayoutDirection.current
    val directionMultiplier = if (layoutDirection == LayoutDirection.Rtl) -1 else 1
    Box(
        modifier = modifier.fillMaxSize().navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true
        ) { page ->
            val pageOffset = (
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    ).absoluteValue
            val density = LocalDensity.current

            OnboardingPage(
                imageResId = OnboardingData.screens[page].imageResId,
                titleResId = OnboardingData.screens[page].titleResId,
                descriptionResId = OnboardingData.screens[page].descriptionResId,
                titleCoordinates = {
                    if (indicatorYOffset.intValue != -100) return@OnboardingPage
                    with(density) {
                        indicatorYOffset.intValue = it.positionInWindow().y.toDp().value.toInt()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        translationX = lerp(
                            start = (size.width / 2f) * directionMultiplier,
                            stop = 0f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
            )
        }

        OnboardingNavigationBar(
            currentPageIndex = currentPageIndex,
            totalPages = totalPages,
            pagerState = pagerState,
            interactionListener = interactionListener,
            coroutineScope = coroutineScope
        )

        AnimatedSkipText(
            isVisible = currentPageIndex < totalPages - 1,
            onClick = interactionListener::onSkipClicked
        )
        AnimatedVisibility(
            visible = indicatorYOffset.intValue != -100,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp)
                .offset(y = indicatorYOffset.intValue.dp - 24.dp)
        ) {

            PageIndicator(
                pageCount = totalPages,
                currentPage = currentPageIndex,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
            )
        }
    }
}