package com.amsterdam.imageviewer.firebase

import coil.ImageLoader

internal sealed interface ModelDownloadState {
    object Idle : ModelDownloadState
    object Downloading : ModelDownloadState

    data class Success(val imageLoader: ImageLoader) : ModelDownloadState
    data class Error(val message: String) : ModelDownloadState
}