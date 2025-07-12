<<<<<<<< HEAD:ui/src/main/java/com/example/ui/screens/search/SuggestionsHub.kt
package com.example.ui.screens.search
========
package com.example.ui.screens.search.sections
>>>>>>>> origin/feature/implement-search:ui/src/main/java/com/example/ui/screens/search/sections/SuggestionsHub.kt

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
import com.example.designsystem.components.Text
import com.example.designsystem.components.globalSearchHub.GlobalSearchHub
import com.example.designsystem.components.globalSearchHub.GlobalSearchHubUI
import com.example.designsystem.theme.AppTheme
import com.example.ui.R
import com.example.viewmodel.search.GlobalSearchInteractionListener
import com.example.viewmodel.search.SearchUiState

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