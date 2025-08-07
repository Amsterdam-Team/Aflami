package com.amsterdam.ui.screens.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.profile.ProfileInteractionListener
import com.amsterdam.viewmodel.profile.ProfileUiState


@Composable
fun LoggedInContent(
    state: ProfileUiState,
    interactionListener: ProfileInteractionListener,
    onClickHistory: () -> Unit
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

    val showVersion by remember {
        derivedStateOf {
            !lazyListState.canScrollForward
        }
    }

    ScreenDialogs(state, interactionListener)

    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                item { ProfileImageSection(state.userInfo.userAvatarUrl) }
                item { ProfileInfoSection(state.userInfo.username, state.userInfo.userPoints) }
                item {
                    HistoryAndRatingSection(
                        onClickHistory = onClickHistory,
                        onClickRating = interactionListener::onClickRating
                    )
                }
                item { HorizontalDivider() }
                item {
                    SettingsSection(
                        state,
                        onSettingsClicked = interactionListener::onClickSettings,
                        onClickLanguage = interactionListener::onClickLanguageSetting,
                        onClickTheme = interactionListener::onClickThemeSetting
                    )
                }
            }
            AnimatedVisibility(
                visible = showVersion,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "v ${state.appVersion}",
                    style = AppTheme.textStyle.label.small,
                    color = AppTheme.color.hint,
                    modifier = Modifier
                        .fillMaxWidth(),
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

@Composable
private fun ScreenDialogs(
    state: ProfileUiState,
    interactionListener: ProfileInteractionListener
) {
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

    AnimatedVisibility(
        state.showLanguageDialog,
        enter = fadeIn(tween(2000)),
        exit = fadeOut(tween(2000)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LanguageDialog(
                language = state.language,
                onConfirm = { interactionListener.onApplyLanguage() },
                onDismiss = { interactionListener.onDismissLanguageDialog() },
                onChangeLanguage = { language -> interactionListener.onChangeLanguage(language) }
            )
        }
    }

    AnimatedVisibility(
        state.showThemeDialog,
        enter = fadeIn(tween(2000)),
        exit = fadeOut(tween(2000)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ThemeDialog(
                isDarkTheme = state.isDarkTheme,
                onConfirm = { interactionListener.onApplyTheme() },
                onDismiss = { interactionListener.onDismissThemeDialog() },
                onChangeTheme = { isDarkTheme ->
                    interactionListener.onChangeTheme(isDarkTheme)
                }
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun LoggedInContentPreview() {
    AflamiTheme {
        LoggedInContent(
            ProfileUiState(), object : ProfileInteractionListener {
                override fun onClickLogin() {}

                override fun onClickLanguageSetting() {}

                override fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language) {}

                override fun onApplyLanguage() {}

                override fun onDismissLanguageDialog() {}

                override fun onClickThemeSetting() {}

                override fun onChangeTheme(isDarkTheme: Boolean) {}

                override fun onApplyTheme() {}

                override fun onDismissThemeDialog() {}
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
                override fun onClickRating() {}
            },
            onClickHistory = {})
    }
}