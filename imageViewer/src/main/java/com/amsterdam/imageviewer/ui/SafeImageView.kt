package com.amsterdam.imageviewer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import com.amsterdam.imageviewer.firebase.FirebaseNsfwModelManager
import com.amsterdam.imageviewer.firebase.ModelDownloadState

@Composable
fun SafeImageView(
    model: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onLoading: (@Composable () -> Unit),
    onError: (@Composable () -> Unit),
    isClassified: Boolean = true
) {
    val state = FirebaseNsfwModelManager.downloadState
    val context = LocalContext.current.applicationContext

    when (state) {
        is ModelDownloadState.Success -> {
            SubcomposeAsyncImage(
                model = model,
                contentDescription = contentDescription,
                imageLoader = state.imageLoader, // Use the successful loader
                modifier = modifier,
                contentScale = contentScale,
                loading = { onLoading() },
                error = { onError() },
            )
        }

        is ModelDownloadState.Error -> {
            LaunchedEffect(Unit) {
                FirebaseNsfwModelManager.initialize(context)
            }
            onError()
        }

        is ModelDownloadState.Downloading,
        is ModelDownloadState.Idle -> {
            LaunchedEffect(Unit) {
                FirebaseNsfwModelManager.initialize(context)
            }
            onLoading()
        }
    }
}