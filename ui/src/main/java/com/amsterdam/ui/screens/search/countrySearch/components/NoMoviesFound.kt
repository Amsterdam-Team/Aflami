package com.amsterdam.ui.screens.search.countrySearch.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.NoDataContainer

@Composable
internal fun NoMoviesFound(modifier: Modifier = Modifier) {
    NoDataContainer(
        modifier = modifier,
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
