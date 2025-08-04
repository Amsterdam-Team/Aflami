package com.amsterdam.ui.screens.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.TopAppBar
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.profile.ProfileInteractionListener
import com.amsterdam.viewmodel.profile.ProfileUiState


@Composable
fun LoggedInContent(
    state: ProfileUiState,
    interactionListener: ProfileInteractionListener
) {
    val lazyListState = rememberLazyListState()
    val scrollOffset = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset.value > 8) AppTheme.color.surface else Color.Transparent,
        animationSpec = tween(800),
        label = "AppBarScrollColor"
    )

    AnimatedVisibility(
        state.settingsState.isSettingsDialogVisible
    ) {
        SettingsDialog(
            onChangePasswordClick = interactionListener::onClickForgotPassword,
            onContentRestrictionClick = interactionListener::onClickContentRestriction,
            onLogoutClick = interactionListener::onClickLogout,
            onDismissClick = interactionListener::onDismissSettingsDialog
        )
    }

    AnimatedVisibility(
        state.settingsState.isLogoutDialogVisible
    ) {
        LogoutDialog(
            isLogoutButtonLoading = state.settingsState.isLogoutButtonLoading,
            onLogoutClick = interactionListener::onClickConfirmLogout,
            onDismissClick = interactionListener::onDismissLogoutDialog
        )
    }

    AnimatedVisibility(
        state.settingsState.isContentRestrictionDialogVisible
    ) {
        ContentRestrictionDialog(
            isSaveButtonLoading = state.settingsState.isContentRestrictionSaveButtonLoading,
            selectedRestriction = state.settingsState.contentRestrictionLevel,
            onSaveClick = interactionListener::onSaveRestrictionLevel,
            onSelectRestriction = interactionListener::onUpdateRestrictionLevel,
            onDismissClick = interactionListener::onDismissContentRestrictionDialog
        )
    }

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            item { ProfileImageSection() }
            item { ProfileInfoSection() }
            item { HistoryAndRatingSection() }
            item { HorizontalDivider() }
            item { SettingsSection(
                onSettingsClicked = interactionListener::onClickSettings
            ) }
            item {
                Text(
                    text = "v 1.1",
                    style = AppTheme.textStyle.label.small,
                    color = AppTheme.color.hint,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.profile),
                    style = AppTheme.textStyle.title.large,
                    color = AppTheme.color.title,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                )
            },
            modifier = Modifier
                .background(appBarColor)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun LoggedInContentPreview() {
    AflamiTheme {
        LoggedInContent(
            state = ProfileUiState(),
            interactionListener = object : ProfileInteractionListener {
                override fun onClickLogin() {}
                override fun onClickSettings() {}
                override fun onDismissSettingsDialog() {}
                override fun onClickLogout() {}
                override fun onDismissLogoutDialog() {}
                override fun onClickForgotPassword() {}
                override fun onClickContentRestriction() {}
                override fun onDismissContentRestrictionDialog() {}
                override fun onClickConfirmLogout() {}
                override fun onUpdateRestrictionLevel(restrictionLevel: RestrictionLevel) {}
                override fun onSaveRestrictionLevel() {}
            }
        )
    }
}