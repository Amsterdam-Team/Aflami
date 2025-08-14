package com.amsterdam.ui.screens.onBoarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.screens.onBoarding.component.AnimatedSkipText
import com.amsterdam.ui.screens.onBoarding.component.NavigationButton
import com.amsterdam.viewmodel.onboarding.OnboardingEffect
import com.amsterdam.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {
    val navigationManager = LocalNavManager.current
    val state by onboardingViewModel.uiState.collectAsStateWithLifecycle()
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
        onboardingViewModel = onboardingViewModel,
        pagerState = pagerState
    )
}

@Composable
private fun OnboardingScreenContent(
    currentPageIndex: Int,
    totalPages: Int,
    onboardingViewModel: OnboardingViewModel,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize(),
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

            OnboardingPage(
                imageResId = OnboardingData.screens[page].imageResId,
                titleResId = OnboardingData.screens[page].titleResId,
                descriptionResId = OnboardingData.screens[page].descriptionResId,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        translationX = lerp(
                            start = size.width / 2f,
                            stop = 0f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    },
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentPageIndex > 0) {
                    NavigationButton(
                        icon = painterResource(id = R.drawable.ic__back_arrow),
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(currentPageIndex - 1)
                            }
                            onboardingViewModel.onPreviousPageClicked()
                        },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                NavigationButton(
                    icon = painterResource(id = R.drawable.ic_arrow_right),
                    onClick = {
                        if (currentPageIndex == totalPages - 1) {
                            onboardingViewModel.onGetStartedClicked()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(currentPageIndex + 1)
                            }
                            onboardingViewModel.onNextPageClicked()
                        }
                    }
                )
            }
        }
        AnimatedSkipText(
            isVisible = currentPageIndex < totalPages - 1,
            onClick = onboardingViewModel::onSkipClicked
        )

        PageIndicator(
            pageCount = totalPages,
            currentPage = currentPageIndex,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .align(Alignment.BottomStart)
                .padding(bottom = 300.dp, start = 12.dp)
        )
    }
}