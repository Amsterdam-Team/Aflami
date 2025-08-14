package com.amsterdam.ui.components.movieAndTvShowDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.components.RatingBar
import com.amsterdam.viewmodel.myRating.RateDialogInteractionListener

@Composable
fun RateDialog(
    interaction: RateDialogInteractionListener,
    isSubmittingEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    selectedStarIndex: Int? = null
) {
    Dialog(
        onDismissRequest = interaction::onClickCancelRateDialog,
        properties = DialogProperties(usePlatformDefaultWidth = false,),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    color = AppTheme.color.surface,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.rate),
                    color = AppTheme.color.title,
                    style = AppTheme.textStyle.title.large,
                )

                IconButton(
                    painter = painterResource(R.drawable.ic_cancel),
                    contentDescription = "",
                    onClick = interaction::onClickCancelRateDialog,
                    tint = AppTheme.color.title,
                )
            }

            Text(
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                text = stringResource(R.string.select_how_much_you_like_it),
                color = AppTheme.color.body,
                style = AppTheme.textStyle.body.medium,
            )

            RatingBar(
                modifier = Modifier.padding(bottom = 24.dp),
                selectedStarIndex = selectedStarIndex,
                onRatingStarChanged = interaction::onChangeRating,
                starsCount = 5,
                starSize = 32.dp,
                useEqualSpacing = false
            )

            ConfirmButton(
                title = stringResource(R.string.submit),
                onClick = interaction::onClickSubmit,
                isEnabled = isSubmittingEnabled,
                isLoading = isLoading,
                isNegative = false,
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun RateDialogPreview() {
    AflamiTheme {
        RateDialog(
            interaction = object : RateDialogInteractionListener {
                override fun onClickCancelRateDialog() {}

                override fun onClickSubmit() {}
                override fun onChangeRating(newRate: Int) {}
            },
            isSubmittingEnabled = true,
            isLoading = false,
            selectedStarIndex = 2,
        )
    }
}