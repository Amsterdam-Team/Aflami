package com.amsterdam.viewmodel.categories

interface CategoriesUiEffect {
    data class NavigateToCategoriesMoviesDetailsScreen(val genreName: String) : CategoriesUiEffect
    data class NavigateToCategoriesTvShowsDetailsScreen(val genreName: String) : CategoriesUiEffect
}