package com.example.ui.screens.search.countrySearch.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.viewmodel.search.countrySearch.CountryItemUiState
import kotlin.collections.forEach

@Composable
internal fun CountriesDropdownMenu(
    items: List<CountryItemUiState>,
    onItemClicked: (CountryItemUiState) -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = modifier
                .border(
                    width = 0.5.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp),
                )
                .background(AppTheme.color.surface)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 6.dp)
        ) {
            items.forEach { item ->
                Text(
                    text = item.countryName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            focusManager.clearFocus()
                            onItemClicked(item)
                        },
                    style = AppTheme.textStyle.body.small,
                    color = AppTheme.color.body
                )
            }
        }
    }
}

@Composable
@ThemeAndLocalePreviews
private fun CountriesDropdownMenuPreview() {
    AflamiTheme {
        CountriesDropdownMenu(
            items = buildList(5) { CountryItemUiState("Egypt", "EG") },
            onItemClicked = {},
            isVisible = true,
        )
    }
}