package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.screens.home.component.MoodPickerCard
import com.amsterdam.ui.screens.home.mapper.toCardMood
import com.amsterdam.ui.screens.home.mapper.toMood
import com.amsterdam.ui.screens.home.model.CardMood
import com.amsterdam.viewmodel.home.HomeInteractionListener
import com.amsterdam.viewmodel.home.HomeUiState

@Composable
fun MoodPickerSection(
    state: HomeUiState.MoodPickerUiState,
    interactionListener: HomeInteractionListener,
    modifier: Modifier = Modifier
) {
    MoodPickerCard(
        cardMoods = state.moods.map { it.toCardMood() },
        selectedMood = state.selectedMood?.toCardMood(),
        isButtonEnabled = state.selectedMood != null,
        isLoading = state.isLoadingMovies,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 26.dp),
        onSelectMood = {
            val mood = it.toMood()
            interactionListener.onChangeMood(mood)
        },
        onClickGetNow = interactionListener::onClickGetNow
    )
}