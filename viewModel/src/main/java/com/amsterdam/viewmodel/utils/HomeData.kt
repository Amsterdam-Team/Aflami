package com.amsterdam.viewmodel.utils

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow

data class HomeData(
    val topRatedMovies: List<Movie>,
    val topRatedTvShows: List<TvShow>,
    val popularMovies: List<Movie>,
    val popularTvShows: List<TvShow>,
    val upComingMovies: List<Movie>
)