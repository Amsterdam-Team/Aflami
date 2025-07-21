package com.example.imageviewer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import com.example.imageviewer.coil.providers.ImageLoaderProvider

@Composable
fun SafeImageView(
    model: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onLoading: (@Composable () -> Unit),
    onError: (@Composable () -> Unit),
) {
    val imageLoader = rememberSafeImageLoader()

    imageLoader?.let { imageLoaders ->
        SubcomposeAsyncImage(
            model = model,
            contentDescription = contentDescription,
            imageLoader = imageLoaders,
            modifier = modifier,
            contentScale = contentScale,
            loading = { onLoading() },
            error = { onError() },
        )
    } ?: onLoading()
}


@Composable
private fun rememberSafeImageLoader(): ImageLoader? {
    val context = LocalContext.current.applicationContext
    var imageLoader by remember { mutableStateOf<ImageLoader?>(null) }

    LaunchedEffect(Unit) {
        imageLoader = ImageLoaderProvider.getInstance(context)
    }

    return imageLoader
}