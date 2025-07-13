package com.amsterdam.ui.screens.search.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.globalSearchHub.GlobalSearchHub
import com.amsterdam.designsystem.components.globalSearchHub.GlobalSearchHubUI
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.search.GlobalSearchInteractionListener
import com.amsterdam.viewmodel.search.SearchUiState

@Composable
fun SuggestionsHubSection(
    state: SearchUiState,
    interaction: GlobalSearchInteractionListener,
) {
    AnimatedVisibility(state.query.isEmpty()) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 8.dp, start = 16.dp, end = 16.dp),
                text = stringResource(R.string.search_suggestions_hub),
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlobalSearchHub(
                    modifier = Modifier
                        .weight(1f),
                    globalSearchHubUI = GlobalSearchHubUI.WORLD,
                    onItemClick = interaction::onWorldSearchCardClicked
                )

                GlobalSearchHub(
                    modifier = Modifier
                        .weight(1f),
                    globalSearchHubUI = GlobalSearchHubUI.ACTOR,
                    onItemClick = interaction::onActorSearchCardClicked
                )
            }
        }
    }
}