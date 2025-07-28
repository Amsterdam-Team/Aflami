package com.amsterdam.viewmodel.cast

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.viewmodel.cast.CastUiState.CastErrorUiState
import com.amsterdam.viewmodel.cast.mapper.toUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CastViewModel @Inject constructor(
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getTvShowCastUseCase: GetTvShowCastUseCase,
    private val args: CastScreenArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CastUiState, CastUiEffect>(CastUiState(), dispatcherProvider),
    CastInteractionListener {

    init { fetchCast() }

    private fun fetchCast() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::executeFetchCast,
            onSuccess = ::onGetCastSuccess,
            onError = ::onGetCastError,
            onCompletion = ::onGetCastCompletion,
        )
    }

    private suspend fun executeFetchCast(): List<Actor> {
        return when(MediaType.valueOf(args.mediaType!!)) {
            MediaType.MOVIE -> getMovieCastUseCase(args.mediaId!!)
            MediaType.TV_SHOW -> getTvShowCastUseCase(args.mediaId!!)
        }
    }

    private fun onGetCastSuccess(cast: List<Actor>) {
        updateState { it.copy(cast = cast.map { it.toUiState() }, errorUiState = null) }
    }

    private fun onGetCastError(exception: AflamiException) {
        val errorUiState = when (exception) {
            is NoInternetException -> CastErrorUiState.NoNetworkConnection
            else -> null
        }

        updateState { it.copy(errorUiState = errorUiState) }
    }

    private fun onGetCastCompletion() = updateState { it.copy(isLoading = false) }

    override fun onClickNavigateBack() = sendNewEffect(CastUiEffect.NavigateBack)

    override fun onClickRetrySearch() = fetchCast()

}