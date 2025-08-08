package com.amsterdam.viewmodel.listDetails

import androidx.paging.PagingData
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ListDetailsUiState(
    val isLoading: Boolean = false,
    val listId: Long = 0,
    val listName: String = "",
    val listItems: Flow<PagingData<ListDetailsItemsUiState>> = emptyFlow(),
    val showDeleteListDialog: Boolean = false,
    val isDeleteLoading: Boolean = false,
    val error: ListDetailsError? = null
) {

    data class ListDetailsItemsUiState(
        val id: Long = 0,
        val name: String = "",
        val posterImageUrl: String = "",
        val rate: String = "",
        val yearOfRelease: String = "",
        val mediaType: MediaType = MediaType.MOVIE
    )

sealed interface ListDetailsError {
    data object NoNetwork : ListDetailsError
    data object UnknownError : ListDetailsError

    companion object {
        fun toListDetailsError(exception: Throwable): ListDetailsError {
            return when (exception) {
                is NetworkException -> NoNetwork
                else -> UnknownError
            }
        }
    }
}}
