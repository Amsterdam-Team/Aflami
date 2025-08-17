package com.amsterdam.ui.screens.search.keywordSearch.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.R
import com.amsterdam.designsystem.components.TabsLayout
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.appBar.DefaultAppBar
import com.amsterdam.viewmodel.shared.TabOption

@Composable
fun SearchScreenHeaderSection(
    keyword: String,
    selectedTabOption: TabOption,
    onNavigateBackClicked: () -> Unit,
    onKeywordValuedChanged: (String) -> Unit,
    onFilterButtonClicked: () -> Unit,
    onTabOptionClicked: (TabOption) -> Unit,
    onSaveSearchHistory: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
    onHeaderSizeChanged: (IntSize) -> Unit = {},
) {
    Column(modifier = modifier.onSizeChanged(onSizeChanged = onHeaderSizeChanged)) {
        DefaultAppBar(
            title = stringResource(R.string.search),
            onNavigateBackClicked = onNavigateBackClicked,
        )

        TextField(
            modifier =
                Modifier
                    .background(color = AppTheme.color.surface)
                    .padding(top = 8.dp),
            text = keyword,
            onValueChange = { onKeywordValuedChanged(it) },
            hintText = stringResource(R.string.search_hint),
            trailingIcon = com.amsterdam.ui.R.drawable.ic_filter_vertical,
            onTrailingClick = onFilterButtonClicked,
            isTrailingClickEnabled = keyword.isNotBlank(),
            isError = keyword.length > 100,
            errorMessage = stringResource(R.string.search_error_query_too_long),
            maxCharacters = 80,
            keyboardActions =
                KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        onKeywordValuedChanged(keyword)
                        onSaveSearchHistory()
                    },
                ),
            imeAction = ImeAction.Search,
        )

        AnimatedVisibility(keyword.isNotBlank()) {
            TabsLayout(
                modifier = Modifier.fillMaxWidth(),
                tabs =
                    listOf(
                        stringResource(R.string.movies),
                        stringResource(R.string.tv_shows),
                    ),
                selectedIndex = selectedTabOption.index,
                onSelectTab = { index -> onTabOptionClicked(TabOption.entries[index]) },
            )
        }
    }
}