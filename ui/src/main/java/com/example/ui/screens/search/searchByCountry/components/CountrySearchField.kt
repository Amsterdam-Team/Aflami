package com.example.ui.screens.search.searchByCountry.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.TextField
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
internal fun CountrySearchField(
    keyword: String,
    onKeywordValueChanged: (String) -> Unit,
    focusManager: FocusManager,
) {
    TextField(
        text = keyword,
        hintText = stringResource(R.string.country_name_hint),
        onValueChange = onKeywordValueChanged,
        keyboardActions = KeyboardActions(
            onDone = {
                onKeywordValueChanged(keyword)
                focusManager.clearFocus()
            }
        ),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    )
}

@Composable
@ThemeAndLocalePreviews
private fun CountrySearchFieldPreview() {
    AflamiTheme {
        CountrySearchField(
            stringResource(R.string.country_name_hint),
            {},
            LocalFocusManager.current
        )
    }
}