package com.example.ui.screens.search.searchByCountry.components

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
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.R

@Composable
internal fun ExploreCountries(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.tour_world_image),
            contentDescription = stringResource(R.string.country_tour_image_description),
            modifier = Modifier.height(82.dp),
        )
        Text(
            text = stringResource(R.string.country_tour_title),
            modifier = Modifier.padding(top = 16.dp),
            style = AppTheme.textStyle.title.medium,
            color = AppTheme.color.title,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.country_tour_description),
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
        ExploreCountries()
    }
}
