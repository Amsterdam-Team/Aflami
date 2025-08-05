package com.amsterdam.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.movieDetails.UserListUiState

@Composable
fun AddToListDialog(
    userLists: List<UserListUiState>,
    modifier: Modifier = Modifier,
    selectedList: UserListUiState? = null,
    onSelectedListChange: (UserListUiState) -> Unit = {},
    onAddToSelectedList: (Long) -> Unit = {},
    onCreateNewList: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            DialogHeaderSection(
                onDismiss = onDismiss,
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .nestedScroll(rememberNestedScrollInteropConnection()),
            ) {
                items(userLists) { userList ->
                    SelectionListItem(
                        listName = userList.name,
                        itemCount = userList.itemCount,
                        isSelected = selectedList == userList,
                        onSelectItem = { onSelectedListChange(userList) },
                    )
                }
            }
            ActionButtonsSection(
                selectedList = selectedList,
                onAddToSelectedList = onAddToSelectedList,
                onCreateNewList = onCreateNewList,
                modifier = Modifier.padding(bottom = 12.dp)
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
    onSelectItem: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color.Transparent else AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp),
                )
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) AppTheme.color.primaryVariant else AppTheme.color.surface)
                .clickable(onClick = onSelectItem)
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

            val context = LocalContext.current
            val itemCount =
                context.resources.getQuantityString(R.plurals.item_count, itemCount, itemCount)
            Text(
                text = itemCount,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.hint,
            )
        }

        val icon =
            if (isSelected) {
                com.amsterdam.designsystem.R.drawable.ic_checkmark_circle
            } else {
                com.amsterdam.designsystem.R.drawable.ic_add_circle
            }

        Icon(
            painter =
                painterResource(icon),
            contentDescription = null,
            tint = if (isSelected) AppTheme.color.primary else AppTheme.color.hint,
        )
    }
}

@Composable
private fun ActionButtonsSection(
    selectedList: UserListUiState?,
    onAddToSelectedList: (Long) -> Unit,
    onCreateNewList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        ConfirmButton(
            title = stringResource(R.string.add),
            onClick = { onAddToSelectedList(selectedList?.id!!) },
            isEnabled = selectedList != null,
            isLoading = false,
            isNegative = false,
            modifier = Modifier,
        )

        OutlinedButton(
            title = stringResource(R.string.create_list),
            onClick = onCreateNewList,
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primaryVariant),
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
        )
    }
}
