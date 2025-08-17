package com.amsterdam.ui.screens.myRating.placeholders

import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.ui.components.NoDataContainer

fun LazyGridScope.emptyRatingListPlaceholder(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    modifier: Modifier = Modifier
) {

    item(span = { GridItemSpan(maxLineSpan) }) {
            NoDataContainer(
                modifier = modifier,
                imageRes = painterResource(R.drawable.img_user_rating),
                title = stringResource(titleRes),
                description = stringResource(descriptionRes)
            )
    }
}