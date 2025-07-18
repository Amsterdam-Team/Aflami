package com.example.ui.screens.search.searchByCountry.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.R
import com.example.ui.components.NoDataContainer

@Composable
internal fun NoMoviesFound(modifier: Modifier = Modifier) {
    NoDataContainer(
        modifier =
            modifier
                .padding(vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
        title = stringResource(R.string.no_search_result),
        description = stringResource(R.string.no_search_result_for_country),
        imageRes = painterResource(id = R.drawable.placeholder_no_result_found),
    )
}

@Composable
@ThemeAndLocalePreviews
private fun NoMoviesFoundPreview() {
    AflamiTheme {
        NoMoviesFound()
    }
}
