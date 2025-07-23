package com.example.viewmodel.home

import com.example.entity.category.MovieGenre

interface HomeInteractionListener {
    fun onClickRetryLoading()
    fun onClickSearch()

    fun onClickUpcomingMovieCard(id: Long)
    fun onChangeUpcomingMovieGenre(genre: MovieGenre)
    fun onClickMovie(movieId : Long)
    fun onClickShowAllToRatedMovies()
}