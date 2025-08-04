package com.amsterdam.imageviewer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.amsterdam.imageviewer.classification.SafetyLevel
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
    safetyLevel: SafetyLevel = SafetyLevel.STRICT,
) {
    val state = FirebaseNsfwModelManager.downloadState
    val context = LocalContext.current.applicationContext
    val imageRequest =
        ImageRequest
            .Builder(context)
            .allowHardware(false)
            .data(model)
            .build()

    when (safetyLevel) {
        SafetyLevel.STRICT -> when (state) {
            is ModelDownloadState.Success -> {
                SubcomposeAsyncImage(
                    model = imageRequest,
                    contentDescription = contentDescription,
                    imageLoader = state.imageLoaderStrict, // Use the successful loader
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

        SafetyLevel.MODERATE -> {
            when (state) {
                is ModelDownloadState.Success -> {
                    SubcomposeAsyncImage(
                        model = imageRequest,
                        contentDescription = contentDescription,
                        imageLoader = state.imageLoaderModerate, // Use the successful loader
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

        SafetyLevel.OFF -> {
            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale,
                loading = { onLoading() },
                error = { onError() },
            )
        }
    }
}