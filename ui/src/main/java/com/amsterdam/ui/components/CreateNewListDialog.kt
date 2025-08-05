package com.amsterdam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun CreateNewListDialog(
    modifier: Modifier = Modifier,
    isCreateListLoading: Boolean = false,
    listName: String = "",
    onListNameChange: (String) -> Unit = {},
    onCreateListClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(
        modifier = modifier,
        onDismiss = onDismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            DialogHeaderSection(
                title = stringResource(R.string.create_list),
                onDismiss = onDismiss,
            )
            TextField(
                text = listName,
                onValueChange = onListNameChange,
                hintText = stringResource(R.string.my_favorite),
                leadingIcon = com.amsterdam.designsystem.R.drawable.ic_nav_lists,
            )

            ConfirmButton(
                title = stringResource(R.string.create),
                isEnabled = listName.isNotEmpty(),
                isLoading = isCreateListLoading,
                isNegative = false,
                onClick = onCreateListClick,
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun PreviewCreateNewListDialog() {
    CreateNewListDialog()
}
