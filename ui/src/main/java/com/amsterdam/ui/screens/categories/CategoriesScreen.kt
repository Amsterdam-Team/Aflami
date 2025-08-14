package com.amsterdam.ui.screens.categories

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.TabsLayout
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.components.CategoryCard
import com.amsterdam.ui.screens.categoriesDetails.GenreMovieUiModel
import com.amsterdam.ui.screens.categoriesDetails.GenreTvShowUiModel
import com.amsterdam.viewmodel.categories.CategoriesInteractionListener
import com.amsterdam.viewmodel.categories.CategoriesUiEffect
import com.amsterdam.viewmodel.categories.CategoriesUiState
import com.amsterdam.viewmodel.categories.CategoriesViewModel
import com.amsterdam.viewmodel.shared.TabOption

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navigationManager = LocalNavManager.current
    CategoriesScreenContent(interaction = viewModel, state = state)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategoriesUiEffect.NavigateToCategoriesDetailsScreen -> {
                    navigationManager.toCategoriesDetails(
                        genreName = effect.genreName
                    )
                }

                is CategoriesUiEffect.NavigateToCategoriesTvShowsDetailsScreen -> {
                    navigationManager.toCategoriesTvShowsDetails(
                        genreName = effect.genreName
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoriesScreenContent(
    interaction: CategoriesInteractionListener, state: CategoriesUiState,
) {
    val lazyState = rememberLazyGridState()
    var appBarHeight by remember { mutableIntStateOf(0) }
    var tabsHeight by remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier
                    .padding(vertical = 13.dp, horizontal = 16.dp)
                    .onSizeChanged { appBarHeight = it.height },
                text = stringResource(R.string.categories),
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title
            )
            TabsLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { tabsHeight = it.height },
                tabs = listOf(
                    stringResource(R.string.movies), stringResource(R.string.tv_shows)
                ),
                selectedIndex = state.selectedTabOption.index,
                onSelectTab = { index -> interaction.onChangeTabOption(TabOption.entries[index]) },
            )
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .background(AppTheme.color.surface)
                    .animateContentSize(),
                state = lazyState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(top = 17.dp, bottom = 89.dp),
                columns = GridCells.Adaptive(160.dp)
            ) {
                when (state.selectedTabOption) {
                    TabOption.MOVIES -> {
                        items(GenreMovieUiModel.entries) { movieGenre ->
                            val genre = movieGenre.name
                            CategoryCard(
                                modifier = Modifier,
                                categoryName = stringResource(movieGenre.displayName),
                                categoryImage = painterResource(movieGenre.imageRes),
                                onClick = {
                                    interaction.onClickMovieGenreCard(genre)
                                })
                        }
                    }

                    TabOption.TV_SHOWS -> {
                        items(GenreTvShowUiModel.entries) { tvShowGenre ->
                            val genre = tvShowGenre.name
                            CategoryCard(
                                modifier = Modifier,
                                categoryName = stringResource(tvShowGenre.displayName),
                                categoryImage = painterResource(tvShowGenre.imageRes),
                                onClick = {
                                    interaction.onClickTvShowGenreCard(genre)
                                })
                        }
                    }
                }
            }
        }
    }
}