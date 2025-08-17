package com.amsterdam.viewmodel.lists

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.GetWishListsUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListsViewModel @Inject constructor(
    private val getWishListsUseCase: GetWishListsUseCase,
    private val createListUseCase: CreateNewListUseCase,
    private val getsSessionType: GetsSessionType,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ListsUiState, ListsEffect>(
    ListsUiState(),
    dispatcherProvider,
),
    ListsInteractionListener {

    init {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {
                    updateState { it.copy(isUserLoggedIn = true) }
                    manageLocaleLanguageUseCase
                        .getAppLanguage()
                        .onEach {
                            loadCustomLists()
                        }.launchIn(viewModelScope)

                    loadCustomLists()
                },
                onGuest = {
                    updateState { it.copy(isUserLoggedIn = false, isLoading = false) }
                },
            )
        }
    }

    fun loadCustomLists(startLoading: Boolean = true) {
        startLoading(startLoading)
        tryToExecute(
            action = { getWishListsUseCase() },
            onSuccess = { customLists ->
                updateState {
                    it.copy(
                        userLists = customLists.map { it.toWishListItemUiState() },
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

    private fun startLoading(start: Boolean = true) = updateState { it.copy(isLoading = start) }

    private suspend fun runIfLoggedIn(
        onLoggedIn: () -> Unit,
        onGuest: () -> Unit,
    ) {
        if (getsSessionType() != SessionType.GUEST) {
            onLoggedIn()
        } else {
            onGuest()
        }
    }


    override fun onClickAddList() {
        viewModelScope.launch {
            runIfLoggedIn(
                onLoggedIn = {
                    updateState {
                        it.copy(
                            isCreateNewListDialogVisible = true,
                            isUserLoggedIn = true
                        )
                    }
                },
                onGuest = {
                    updateState { it.copy(isUserLoggedIn = false) }
                },
            )
        }
    }

    override fun onListNameChange(listName: String) {
        updateState { it.copy(listName = listName) }
    }

    override fun onCreateNewListClick() {
        updateState { it.copy(isCreateListLoading = true) }
        tryToExecute(
            action = {
                createListUseCase(state.value.listName)
            },
            onSuccess = {
                sendNewEffect(ListsEffect.ListCreatedSuccessfully)
                loadCustomLists()
            },
            onError = {
                sendNewEffect(ListsEffect.FailedToCreateList)
            },
            onCompletion = {
                updateState {
                    it.copy(
                        isCreateNewListDialogVisible = false,
                        listName = "",
                        isCreateListLoading = false,
                    )
                }
            },
        )
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

    override fun onDismiss() {
        updateState { it.copy(isCreateNewListDialogVisible = false, listName = "") }
    }

    override fun onNavigateToLoginClicked() {
        sendNewEffect(ListsEffect.NavigateToLogin)
    }


}