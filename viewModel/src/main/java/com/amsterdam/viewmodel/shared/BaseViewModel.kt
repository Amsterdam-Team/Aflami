package com.amsterdam.viewmodel.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

open class BaseViewModel<S, E>(
    initialState: S,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    interface BaseUiEffect

    private val _effect = MutableSharedFlow<Pair<E, Boolean>>()
    val effect = _effect.asSharedFlow()
        .throttleFirstSelective(500) { it.second }
        .mapNotNull { it.first }

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)

    val state: StateFlow<S> = _state.asStateFlow()


    protected fun updateState(updater: (S) -> S) {
        viewModelScope.launch(dispatcherProvider.MainImmediate) {
            _state.update(updater)
        }
    }

    protected fun sendNewEffect(newEffect: E) {
        viewModelScope.launch(dispatcherProvider.Default) {
            _effect.emit(newEffect to false)
        }
    }

    protected fun sendNewNavigationEffect(navigationEffect: E) {
        viewModelScope.launch(dispatcherProvider.Default) {
            _effect.emit(navigationEffect to true)
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

    private fun <T> Flow<T>.throttleFirstSelective(
        periodMillis: Long,
        shouldThrottle: (T) -> Boolean
    ): Flow<T> {
        require(periodMillis > 0)
        return flow {
            var lastTime = 0L
            collect { value ->
                if (!shouldThrottle(value)) {
                    emit(value)
                } else {
                    val currentTime = Clock.System.now().toEpochMilliseconds()
                    if (currentTime - lastTime >= periodMillis) {
                        lastTime = currentTime
                        emit(value)
                    }
                }
            }
        }
    }
}