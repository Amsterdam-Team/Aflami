package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.UnknownException
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

open class BaseViewModel<S, E>(initialState: S,private val dispatcherProvider: DispatcherProvider) : ViewModel() {
    interface BaseUiEffect

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E?>()
    val effect = _effect.asSharedFlow()


    protected fun updateState(updater: (S) -> S) {
        viewModelScope.launch(dispatcherProvider.Main) {
            _state.update(updater)
        }
    }

    protected fun sendNewEffect(newEffect: E) {
        viewModelScope.launch(dispatcherProvider.Main) {
            _effect.emit(newEffect)
        }
    }


    protected fun <T> tryToExecute(
        action: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (AflamiException) -> Unit,
        dispatcher: CoroutineDispatcher = dispatcherProvider.IO,
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                action().also {
                    onSuccess(it)
                }
            } catch (exception: AflamiException) {
                onError(exception)
            } catch (_: Exception) {
                onError(UnknownException())
            }
        }
    }
}