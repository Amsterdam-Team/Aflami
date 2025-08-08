package com.amsterdam.viewmodel.cast

import androidx.lifecycle.SavedStateHandle

class CastScreenArgs(savedStateHandle: SavedStateHandle) {

    val mediaId = savedStateHandle.get<Long>(MEDIA_ID_ARGS)
    val mediaType = savedStateHandle.get<String>(MEDIA_TYPE_ARGS)

    companion object {
        const val MEDIA_ID_ARGS = "mediaId"
        const val MEDIA_TYPE_ARGS = "mediaType"
    }
}