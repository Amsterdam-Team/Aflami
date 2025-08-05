package com.amsterdam.ui.screens.profile.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.RadioButton
import com.amsterdam.designsystem.components.RadioState
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.ui.R
import com.amsterdam.ui.components.DialogTitleRow

@Composable
fun ContentRestrictionDialog(
    isSaveButtonLoading: Boolean,
    modifier: Modifier = Modifier,
    selectedRestriction: RestrictionLevel,
    onSaveClick: () -> Unit,
    onSelectRestriction: (RestrictionLevel) -> Unit,
    onDismissClick: () -> Unit
){
    Dialog(
        onDismiss = onDismissClick
    ) {
        Column(
            modifier = modifier.padding(12.dp)
        ) {
            DialogTitleRow(
                modifier = Modifier.padding(bottom = 24.dp),
                title = stringResource(R.string.content_restriction),
                onDismiss = onDismissClick
            )

            RestrictionSelection(
                title = stringResource(R.string.restriction_strict),
                description = stringResource(R.string.restriction_strict_description),
                isSelected = selectedRestriction == RestrictionLevel.STRICT,
                modifier = Modifier.padding(bottom = 12.dp)
            ){
                onSelectRestriction(RestrictionLevel.STRICT)
            }

            RestrictionSelection(
                title = stringResource(R.string.restriction_moderate),
                description = stringResource(R.string.restriction_moderate_description),
                isSelected = selectedRestriction == RestrictionLevel.MODERATE,
                modifier = Modifier.padding(bottom = 12.dp)
            ){
                onSelectRestriction(RestrictionLevel.MODERATE)
            }

            RestrictionSelection(
                title = stringResource(R.string.restriction_off),
                description = stringResource(R.string.restriction_off_description),
                isSelected = selectedRestriction == RestrictionLevel.OFF,
                modifier = Modifier.padding(bottom = 24.dp)
            ){
                onSelectRestriction(RestrictionLevel.OFF)
            }

            ConfirmButton(
                title = stringResource(R.string.save),
                onClick = onSaveClick,
                isEnabled = true,
                isLoading = isSaveButtonLoading,
                isNegative = false
            )
        }
    }
}

@Composable
private fun RestrictionSelection(
    title: String,
    description: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    val bodyColor by animateColorAsState(
        targetValue = if (isSelected) AppTheme.color.primaryVariant else AppTheme.color.surface
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bodyColor)
            .then(
                if (!isSelected) Modifier.border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp)
                )
                else Modifier
            )
            .clickable(
                onClick = onClick,
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    color = AppTheme.color.body,
                    style = AppTheme.textStyle.label.large
                )
                Text(
                    text = description,
                    color = AppTheme.color.hint,
                    style = AppTheme.textStyle.label.small
                )
            }
            RadioButton(
                state = if (isSelected) RadioState.Selected else RadioState.Default
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ContentRestrictionDialogPreview(){
    AflamiTheme {
        ContentRestrictionDialog(
            onDismissClick = {},
            isSaveButtonLoading = false,
            selectedRestriction = RestrictionLevel.STRICT,
            onSaveClick = {  },
            onSelectRestriction = {}
        )
    }
}