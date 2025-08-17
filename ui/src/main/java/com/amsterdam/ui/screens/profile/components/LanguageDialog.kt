package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.ui.screens.profile.model.Language
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language as DomainLanguage

@Composable
fun LanguageDialog(
    language: DomainLanguage,
    modifier: Modifier = Modifier,
    onChangeLanguage: (DomainLanguage) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismiss = onDismiss,
        isDismissible = true,
        modifier = modifier,
    ) {
        DialogContent(
            language = language,
            onConfirm = { onConfirm() },
            onDismiss = { onDismiss() },
            onChangeLanguage = { onChangeLanguage(it) }
        )
    }
}


@Composable
fun DialogContent(
    language: DomainLanguage,
    onConfirm: () -> Unit,
    onChangeLanguage: (DomainLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.language),
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title
            )
            IconButton(
                painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                contentDescription = stringResource(R.string.cancel),
                onClick = {
                    onDismiss()
                },
                tint = AppTheme.color.title,
            )
        }

        LanguageSelectionItem(
            modifier = Modifier.padding(top = 24.dp),
            isSelected = Language.ENGLISH.name == language.name,
            onClick = { onChangeLanguage(DomainLanguage.ENGLISH) },
            text = stringResource(Language.ENGLISH.nameResourceId),
            trailingText = stringResource(Language.ENGLISH.exampleResourceId)
        )

        LanguageSelectionItem(
            modifier = Modifier.padding(top = 12.dp),
            isSelected = Language.ARABIC.name == language.name,
            onClick = { onChangeLanguage(DomainLanguage.ARABIC) },
            text = stringResource(Language.ARABIC.nameResourceId),
            trailingText = stringResource(Language.ARABIC.exampleResourceId)
        )

        ConfirmButton(
            title = stringResource(R.string.apply),
            onClick = { onConfirm() },
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}