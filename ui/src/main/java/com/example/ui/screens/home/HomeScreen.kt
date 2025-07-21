package com.example.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.application.LocalNavController
import com.example.ui.components.NoNetworkContainer
import com.example.ui.components.appBar.HomeAppBar
import com.example.ui.navigation.Route
import com.example.ui.screens.home.component.PopularSection
import com.example.viewmodel.home.HomeEffect
import com.example.viewmodel.home.HomeInteractionListener
import com.example.viewmodel.home.HomeUiState
import com.example.viewmodel.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val state = homeViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        homeViewModel.effect.collectLatest {
            it?.let {
                when (it) {
                    HomeEffect.NavigateToSearchScreenEffect ->
                        navController.navigate(Route.Search)
                }
            }
        }
    }

    HomeScreenContent(
        modifier = modifier,
        state = state.value,
        interactionListener = homeViewModel
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    interactionListener: HomeInteractionListener
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
        },
        label = "Content Animation"
    ) { state ->
        when {
            state.isLoading ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingContainer(modifier = Modifier)
                }

            state.error != null ->
                when(state.error!!){
                    HomeUiState.HomeError.NetworkError ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            NoNetworkContainer(
                                onClickRetry = interactionListener::onClickRetryLoading
                            )
                        }
                }

            state.popularMovies.isNotEmpty() ->
                LazyColumn(
                    modifier =
                        modifier
                            .fillMaxSize()
                            .background(AppTheme.color.surface),
                ) {
                    stickyHeader {
                        HomeAppBar(
                            onSearchClicked = interactionListener::onClickSearch,
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp),
                        )
                    }
                    PopularSection(state.popularMovies)
                }

                }
    }

}


@ThemeAndLocalePreviews
@Composable
private fun HomeScreenPreview() {
    AflamiTheme {
        HomeScreenContent(state = HomeUiState(),
            interactionListener = object : HomeInteractionListener{
                override fun onClickRetryLoading() {
                }

                override fun onClickSearch() {
                }
            }
        )
    }
}
