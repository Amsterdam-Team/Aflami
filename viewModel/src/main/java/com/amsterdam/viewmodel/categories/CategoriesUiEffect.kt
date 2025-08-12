package com.amsterdam.viewmodel.categories


interface CategoriesUiEffect {
    data class NavigateToCategoriesDetailsScreen(val genreName: String) : CategoriesUiEffect
    data class NavigateToCategoriesTvShowsDetailsScreen(val genreName: String) : CategoriesUiEffect

}