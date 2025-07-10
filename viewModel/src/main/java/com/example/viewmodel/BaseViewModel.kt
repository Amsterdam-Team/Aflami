package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.UnknownException
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

open class BaseViewModel<S, E>(initialState: S) : ViewModel() {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E?>()
    val effect = _effect.asSharedFlow().throttleFirst(500).mapNotNull { it }


    protected fun updateState(updater: (S) -> S) {
        _state.update(updater)
    }

    protected fun sendNewEffect(newEffect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(newEffect)
        }
    }


    protected fun <T> tryToExecute(
        action: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (AflamiException) -> Unit,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
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

    private fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
        require(periodMillis > 0)
        return flow {
            var lastTime = 0L
            collect { value ->
                val currentTime = Clock.System.now().toEpochMilliseconds()
                if (currentTime - lastTime >= periodMillis) {
                    lastTime = currentTime
                    emit(value)
                }
            }
        }
    }
}