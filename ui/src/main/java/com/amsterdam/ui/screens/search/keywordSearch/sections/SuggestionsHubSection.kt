package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.globalSearchHub.GlobalSearchHub
import com.amsterdam.ui.components.globalSearchHub.GlobalSearchHubUI

@Composable
internal fun SuggestionsHubSection(
    onWorldSearchCardClicked: () -> Unit,
    onActorSearchCardClicked: () -> Unit,
) {
    Text(
        text = stringResource(R.string.serach_suggestions_hub),
        style = AppTheme.textStyle.title.medium,
        color = AppTheme.color.title,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
    )
    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GlobalSearchHub(
            modifier = Modifier.weight(1f),
            globalSearchHubUI = GlobalSearchHubUI.WORLD,
            onItemClick = onWorldSearchCardClicked,
        )

        GlobalSearchHub(
            modifier = Modifier.weight(1f),
            globalSearchHubUI = GlobalSearchHubUI.ACTOR,
            onItemClick = onActorSearchCardClicked,
        )
    }
}
