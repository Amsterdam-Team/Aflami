package com.example.viewmodel.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel<S, E>(
    initialState: S,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    interface BaseUiEffect

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E?>()
    val effect = _effect.asSharedFlow()


    protected fun updateState(updater: (S) -> S) {
        viewModelScope.launch(dispatcherProvider.MainImmediate) {
            _state.update(updater)
        }
    }

    protected fun sendNewEffect(newEffect: E) {
        viewModelScope.launch(dispatcherProvider.MainImmediate) {
            _effect.emit(newEffect)
        }
    }

    protected fun <T> tryToExecute(
        action: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (AflamiException) -> Unit,
        onCompletion: () -> Unit = {},
        dispatcher: CoroutineDispatcher = dispatcherProvider.IO,
    ): Job {
        return viewModelScope.launch(dispatcher) {
            try {
                action().also {
                    onSuccess(it)
                }
            } catch (exception: AflamiException) {
                onError(exception)
            } catch (_: Exception) {
                onError(AflamiException())
            } finally {
                onCompletion()
            }
        }
    }
}