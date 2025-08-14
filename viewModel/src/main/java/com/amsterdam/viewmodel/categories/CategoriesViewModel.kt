package com.amsterdam.viewmodel.categories

import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CategoriesUiState, CategoriesUiEffect>
    (CategoriesUiState(), dispatcherProvider), CategoriesInteractionListener {

    override fun onChangeTabOption(tabOption: TabOption) {
        updateState {
            it.copy(selectedTabOption = tabOption)
        }
    }
    override fun onClickMovieGenreCard(
        genreName: String,
    ) {
        sendNewNavigationEffect(
            CategoriesUiEffect.NavigateToCategoriesDetailsScreen(
                genreName,
            )
        )
    }

    override fun onClickTvShowGenreCard(genreName: String) {
        sendNewNavigationEffect(
            CategoriesUiEffect.NavigateToCategoriesTvShowsDetailsScreen(
                genreName,
            )
        )
    }
}

