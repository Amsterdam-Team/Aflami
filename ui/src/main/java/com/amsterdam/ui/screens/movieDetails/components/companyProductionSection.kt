package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.R
import com.amsterdam.ui.components.EmptyStateText
import com.amsterdam.ui.components.adaptiveGrid
import com.amsterdam.ui.components.movieAndTvShowDetails.CompanyCard
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.ProductionMovieCompanyUiState

fun LazyListScope.companyProductionSection(
    companies: List<ProductionMovieCompanyUiState>,
    deviceWidth: Int
) {
    if (companies.isEmpty()) {
        item { EmptyStateText(stringResource(R.string.there_is_no_company_production)) }
    } else {
        adaptiveGrid(
            availableWidth = deviceWidth,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 12.dp,
            ),
            items = companies, itemMinWidth = 160,
            itemsHorizontalPadding = 8.dp, itemsVerticalPadding = 8.dp
        ) { productionCompany ->
            CompanyCard(
                modifier = Modifier.weight(1f),
                name = productionCompany.name,
                country = productionCompany.country,
                image = productionCompany.image
            )
        }
    }
}