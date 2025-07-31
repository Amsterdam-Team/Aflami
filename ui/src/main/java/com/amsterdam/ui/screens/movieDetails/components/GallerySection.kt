package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.components.AdaptiveGrid

@Composable
fun GallerySection(
    gallery: List<String>,
    modifier: Modifier = Modifier
) {
    AdaptiveGrid(
        gallery,
        160,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
    ) { item ->
        SafeImageView(
            modifier = modifier
                .weight(1f)
                .height(145.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentDescription = null,
            model = item.toString(),
            contentScale = ContentScale.Crop,
            onLoading = { ImageLoadingIndicator() },
            onError = { ImageErrorIndicator() },
        )
    }
}

