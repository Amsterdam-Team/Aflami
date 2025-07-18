package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.imageviewer.ui.SafeImageView

fun LazyListScope.GallerySection(gallery: List<String>) {
    itemsIndexed(gallery.chunked(2), key = { index, _ -> index }) { index, rowGallery ->

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp, end =
                        16.dp, bottom = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowGallery.forEachIndexed { index, image ->
                val maxWidth = if (index == 0) .5f else 1f

                SafeImageView(
                    modifier = Modifier
                        .fillMaxWidth(maxWidth)
                        .height(145.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = null,
                    model = image,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

