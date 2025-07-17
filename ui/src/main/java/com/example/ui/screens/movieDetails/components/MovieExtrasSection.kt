package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Chip
import com.example.ui.screens.movieDetails.getExtrasSectionItemInfo
import com.example.viewmodel.shared.Selectable
import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras

@Composable
fun MovieExtrasSection(
    modifier: Modifier = Modifier,
    extras: List<Selectable<MovieExtras>>,
    onClickExtras: (MovieExtras) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        extras.forEach {
            val extrasSectionItemInfo = it.item.getExtrasSectionItemInfo()
            Chip(
                modifier = Modifier.size(70.dp, 96.dp),
                icon = painterResource(extrasSectionItemInfo.iconResId),
                label = stringResource(extrasSectionItemInfo.textResId),
                isSelected = it.isSelected,
                onClick = { onClickExtras(it.item) }
            )
        }
    }
}