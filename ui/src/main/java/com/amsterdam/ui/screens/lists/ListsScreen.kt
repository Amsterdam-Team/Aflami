package com.amsterdam.ui.screens.lists

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.ListItem
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.lists.ListsEffect
import com.amsterdam.viewmodel.lists.ListsInteractionListener
import com.amsterdam.viewmodel.lists.ListsUiState
import com.amsterdam.viewmodel.lists.UserListsViewModel
import com.amsterdam.viewmodel.shared.uiStates.UserListItemUiState

@Composable
fun ListsScreen(
    modifier: Modifier = Modifier,
    viewModel: UserListsViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val uiState = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ListsEffect.NavigateToAddCustomList -> {}

                is ListsEffect.NavigateToListDetails -> {
                    navController.navigate(Route.ListDetails(listId = effect.listId, listName = effect.listName))
                }
            }
        }
    }

    ListsScreenContent(
        modifier = modifier,
        state = uiState.value,
        interaction = viewModel
    )
}

@Composable
private fun ListsScreenContent(
    modifier: Modifier = Modifier,
    state: ListsUiState,
    interaction: ListsInteractionListener
) {
    var headerHeight by remember { mutableStateOf(0.dp) }
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = AppTheme.color.surface)
                .statusBarsPadding()
                .navigationBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DefaultAppBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            showNavigateBackButton = false,
            title = stringResource(com.amsterdam.ui.R.string.lists),
            lastOption = painterResource(R.drawable.ic_add),
        )

        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
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
                        modifier = Modifier.fillMaxSize(),
                        onClickRetry = interaction::onClickRetryFetchList,
                    )
                }

                lists.isEmpty() -> {
                    NoDataContainer(
                        modifier = Modifier.fillMaxSize(),
                        title = stringResource(com.amsterdam.ui.R.string.no_list_yet),
                        description = stringResource(com.amsterdam.ui.R.string.no_list_description),
                        imageRes = painterResource(id = com.amsterdam.ui.R.drawable.placeholder_no_saved_items),
                    )
                }

                else -> {

                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Adaptive(minSize = 156.dp),
                        state = rememberLazyGridState(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(lists) { userList ->
                            ListItem(
                                title = userList.name,
                                count = userList.itemCount,
                                modifier =
                                    Modifier
                                        .size(156.dp, 147.dp)
                                        .clickable(
                                            onClick = {
                                                interaction.onListClick(userList.id.toLong(), userList.name)
                                            },
                                        ),
                            )
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
            interaction = object : ListsInteractionListener {
                override fun onClickAddCustomList() {

                }

                override fun onListClick(
                    listId: Long,
                    listName: String,
                ) {
                }

                override fun onClickRetryFetchList() {}
            }
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListsScreenPreview_Empty() {
    AflamiTheme {
        ListsScreenContent(
            state = ListsUiState(
                isLoading = false,
                userLists = emptyList()
            ),
            interaction = object : ListsInteractionListener {
                override fun onClickAddCustomList() {
                }

                override fun onListClick(
                    listId: Long,
                    listName: String,
                ) {
                }

                override fun onClickRetryFetchList() {}
            }
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListsScreenPreview_WithData() {
    AflamiTheme {
        ListsScreenContent(
            state = ListsUiState(
                isLoading = false,
                userLists = listOf(
                    UserListItemUiState(
                        id = 1,
                        name = "Favorite Movies",
                        description = "My favorite movies collection",
                        itemCount = 15
                    ),
                    UserListItemUiState(
                        id = 2,
                        name = "Watch Later",
                        description = "Movies to watch later",
                        itemCount = 8
                    ),
                    UserListItemUiState(
                        id = 3,
                        name = "Action Movies",
                        description = "Action genre movies",
                        itemCount = 23
                    ),
                    UserListItemUiState(
                        id = 4,
                        name = "Comedy Collection",
                        description = "Funny movies",
                        itemCount = 12
                    )
                )
            ),
            interaction = object : ListsInteractionListener {
                override fun onClickAddCustomList() {
                }

                override fun onListClick(
                    listId: Long,
                    listName: String,
                ) {
                }

                override fun onClickRetryFetchList() {}
            }
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ListsScreenPreview_Error() {
    AflamiTheme {
        ListsScreenContent(
            state = ListsUiState(
                isLoading = false,
                userLists = emptyList(),
                errorUiState = ListsUiState.ListsErrorState.NoNetworkConnection,
            ),
            interaction = object : ListsInteractionListener {
                override fun onClickAddCustomList() {
                }

                override fun onListClick(
                    listId: Long, listName: String) {
                }

                override fun onClickRetryFetchList() {}
            }
        )
    }
}

