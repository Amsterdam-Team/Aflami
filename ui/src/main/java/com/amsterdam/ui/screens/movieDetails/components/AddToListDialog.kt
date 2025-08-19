package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.ui.components.EmptyStateText
import com.amsterdam.ui.utils.withEnglishDigits
import com.amsterdam.viewmodel.movieDetails.WishListUiState

@Composable
fun AddToListDialog(
    userLists: List<WishListUiState>,
    modifier: Modifier = Modifier,
    selectedLists: List<WishListUiState> = emptyList(),
    isAddMovieToListLoading: Boolean = false,
    onSelectedListChange: (List<WishListUiState>) -> Unit = {},
    onAddToSelectedList: (List<Long>) -> Unit = {},
    onCreateNewList: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismiss = onDismiss,
        scrollable = false,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            DialogHeaderSection(
                onDismiss = onDismiss,
            )
            if (userLists.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(userLists) { userList ->
                        SelectionListItem(
                            listName = userList.name,
                            itemCount = userList.itemCount,
                            isSelected = selectedLists.contains(userList),
                            isItemInList = userList.isMovieInList,
                            onSelectItem = {
                                val updatedSelection = if (selectedLists.contains(userList)) {
                                    selectedLists - userList
                                } else {
                                    selectedLists + userList
                                }
                                onSelectedListChange(updatedSelection)
                            },
                        )
                    }
                }
            } else {
                EmptyStateText(stringResource(R.string.there_are_no_lists))
            }
            ActionButtonsSection(
                selectedList = selectedLists,
                isAddMovieToListLoading = isAddMovieToListLoading,
                onAddToSelectedList = onAddToSelectedList,
                onCreateNewList = onCreateNewList,
                modifier = Modifier.padding(bottom = 12.dp),
            )
        }
    }
}

@Composable
private fun SelectionListItem(
    listName: String,
    itemCount: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isItemInList: Boolean = false,
    onSelectItem: () -> Unit = {},
) {

    val (backgroundColor, borderColor, tint) = if (isItemInList) {
        Triple(
            AppTheme.color.disable, AppTheme.color.stroke, AppTheme.color.stroke
        )
    } else if (isSelected) {
        Triple(
            AppTheme.color.primaryVariant, Color.Transparent, AppTheme.color.primary
        )
    } else {
        Triple(
            AppTheme.color.surface, AppTheme.color.stroke, AppTheme.color.hint
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            )
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(
                enabled = !isItemInList,
                onClick = onSelectItem
            )
            .padding(horizontal = 12.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(vertical = 7.dp),
        ) {
            Text(
                text = listName,
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.body,
            )

            val listItemCount = pluralStringResource(R.plurals.item_count, itemCount, itemCount).withEnglishDigits()
            Text(
                text = listItemCount,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.hint,
            )
        }

        val icon = if (isSelected) {
            com.amsterdam.designsystem.R.drawable.ic_checkmark_circle
        } else {
            com.amsterdam.designsystem.R.drawable.ic_add_circle
        }

        if (isItemInList) {
            Text(
                text = stringResource(R.string.added),
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.hint
            )
        } else {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = tint,
            )
        }
    }
}

@Composable
private fun ActionButtonsSection(
    selectedList: List<WishListUiState>,
    isAddMovieToListLoading: Boolean,
    onAddToSelectedList: (List<Long>) -> Unit,
    onCreateNewList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        ConfirmButton(
            title = stringResource(R.string.add),
            onClick = { onAddToSelectedList(selectedList.map { it.id }) },
            isEnabled = selectedList.isNotEmpty(),
            isLoading = isAddMovieToListLoading,
            isNegative = false,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.create_list),
            onClick = onCreateNewList,
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primaryVariant)
            )
    }
}

@Composable
private fun DialogHeaderSection(onDismiss: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.add_to_list),
            style = AppTheme.textStyle.title.large,
            color = AppTheme.color.title,
        )

        IconButton(
            painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
            contentDescription = null,
            tint = AppTheme.color.title,
            onClick = onDismiss,
        )
    }
}

@Preview
@Composable
private fun PreviewAddToListDialog() {
    AflamiTheme {
        AddToListDialog(
            userLists = emptyList(),
            selectedLists = emptyList(),
        )
    }
}
