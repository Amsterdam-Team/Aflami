package com.amsterdam.viewmodel.home

import com.amsterdam.domain.models.Mood
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType

interface HomeInteractionListener {
    fun onClickRetryLoading()
    fun onClickSearch()
    fun onClickMediaItem(mediaId : Long, mediaType: MediaType)
    fun onClickShowAllContinueWatchingMovies()
    fun onClickUpcomingMovieCard(id: Long)
    fun onChangeUpcomingMovieGenre(genre: MovieGenre)
    fun onClickShowAllToRatedMovies()

    fun onClickMood(mood: Mood)
    fun onClickGetNow()
    fun onDismissMoodPickerDialog()

    fun onClickViewDetails()
    fun onClickGetAnotherMovie()
}