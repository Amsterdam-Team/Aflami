package com.amsterdam.ui.screens.seriesDetails.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.components.adaptiveGrid
import com.amsterdam.ui.screens.movieDetails.components.EmptyStateText
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.ProductionTvShowCompanyUiState

fun LazyListScope.companyProductionTvShowSection(
    companies: List<ProductionTvShowCompanyUiState>,
    deviceWidth: Int
) {
    if (companies.isEmpty()) {
        item { EmptyStateText(stringResource(com.amsterdam.ui.R.string.there_is_no_company_production)) }
    } else {
        adaptiveGrid(
            deviceWidth = deviceWidth,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            items = companies, itemMinWidth = 160,
            itemsHorizontalPadding = 8.dp, itemsVerticalPadding = 8.dp
        ) { productionCompany ->
            CompanyTvShowCard(
                productionCompany = productionCompany,
                modifier = Modifier.weight(1f)
            )
        }
    }
}