package com.example.ui.screens.search.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.designsystem.R
import com.example.designsystem.components.Text
import com.example.designsystem.components.globalSearchHub.GlobalSearchHub
import com.example.designsystem.components.globalSearchHub.GlobalSearchHubUI
import com.example.designsystem.theme.AppTheme

@Composable
fun SuggestionsHubSection(
    keyword: String,
    onWorldSearchCardClicked: () -> Unit,
    onActorSearchCardClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(keyword.isBlank()) {
        Column(
            modifier = modifier.padding(
                bottom = 12.dp,
                top = 8.dp,
                start = 16.dp,
                end = 16.dp
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.serach_suggestions_hub),
                style = AppTheme.textStyle.title.medium,
                color = AppTheme.color.title,
                textAlign = TextAlign.Start
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(intrinsicSize = IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlobalSearchHub(
                    modifier = Modifier
                        .weight(1f),
                    globalSearchHubUI = GlobalSearchHubUI.WORLD,
                    onItemClick = onWorldSearchCardClicked
                )

                GlobalSearchHub(
                    modifier = Modifier
                        .weight(1f),
                    globalSearchHubUI = GlobalSearchHubUI.ACTOR,
                    onItemClick = onActorSearchCardClicked
                )
            }
        }
    }
}