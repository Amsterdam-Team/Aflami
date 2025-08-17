package com.amsterdam.ui.screens.seriesDetails.mappers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeasonUiState.DurationUiState

@Composable
fun DurationUiState.toLocalizedString(): String {
    return when {
        hour > 0 && minute > 0 ->
            stringResource(R.string.duration_hours_minutes, hour, minute)
        hour > 0 ->
            stringResource(R.string.duration_hours, hour)
        else ->
            stringResource(R.string.duration_minutes, minute)
    }
}