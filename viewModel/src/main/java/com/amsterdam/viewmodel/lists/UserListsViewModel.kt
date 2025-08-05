package com.amsterdam.viewmodel.lists

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.list.GetUserListsUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserListsViewModel @Inject constructor(
    private val getUserListsUseCase: GetUserListsUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ListsUiState, ListsEffect>(
    ListsUiState(),
    dispatcherProvider,
),
    ListsInteractionListener {

    init {
        manageLocaleLanguageUseCase.getDeviceLanguage()
            .onEach {
                loadCustomLists()
            }.launchIn(viewModelScope)

        loadCustomLists()
    }

    private fun loadCustomLists(startLoading: Boolean = true) {
        startLoading(startLoading)
        tryToExecute(
            action = { getUserListsUseCase() },
            onSuccess = { customLists ->
               updateState {
                    it.copy(
                        userLists = customLists.map { it.toUserListItemUiState() },
                        isLoading = false,
                        errorUiState = null
                    )
                }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorUiState = ListsUiState.ListsErrorState.toListsErrorState(exception),
                    )
                }
            }
        )
    }

    override fun onClickAddCustomList() {
        sendNewEffect(ListsEffect.NavigateToAddCustomList)
    }

    override fun onListClick(
        listId: Long,
        listName: String,
    ) {
        sendNewEffect(ListsEffect.NavigateToListDetails(listId, listName))
    }

    override fun onClickRetryFetchList() {
        loadCustomLists()
    }

    private fun startLoading(start: Boolean = true) = updateState { it.copy(isLoading = start) }


}