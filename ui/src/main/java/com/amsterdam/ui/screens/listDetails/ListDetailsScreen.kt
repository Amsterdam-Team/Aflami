package com.amsterdam.ui.screens.listDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.screens.listDetails.component.DeleteListDialog
import com.amsterdam.ui.screens.listDetails.component.MoviesItemsGrid
import com.amsterdam.ui.screens.listDetails.component.getListDetailsErrorMessage
import com.amsterdam.viewmodel.listDetails.ListDetailsEffect
import com.amsterdam.viewmodel.listDetails.ListDetailsInteractionListener
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState
import com.amsterdam.viewmodel.listDetails.ListDetailsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ListsDetailsScreen(viewModel: ListDetailsViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ListDetailsEffect.NavigateToMovieDetailsScreen -> {
                    navController.navigate(Route.MovieDetails(effect.movieId))
                }

                ListDetailsEffect.NavigateBack -> navController.navigateUp()

                ListDetailsEffect.ShowDeletionSuccessSnackBar -> {
                    SnackBarManager.showSuccess(
                        context.getString(R.string.list_deleted_successfully)
                    )
                }

                ListDetailsEffect.ShowRemoveMovieSuccessSnackBar -> {
                    SnackBarManager.showSuccess(context.getString(R.string.movie_deleted_successfully))
                }

                is ListDetailsEffect.ShowErrorSnackbar -> {
                    SnackBarManager.showError(
                        getListDetailsErrorMessage(state.error, context)
                    )
                }
            }
        }
    }

    ListDetailsContent(
        state = state,
        listener = viewModel
    )
}

@Composable
private fun ListDetailsContent(
    state: ListDetailsUiState,
    listener: ListDetailsInteractionListener
) {

    val movies = state.listItems.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        DefaultAppBar(
            title = state.listName,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            lastOption = painterResource(com.amsterdam.designsystem.R.drawable.ic_delete),
            lastOptionIconTint = AppTheme.color.redAccent,
            onLastOptionClicked = listener::onClickDeleteList,
            onNavigateBackClicked = listener::onClickBack,
        )

        AnimatedSectionVisibility(
            visible = state.isLoading
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingContainer()
            }
        }

        AnimatedSectionVisibility(
            visible = movies.itemCount == 0
                    && movies.loadState.refresh is LoadState.NotLoading
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NoDataContainer(
                    imageRes = painterResource(R.drawable.no_saved_items),
                    title = stringResource(R.string.no_saved_items_here)
                )
            }
        }

        AnimatedSectionVisibility(
            visible = movies.loadState.refresh is LoadState.Error
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NoNetworkContainer(
                    onClickRetry = listener::onClickRetryLoading
                )
            }
        }

        AnimatedSectionVisibility(
            visible = movies.itemCount > 0
        ) {
            MoviesItemsGrid(
                movies = movies,
                modifier = Modifier.weight(1f),
                onClickMovie = listener::onClickMovie,
                onClickRemoveItem = listener::onClickRemoveMovie
            )
        }
    }

    AnimatedSectionVisibility(
        visible = state.showDeleteListDialog
    ) {
        DeleteListDialog(
            isLoading = state.isDeleteLoading,
            onDismiss = listener::onDeleteListDialogDismiss,
            onConfirm = listener::onDeleteListConfirmed
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListDetailsScreenPreview() {
    AflamiTheme {
        ListDetailsContent(
            state = ListDetailsUiState(),
            listener = object : ListDetailsInteractionListener {
                override fun onClickBack() {}
                override fun onClickRetryLoading() {}
                override fun onClickMovie(movieId: Long) {}
                override fun onClickDeleteList() {}
                override fun onDeleteListConfirmed() {}
                override fun onDeleteListDialogDismiss() {}
                override fun onClickRemoveMovie(movieId: Long) {}
            }
        )
    }
}
