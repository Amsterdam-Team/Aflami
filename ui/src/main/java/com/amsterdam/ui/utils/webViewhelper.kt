package com.amsterdam.ui.utils

fun buildFormExistenceCheckScript(): String {
    return "(function() { return document.querySelector('form[action=\"/signup\"]') !== null; })();"
}

fun createFormDetector(
    onFormSubmittingComplete: () -> Unit
): (isFormPresent: Boolean) -> Unit {
    var hasInitialFormLoaded = false

    return { isFormPresent ->
        val isSuccess = hasInitialFormLoaded && !isFormPresent

        if (isSuccess) {
            onFormSubmittingComplete()
        } else if (isFormPresent) {
            hasInitialFormLoaded = true
        }
    }
}