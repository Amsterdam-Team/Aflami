package com.example.imageviewer.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.imageviewer.classification.policy.SafetyPolicy
import com.example.imageviewer.coil.ImageLoaderFactory


@Composable
public fun SafeImageView(
    model: String,
    contentDescription: String?,
    @DrawableRes placeholder: Int,
    @DrawableRes error: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val imageLoader = remember(context) {
        ImageLoaderFactory.create(context, SafetyPolicy.NudityPolicy)
    }

    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier,
        placeholder = painterResource(placeholder),
        error = painterResource(error),
    )
}