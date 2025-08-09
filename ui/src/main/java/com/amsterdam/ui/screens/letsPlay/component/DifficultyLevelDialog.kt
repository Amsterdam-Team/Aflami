package com.amsterdam.ui.screens.letsPlay.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
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
import androidx.compose.ui.zIndex
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.chip.GenreChip
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.letsPlay.getDifficultyLevelTextId
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState

@Composable
fun DifficultyLevelDialog(
    selectedDifficultyLevel: GameDifficultyUiState?,
    difficulties: List<GameDifficultyUiState>,
    isStartGameButtonEnable: Boolean,
    onLevelSelected: (GameDifficultyUiState) -> Unit,
    onCloseDialogClicked: () -> Unit,
    onClickStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismiss = onCloseDialogClicked,
        modifier = modifier
    ) {
        Column (modifier = Modifier.padding(12.dp)) {
            DefaultAppBar(
                showNavigateBackButton = false,
                title = stringResource(com.amsterdam.ui.R.string.choose_level),
                lastOption = painterResource(R.drawable.ic_cancel),
                onLastOptionClicked = onCloseDialogClicked
            )
            Row(
                modifier = Modifier.padding(top = 24.dp).zIndex(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                difficulties.forEach {
                    GenreChip(
                        genre = stringResource(it.difficultyLevel.getDifficultyLevelTextId()),
                        selected = selectedDifficultyLevel == it,
                        onClick = { onLevelSelected(it) }
                    )
                }
            }

                AnimatedVisibility(
                    visible = selectedDifficultyLevel != null,
                    enter = slideInVertically(
                        animationSpec = tween(600),
                        initialOffsetY = { -it },
                    ),
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    selectedDifficultyLevel?.let { difficulty ->
                    Row(
                        modifier = Modifier.fillMaxWidth().background(AppTheme.color.surfaceHigh,RoundedCornerShape(12.dp)).padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_idea),
                            contentDescription = null,
                            tint = AppTheme.color.yellowAccent
                        )
                        Text(
                            text = stringResource(
                                com.amsterdam.ui.R.string.game_settings,
                                difficulty.totalQuestions,
                                difficulty.timeLimitSeconds,
                                difficulty.pointsPerQuestion
                            ),
                            style = AppTheme.textStyle.label.small,
                            color = AppTheme.color.yellowAccent
                        )
                    }
                }
            }
            ConfirmButton(
                title = stringResource(com.amsterdam.ui.R.string.lets_start),
                onClick = onClickStartGame,
                isEnabled = isStartGameButtonEnable,
                isLoading = false,
                isNegative = false,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }

}