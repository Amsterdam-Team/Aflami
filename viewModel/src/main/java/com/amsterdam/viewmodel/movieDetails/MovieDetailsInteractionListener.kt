package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras

interface MovieDetailsInteractionListener {
    fun onClickMovieExtras(movieExtras: MovieExtras)
    fun onClickShowAllCast()
    fun onClickBack()
    fun onClickRetryRequest()

    fun onClickAddToList()

    fun onSaveMovieToList(
        movieId: Int,
        listId: Long,
    )

    fun onClickCreateList()

    fun onChangeListName(listName: String)

    fun onClickCreateNewList()

    fun onSelectedListChange(selectedList: UserListUiState)

    fun onClickRate()

    fun onClickNavigateToLogin()

    fun onClickCancel()
    fun onClickSimilarMovie(movieId: Long)
    fun onDescriptionExpansionToggled()
    fun onReviewExpansionToggled(reviewId: String)

    fun onClickPlayVideo()
}