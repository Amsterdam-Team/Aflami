package com.amsterdam.ui.screens.home.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.ImageErrorIndicator
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.ui.components.RatingChip
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState

@SuppressLint("ContextCastToActivity")
@Composable
fun MovieMoodPickerDialogDialog(
    movie: MovieItemUiState,
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
fun DialogContent(
    movie: MovieItemUiState,
    onDismiss: () -> Unit,
    onClickGetAnotherMovie: () -> Unit,
    onClickViewDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.mood_picker),
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title
            )
            IconButton(
                painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                contentDescription = stringResource(R.string.cancel),
                onClick = {
                    onDismiss()
                },
                tint = AppTheme.color.title,
            )
        }

        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(R.string.mood_picker_description),
            style = AppTheme.textStyle.body.medium,
            color = AppTheme.color.body,
            textAlign = TextAlign.Start

        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            SafeImageView(
                model = movie.posterImageUrl,
                contentDescription = stringResource(R.string.movie),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(196.dp)
                        .clip(RoundedCornerShape(16.dp)),
                onLoading = { ImageLoadingIndicator() },
                onError = { ImageErrorIndicator() },
            )

            RatingChip(
                movie.rate,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = movie.name,
                    style = AppTheme.textStyle.label.large,
                    color = AppTheme.color.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.movie),
                        style = AppTheme.textStyle.label.small,
                        color = AppTheme.color.onPrimaryBody,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(3.dp, 3.dp)

                            .background(
                                AppTheme.color.onPrimaryBody,
                                shape = CircleShape
                            )
                            .align(Alignment.CenterVertically)
                    )

                    Text(
                        text = movie.yearOfRelease,
                        style = AppTheme.textStyle.label.small,
                        color = AppTheme.color.onPrimaryBody,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        ConfirmButton(
            title = stringResource(R.string.view_details),
            onClick = onClickViewDetails,
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(top = 24.dp),
        )

        OutlinedButton(
            title = stringResource(R.string.get_another_movie),
            onClick = onClickGetAnotherMovie,
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}


@Composable
@ThemeAndLocalePreviews
private fun MovieDialogPreview() {
    AflamiTheme {
        MovieMoodPickerDialogDialog(
            movie = MovieItemUiState(),
            onDismiss = { },
            onClickViewDetails = {},
            onClickGetAnotherMovie = {}
        )
    }
}