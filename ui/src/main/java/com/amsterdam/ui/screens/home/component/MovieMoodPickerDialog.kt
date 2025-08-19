package com.amsterdam.ui.screens.home.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalRestrictionLevel
import com.amsterdam.ui.components.DialogTitleRow
import com.amsterdam.ui.components.MediaCard
import com.amsterdam.ui.utils.toSafetyLevel
import com.amsterdam.viewmodel.home.HomeUiState.MoodPickerItemUiState

@SuppressLint("ContextCastToActivity")
@Composable
fun MovieMoodPickerDialog(
    movie: MoodPickerItemUiState,
    onClickViewDetails: () -> Unit,
    onClickGetAnotherMovie: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismiss = onDismiss,
        isDismissible = true,
        modifier = modifier,
    ) {
        DialogContent(
            movie,
            onDismiss,
            onClickGetAnotherMovie,
            onClickViewDetails
        )
    }
}

@Composable
private fun DialogContent(
    movie: MoodPickerItemUiState,
    onDismiss: () -> Unit,
    onClickGetAnotherMovie: () -> Unit,
    onClickViewDetails: () -> Unit
) {
    val safetyLevel = LocalRestrictionLevel.current.toSafetyLevel()
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {

        DialogTitleRow(
            title = stringResource(R.string.mood_picker),
            onDismiss = onDismiss
        )

        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(R.string.mood_picker_description),
            style = AppTheme.textStyle.body.medium,
            color = AppTheme.color.body,
            textAlign = TextAlign.Start

        )

        if (movie.id != 0L) {
            MediaCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                movieImage = {
                    SafeImageView(
                        model = movie.posterImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        safetyLevel = safetyLevel,onLoading = { ImageLoadingIndicator() },
                        onError = { ImageErrorIndicator() },
                        isAdult = movie.isAdult
                    )
                },
                movieType = stringResource(R.string.movie),
                movieYear = movie.yearOfRelease,
                movieTitle = movie.name,
                movieRating = movie.rate
            )
        } else {
            LoadingContainer(Modifier.padding(top = 12.dp).height(222.dp).fillMaxWidth())
        }

        ConfirmButton(
            title = stringResource(R.string.view_details),
            onClick = onClickViewDetails,
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(top = 24.dp).fillMaxWidth(),
        )

        OutlinedButton(
            title = stringResource(R.string.get_another_movie),
            onClick = onClickGetAnotherMovie,
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth()
        )
    }
}


@Composable
@ThemeAndLocalePreviews
private fun MovieDialogPreview() {
    AflamiTheme {
        MovieMoodPickerDialog(
            movie = MoodPickerItemUiState(),
            onDismiss = { },
            onClickViewDetails = {},
            onClickGetAnotherMovie = {}
        )
    }
}