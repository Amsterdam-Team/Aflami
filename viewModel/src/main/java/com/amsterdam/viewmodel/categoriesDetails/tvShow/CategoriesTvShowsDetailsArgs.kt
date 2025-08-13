package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.lifecycle.SavedStateHandle

class CategoriesTvShowsDetailsArgs(savedStateHandle: SavedStateHandle) {
    val genreName = savedStateHandle.get<String>(GENRE_NAME)


    companion object{
        const val GENRE_NAME = "genreName"

    }
}