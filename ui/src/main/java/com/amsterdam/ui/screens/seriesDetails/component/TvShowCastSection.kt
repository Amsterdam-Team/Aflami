package com.amsterdam.ui.screens.seriesDetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.SectionTitle
import com.amsterdam.ui.components.movieAndTvShowDetails.ActorCard
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.ActorTvShowUiState

@Composable
fun TvShowCastSection(
    modifier: Modifier = Modifier,
    actors: List<ActorTvShowUiState>,
    onClickAllCast: () -> Unit
) {
    AnimatedVisibility(
        visible = actors.isNotEmpty(),
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SectionTitle(
                    title = stringResource(R.string.cast),
                    showAllLabel = true,
                    onAllLabelClicked = onClickAllCast,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            LazyRow(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(actors) {
                    ActorCard(
                        name = it.name,
                        photoUrl = it.photo
                    )
                }
            }
        }
    }
}