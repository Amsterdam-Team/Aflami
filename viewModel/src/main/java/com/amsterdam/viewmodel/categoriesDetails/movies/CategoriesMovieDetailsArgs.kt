package com.amsterdam.viewmodel.categoriesDetails.movies

import androidx.lifecycle.SavedStateHandle

class CategoriesMovieDetailsArgs (savedStateHandle: SavedStateHandle){
    val genreName = savedStateHandle.get<String>(GENRE_NAME)


    companion object{
        const val GENRE_NAME = "genreName"
    }
}