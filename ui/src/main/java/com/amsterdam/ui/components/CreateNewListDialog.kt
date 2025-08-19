package com.amsterdam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.theme.AppTheme
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
                onValueChange =  onListNameChange ,
                maxCharacters = 25,
                hintText = stringResource(R.string.my_favorite),
                leadingIcon = com.amsterdam.designsystem.R.drawable.ic_nav_lists,
            )

            ConfirmButton(
                title = stringResource(R.string.create),
                isEnabled = listName.isNotEmpty(),
                isLoading = isCreateListLoading,
                isNegative = false,
                onClick = onCreateListClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DialogHeaderSection(
    title: String,
    onDismiss: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
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


@ThemeAndLocalePreviews
@Composable
private fun PreviewCreateNewListDialog() {
    CreateNewListDialog()
}
