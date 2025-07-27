package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
internal fun ExploreMoviesAndShows(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.placeholder_no_result_found),
            contentDescription = null,
            modifier = Modifier.height(82.dp),
        )
        Text(
            text = stringResource(R.string.search_description),
            modifier = Modifier.padding(top = 8.dp),
            style = AppTheme.textStyle.body.small,
            color = AppTheme.color.body,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@ThemeAndLocalePreviews
private fun ExploreCountriesPreview() {
    AflamiTheme {
        ExploreMoviesAndShows()
    }
}
