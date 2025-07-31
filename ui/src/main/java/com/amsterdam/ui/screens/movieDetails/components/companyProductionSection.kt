package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState

fun LazyListScope.companyProductionSection(companies: List<ProductionCompanyUiState>) {
    if (companies.isEmpty()){ item { EmptyStateText(stringResource(com.amsterdam.ui.R.string.there_is_no_company_production)) } }
    else {
        itemsIndexed(companies.chunked(2), key = { index, _ -> index }) { index, rowCompanies ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp, end =
                            16.dp, bottom = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowCompanies.forEachIndexed { index, company ->
                    val maxWidth = if (index == 0) .5f else 1f
                    CompanyCard(
                        productionCompany = company,
                        modifier = Modifier.fillMaxWidth(maxWidth)
                    )
                }
            }
        }
    }
}