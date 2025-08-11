package com.amsterdam.ui.screens.letsPlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.ui.screens.letsPlay.component.CompletionCard
import com.amsterdam.ui.screens.letsPlay.component.StatCard
import com.amsterdam.viewmodel.gameEnd.ResultInteractionListener
import com.amsterdam.viewmodel.gameEnd.ResultSideEffect
import com.amsterdam.viewmodel.gameEnd.ResultUiState
import com.amsterdam.viewmodel.gameEnd.ResultViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ResultScreen(
    viewModel: ResultViewModel = hiltViewModel(),
    onNavigateToGame: () -> Unit,
    onNavigateToMenu: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ResultSideEffect.NavigateToGame -> onNavigateToGame()
                is ResultSideEffect.NavigateBackToMenu -> onNavigateToMenu()
            }
        }
    }

    ResultScreenContent(
        state = state,
        listener = viewModel
    )
}

@Composable
 fun ResultScreenContent(
    state: ResultUiState,
    listener: ResultInteractionListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        DefaultAppBar(
            title = stringResource(R.string.guess_character_game_title),
            showNavigateBackButton = true,
            onNavigateBackClicked = listener::onClickClose,
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompletionCard()
            Spacer(modifier = Modifier.height(41.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    iconRes = com.amsterdam.designsystem.R.drawable.img_user_rating,
                    label = "Points Achieved",
                    value = "${state.points} Pts."
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.img_user_history,
                    label = "Total time",
                    value = "${state.timeInSeconds} Sec."
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ConfirmButton(
                    title = stringResource(R.string.back_to_menue),
                    onClick = listener::onClickBackToMenu,
                    isEnabled = true,
                    isLoading = false,
                    isNegative = false,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(
                    title = stringResource(R.string.play_again),
                    onClick = listener::onClickPlayAgain,
                    isEnabled = true,
                    isLoading = false,
                    isNegative = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}




@ThemeAndLocalePreviews
@Composable
private fun ResultScreenPreview() {
    ResultScreenContent(
        state = ResultUiState(points = 110, timeInSeconds = 110),
        listener = object : ResultInteractionListener {
            override fun onClickPlayAgain() {}
            override fun onClickBackToMenu() {}
            override fun onClickClose() {}
        }
    )
}