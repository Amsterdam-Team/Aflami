package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras

interface MovieDetailsInteractionListener {
    fun onClickMovieExtras(movieExtras: MovieExtras)
    fun onClickShowAllCast()
    fun onClickBack()
    fun onClickRetryRequest()
    fun onAddToListClicked()

    fun onSaveMovieToList(
        movieId: Int,
        listId: Long,
    )

    fun onClickAddList()

    fun onListNameChange(listName: String)

    fun onCreateNewListClick()

    fun onRateClicked()
    fun onNavigateToLoginClicked()
    fun onCancelClicked()
    fun onClickSimilarMovie(movieId: Long)
    fun onDescriptionExpansionToggled()
    fun onReviewExpansionToggled(reviewId: String)
    fun onPlayVideoClicked()
}