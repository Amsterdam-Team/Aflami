package com.amsterdam.viewmodel.baseViewModel

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider

class TestViewModel(
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<Int, String>(initialState = 0, dispatcherProvider = dispatcherProvider) {

    fun increment(num: Int) {
        updateState {it + num }
    }

    fun sendTestEffect(effect: String) {
        sendNewEffect(effect)
    }

    fun executeWithSuccess() {
        tryToExecute(
            action = { "Success" },
            onSuccess = { sendTestEffect(it) },
            onError = { sendTestEffect("Error") }
        )
    }

    fun executeWithError() {
        tryToExecute(
            action = { throw AflamiException() },
            onSuccess = { sendTestEffect(it.toString()) },
            onError = { sendTestEffect("Error") }
        )
    }
}
