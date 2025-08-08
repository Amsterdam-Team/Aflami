package com.amsterdam.ui.screens.search.countrySearch.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
internal fun CountrySearchField(
    keyword: String,
    onKeywordValueChanged: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        text = keyword,
        hintText = stringResource(R.string.country_name_hint),
        onValueChange = { onKeywordValueChanged(it.trim()) },
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onKeywordValueChanged(keyword)
            },
        ),
        imeAction = ImeAction.Search,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    )
}

@Composable
@ThemeAndLocalePreviews
private fun CountrySearchFieldPreview() {
    AflamiTheme {
        CountrySearchField(stringResource(R.string.country_name_hint)) {}
    }
}