package com.amsterdam.ui.screens.lists

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.application.LocalScaffoldBottomPadding
import com.amsterdam.ui.components.CreateNewListDialog
import com.amsterdam.ui.screens.lists.components.ListItem
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.profile.components.NotLoggedInContent
import com.amsterdam.viewmodel.lists.ListsEffect
import com.amsterdam.viewmodel.lists.ListsInteractionListener
import com.amsterdam.viewmodel.lists.ListsUiState
import com.amsterdam.viewmodel.lists.WishListsViewModel
import com.amsterdam.viewmodel.shared.uiStates.WishListItemUiState

@Composable
fun ListsScreen(
    modifier: Modifier = Modifier,
    viewModel: WishListsViewModel = hiltViewModel(),
) {
    val navigationManager = LocalNavManager.current
    val uiState = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadCustomLists(startLoading = false)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ListsEffect.NavigateToListDetails -> {
                    navigationManager.toListDetails(listId = effect.listId, listName = effect.listName)
                }

                ListsEffect.FailedToCreateList -> {
                    SnackBarManager
                        .showError(
                            message = context.resources.getString(R.string.general_error_message),
                        )
                }

                ListsEffect.ListCreatedSuccessfully -> {
                    SnackBarManager
                        .showSuccess(
                            message = context.resources.getString(R.string.list_added_success_message),
                        )
                }

                ListsEffect.NavigateToLogin -> {
                    navigationManager.toLogin()
                }
            }
        }
    }

    ListsScreenContent(
        modifier = modifier,
        state = uiState.value,
        interaction = viewModel,
    )
}

@Composable
private fun ListsScreenContent(
    modifier: Modifier = Modifier,
    state: ListsUiState,
    interaction: ListsInteractionListener,
) {
    val animationDuration by remember { mutableIntStateOf(1000) }
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(AppTheme.color.surface)
                .navigationBarsPadding()
                .windowInsetsPadding(WindowInsets(bottom = LocalScaffoldBottomPadding.current)),
    ) {
        AnimatedVisibility(
            modifier = Modifier,
            visible = state.isCreateNewListDialogVisible,
        ) {
            CreateNewListDialog(
                isCreateListLoading = state.isCreateListLoading,
                listName = state.listName,
                onListNameChange = interaction::onListNameChange,
                onCreateListClick = interaction::onCreateNewListClick,
                onDismiss = interaction::onDismiss,
            )
        }

        AnimatedVisibility(
            visible = !state.isUserLoggedIn && !state.isLoading,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            NotLoggedInContent(
                stringResource(com.amsterdam.ui.R.string.lists),
                interaction::onNavigateToLoginClicked,
            )
        }

        AnimatedVisibility(
            state.isUserLoggedIn,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(color = AppTheme.color.surface)
                        .statusBarsPadding(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DefaultAppBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    showNavigateBackButton = false,
                    title = stringResource(com.amsterdam.ui.R.string.lists),
                    lastOption = painterResource(com.amsterdam.designsystem.R.drawable.ic_add),
                    onLastOptionClicked = interaction::onClickAddList,
                )

                AnimatedContent(
                    modifier =
                        Modifier
                            .fillMaxSize(),
                    targetState = Triple(state.isLoading, state.errorUiState, state.userLists),
                    transitionSpec = {
                        fadeIn(tween(700)) togetherWith fadeOut(tween(700))
                    },
                ) { (isLoading, errorState, lists) ->

                    when {
                        isLoading -> {
                            LoadingContainer()
                        }

                        errorState == ListsUiState.ListsErrorState.NoNetworkConnection -> {
                            NoNetworkContainer(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                                    onClickRetry = interaction::onClickRetryFetchList,
                            )
                        }

                        lists.isEmpty() -> {
                            NoDataContainer(
                                modifier = Modifier.fillMaxSize(),
                                title = stringResource(com.amsterdam.ui.R.string.no_list_yet),
                                description = stringResource(com.amsterdam.ui.R.string.no_list_description),
                                imageRes = painterResource(id = com.amsterdam.ui.R.drawable.placeholder_no_saved_items),
                                imageAlpha = 0.64f,
                            )
                        }

                        else -> {
                            LazyVerticalGrid(
                                modifier =
                                    Modifier
                                        .fillMaxSize(),
                                columns = GridCells.Adaptive(minSize = 156.dp),
                                state = rememberLazyGridState(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            ) {
                                items(lists) { userList ->
                                    ListItem(
                                        modifier = Modifier.size(156.dp, 147.dp),
                                        list = userList,
                                        onClick = {
                                            interaction.onListClick(
                                                listId = userList.id.toLong(),
                                                listName = userList.name.trim(),
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListsScreenPreview_Loading() {
    AflamiTheme {
        ListsScreenContent(
            state = ListsUiState(isLoading = true),
            interaction =
                object : ListsInteractionListener {
                    override fun onClickAddList() {
                    }

                    override fun onListNameChange(listName: String) {
                    }

                    override fun onCreateNewListClick() {
                    }

                    override fun onListClick(
                        listId: Long,
                        listName: String,
                    ) {
                    }

                    override fun onClickRetryFetchList() {}

                    override fun onDismiss() {
                    }

                    override fun onNavigateToLoginClicked() {
                    }
                },
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListsScreenPreview_Empty() {
    AflamiTheme {
        ListsScreenContent(
            state =
                ListsUiState(
                    isLoading = false,
                    userLists = emptyList(),
                ),
            interaction =
                object : ListsInteractionListener {
                    override fun onClickAddList() {
                    }

                    override fun onListNameChange(listName: String) {
                    }

                    override fun onCreateNewListClick() {
                    }

                    override fun onListClick(
                        listId: Long,
                        listName: String,
                    ) {
                    }

                    override fun onClickRetryFetchList() {}

                    override fun onDismiss() {
                    }

                    override fun onNavigateToLoginClicked() {
                    }
                },
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListsScreenPreview_WithData() {
    AflamiTheme {
        ListsScreenContent(
            state =
                ListsUiState(
                    isLoading = false,
                    userLists =
                        listOf(
                            WishListItemUiState(
                                id = 1,
                                name = "Favorite Movies",
                                description = "My favorite movies collection",
                                itemCount = 15,
                            ),
                            WishListItemUiState(
                                id = 2,
                                name = "Watch Later",
                                description = "Movies to watch later",
                                itemCount = 8,
                            ),
                            WishListItemUiState(
                                id = 3,
                                name = "Action Movies",
                                description = "Action genre movies",
                                itemCount = 23,
                            ),
                            WishListItemUiState(
                                id = 4,
                                name = "Comedy Collection",
                                description = "Funny movies",
                                itemCount = 12,
                            ),
                        ),
                ),
            interaction =
                object : ListsInteractionListener {
                    override fun onClickAddList() {
                    }

                    override fun onListNameChange(listName: String) {
                    }

                    override fun onCreateNewListClick() {
                    }

                    override fun onListClick(
                        listId: Long,
                        listName: String,
                    ) {
                    }

                    override fun onClickRetryFetchList() {}

                    override fun onDismiss() {
                    }

                    override fun onNavigateToLoginClicked() {
                    }
                },
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListsScreenPreview_Error() {
    AflamiTheme {
        ListsScreenContent(
            state =
                ListsUiState(
                    isLoading = false,
                    userLists = emptyList(),
                    errorUiState = ListsUiState.ListsErrorState.NoNetworkConnection,
                ),
            interaction =
                object : ListsInteractionListener {
                    override fun onClickAddList() {
                    }

                    override fun onListNameChange(listName: String) {
                    }

                    override fun onCreateNewListClick() {
                    }

                    override fun onListClick(
                        listId: Long,
                        listName: String,
                    ) {
                    }

                    override fun onClickRetryFetchList() {}

                    override fun onDismiss() {
                    }

                    override fun onNavigateToLoginClicked() {
                    }
                },
        )
    }
}
