package com.amsterdam.viewmodel.listDetails

interface ListDetailsEffect {
    object NavigateBack : ListDetailsEffect
    data class NavigateToMovieDetailsScreen(val movieId: Long) : ListDetailsEffect
    data class ShowErrorSnackbar(val error: ListDetailsError?) : ListDetailsEffect
    object ShowDeletionSuccessSnackBar : ListDetailsEffect
    object ShowRemoveMovieSuccessSnackBar : ListDetailsEffect
}