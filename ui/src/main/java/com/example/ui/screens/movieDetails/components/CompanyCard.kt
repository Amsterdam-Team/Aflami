package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.designsystem.components.ImageErrorIndicator
import com.example.designsystem.components.ImageLoadingIndicator
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AppTheme
import com.example.imageviewer.ui.SafeImageView
import com.example.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState


@Composable
fun CompanyCard(productionCompany: ProductionCompanyUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(145.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = AppTheme.color.stroke, shape = RoundedCornerShape(16.dp))

    ) {
        SafeImageView(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            contentDescription = productionCompany.name,
            model = productionCompany.image,
            contentScale = ContentScale.FillBounds,
            onLoading = { ImageLoadingIndicator() },
            onError = { ImageErrorIndicator() },
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(2f)
                .padding(
                    start = 8.dp, end = 8.dp, bottom =
                        8.dp
                )
        ) {


            Text(
                modifier = Modifier.fillMaxWidth(), text =
                    productionCompany.name,
                maxLines = 1,
                style = AppTheme.textStyle.label.large,
                overflow = TextOverflow.Ellipsis,
                color = AppTheme.color.onPrimary
            )
            Text(
                text = productionCompany.country,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.onPrimaryBody
            )
        }
        val overlayDarkColor = AppTheme.color.overlayDark
        Box(
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.BottomCenter)
                .height(82.dp)
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = overlayDarkColor
                        )
                    )
                }
        )
    }
}

