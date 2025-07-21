package com.example.ui.screens.home.sections

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.example.designsystem.components.LoadingContainer
import com.example.ui.components.NoNetworkContainer
import com.example.viewmodel.home.HomeUiState


fun LazyListScope.homeErrorContent(
    modifier: Modifier = Modifier,
    onClickRetryLoading: () -> Unit,
    error: HomeUiState.HomeError?
) {
    error?.let {
        item {
            when (error) {
                HomeUiState.HomeError.NetworkError -> AnimatedSectionVisibility(true) {
                    NoNetworkContainer(
                        modifier = modifier.fillParentMaxSize(),
                        onClickRetry = { onClickRetryLoading() }
                    )
                }
            }
        }
    }
}

fun LazyListScope.homeLoadingContent(isLoading: Boolean, modifier: Modifier = Modifier) {
    item {
        AnimatedSectionVisibility(isLoading) {
            LoadingContainer(modifier = modifier.fillParentMaxSize())
        }
    }
}