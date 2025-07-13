package com.example.imageviewer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import com.example.imageviewer.classification.policy.SafetyPolicy
import com.example.imageviewer.coil.ImageLoaderFactory

@Composable
fun SafeImageView(
    model: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onLoading: (@Composable () -> Unit)? = null,
    onError: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoaderFactory.create(context, SafetyPolicy.SFWPolicy)
    }

    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier,
        contentScale = contentScale,
        loading = { onLoading?.let { it() } ?: LoadingIndicator() },
        error = { onError?.let { it -> it() } ?: ErrorIndicator() },
    )
}