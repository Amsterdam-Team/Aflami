package com.example.ui.screen.search.sections

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
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.R

@Composable
fun SuggestionsHubSection(
    onWorldTourCardClick: () -> Unit,
    onFindByActorCardClick: () -> Unit
) {
    Text(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 8.dp),
        text = stringResource(R.string.search_suggestions_hub),
        style = AppTheme.textStyle.title.medium,
        color = AppTheme.color.title,
        textAlign = TextAlign.Start
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GlobalSearchHub(
            modifier = Modifier
                .weight(1f),
            globalSearchHubUI = GlobalSearchHubUI.WORLD,
            onItemClick = { onWorldTourCardClick() }
        )

        GlobalSearchHub(
            modifier = Modifier
                .weight(1f),
            globalSearchHubUI = GlobalSearchHubUI.ACTOR,
            onItemClick = { onFindByActorCardClick() }
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun SuggestionsHubSectionPreview() {
    Column {
        SuggestionsHubSection({}, {})
    }
}