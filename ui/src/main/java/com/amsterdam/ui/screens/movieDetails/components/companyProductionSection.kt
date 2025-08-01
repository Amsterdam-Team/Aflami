package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.components.adaptiveGrid
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState

fun LazyListScope.companyProductionSection(
    companies: List<ProductionCompanyUiState>,
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
            CompanyCard(
                productionCompany = productionCompany,
                modifier = Modifier.weight(1f)
            )
        }
    }
}