package com.amsterdam.ui.screens.listDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.listDetails.component.DeleteListDialog
import com.amsterdam.ui.screens.listDetails.component.ListDetailsItemsGrid
import com.amsterdam.ui.screens.listDetails.component.getListDetailsErrorMessage
import com.amsterdam.viewmodel.listDetails.ListDetailsEffect
import com.amsterdam.viewmodel.listDetails.ListDetailsInteractionListener
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState
import com.amsterdam.viewmodel.listDetails.ListDetailsViewModel
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ListsDetailsScreen(viewModel: ListDetailsViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val navigationManager = LocalNavManager.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val movies = state.listItems.collectAsLazyPagingItems()
    LaunchedEffect(movies.loadState) {
        viewModel.onPagingLoadStateChanged(movies.loadState)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ListDetailsEffect.NavigateToMovieDetailsScreen -> {
                    navigationManager.toMovieDetails(effect.movieId)
                }

                is ListDetailsEffect.NavigateToTvShowDetailsScreen -> {
                    navigationManager.toSeriesDetails(effect.tvShowId)
                }

                ListDetailsEffect.NavigateBack -> navigationManager.navigateUp()

                ListDetailsEffect.ShowDeletionSuccessSnackBar -> {
                    SnackBarManager.showSuccess(
                        context.getString(R.string.list_deleted_successfully)
                    )
                }

                ListDetailsEffect.ShowRemoveMovieSuccessSnackBar -> {
                    SnackBarManager.showSuccess(context.getString(R.string.movie_deleted_successfully))
                }

                is ListDetailsEffect.ShowErrorSnackBar -> {
                    SnackBarManager.showError(getListDetailsErrorMessage(errorState, context))
                }

            }
        }
    }

    ListDetailsContent(
        state = state,
        errorState = errorState,
        listener = viewModel
    )
}

@Composable
private fun ListDetailsContent(
    state: ListDetailsUiState,
    errorState: ErrorUiState?,
    listener: ListDetailsInteractionListener
) {
    val density = LocalDensity.current
    val listMediaItems = state.listItems.collectAsLazyPagingItems()
    var headerHeight by remember { mutableStateOf(0.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val maxTitleLength = 25
        val shortenedTitle = if (state.listName.length > maxTitleLength) {
            state.listName.take(maxTitleLength) + "…"
        } else {
            state.listName
        }
        DefaultAppBar(
            title = shortenedTitle,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { headerHeight = with(density) { it.height.dp } }
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            lastOption = painterResource(com.amsterdam.designsystem.R.drawable.ic_delete),
            lastOptionIconTint = AppTheme.color.redAccent,
            onLastOptionClicked = listener::onClickDeleteList,
            onNavigateBackClicked = listener::onClickBack,
        )

        val gridState = rememberLazyGridState()
        val isScrolled by remember {
            derivedStateOf {
                gridState.firstVisibleItemIndex > 0 ||
                        gridState.firstVisibleItemScrollOffset > 0
            }
        }

        if (isScrolled) {
            HorizontalDivider(color = AppTheme.color.stroke)
        }

        with(state) {
            when {
                isLoading && listMediaItems.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingContainer()
                    }
                }

                !isLoading && errorState is ErrorUiState.NoInternetError && listMediaItems.itemCount == 0-> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        NoNetworkContainer(
                            onClickRetry = listener::onClickRetryLoading
                        )
                    }
                }

                !isLoading && listMediaItems.itemCount == 0 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center
                    ) {
                        CenterOfScreenContainer(
                            unneededSpace = headerHeight
                        ) {
                            NoDataContainer(
                                modifier = Modifier.padding(top = 12.dp),
                                imageRes = painterResource(R.drawable.no_saved_items),
                                title = stringResource(R.string.no_saved_items_here),
                                imageAlpha = 0.68f
                            )
                        }
                    }
                }

                else -> {
                    ListDetailsItemsGrid(
                        listMediaItems = listMediaItems,
                        gridState = gridState,
                        modifier = Modifier.weight(1f),
                        onClickMovie = listener::onClickMovie,
                        onClickTvShow = listener::onClickTvShow,
                        onClickRemoveItem = listener::onClickRemoveMovie
                    )
                }
            }
        }

        if(state.showDeleteListDialog){
            DeleteListDialog(
                isLoading = state.isDeleteLoading,
                onDismiss = listener::onDeleteListDialogDismiss,
                onConfirm = listener::onDeleteListConfirmed
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListDetailsScreenPreview() {
    AflamiTheme {
        ListDetailsContent(
            state = ListDetailsUiState(),
            errorState = null,
            listener = object : ListDetailsInteractionListener {
                override fun onClickBack() {}
                override fun onClickRetryLoading() {}
                override fun onClickMovie(movieId: Long) {}
                override fun onClickTvShow(tvShowId: Long) {}
                override fun onClickDeleteList() {}
                override fun onDeleteListConfirmed() {}
                override fun onDeleteListDialogDismiss() {}
                override fun onClickRemoveMovie(movieId: Long) {}
                override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {}
            }
        )
    }
}
