package com.example.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.GenreChip
import com.example.designsystem.components.Icon
import com.example.designsystem.components.RadioButton
import com.example.designsystem.components.RadioState
import com.example.designsystem.components.Score
import com.example.designsystem.components.SectionTitle
import com.example.designsystem.components.Slider
import com.example.designsystem.components.TabsLayout
import com.example.designsystem.components.TextField
import com.example.designsystem.components.buttons.ConfirmButton
import com.example.designsystem.components.buttons.FloatingActionButton
import com.example.designsystem.components.buttons.OutlinedButton
import com.example.designsystem.components.buttons.PlainTextButton
import com.example.designsystem.components.chip.Chip
import com.example.designsystem.components.customSnackBar.SnackBar
import com.example.designsystem.components.customSnackBar.SnackBarStatus
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun DesignSystem() {
    val scrollState = rememberScrollState()
    var selectedIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(AppTheme.color.surface)
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Slider(
            aflamiImageList =
                listOf(
                    R.drawable.bg_man_with_popcorn,
                    R.drawable.bg_directly_shot_film,
                    R.drawable.bg_cinema_movie_theater,
                    R.drawable.bg_directly_shot_film,
                    R.drawable.bg_children_wearing_3d,
                    R.drawable.bg_children_wearing_3d,
                ),
        )
        FloatingActionButton(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                )
            },
            onClick = {},
            isNegative = false,
            isLoading = false,
        )
        FloatingActionButton(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                )
            },
            onClick = {},
            isNegative = true,
            isLoading = false,
        )
        FloatingActionButton(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                )
            },
            onClick = {},
            isNegative = false,
            isLoading = true,
        )
        ConfirmButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = true,
        )
        ConfirmButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = true,
            isLoading = false,
            isEnabled = true,
        )
        ConfirmButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = false,
        )
        ConfirmButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = true,
            isEnabled = true,
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = true,
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = true,
            isLoading = false,
            isEnabled = true,
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = false,
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = true,
            isEnabled = true,
        )
        PlainTextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = true,
        )
        PlainTextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = true,
            isLoading = false,
            isEnabled = true,
        )
        PlainTextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = false,
        )
        PlainTextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = true,
            isEnabled = true,
        )
        Box {
            SnackBar(
                message = stringResource(R.string.list_added_success_message),
                status = SnackBarStatus.Success,
            )
        }
        Box {
            SnackBar(
                message = stringResource(R.string.general_error_message),
                status = SnackBarStatus.Failure,
            )
        }
        Chip(
            icon = painterResource(R.drawable.ic_nav_categories),
            label = stringResource(R.string.categories),
            isSelected = true,
        )
        Chip(
            icon = painterResource(R.drawable.ic_nav_categories),
            label = stringResource(R.string.categories),
            isSelected = false,
        )
        RadioButton(state = RadioState.Selected)
        RadioButton(state = RadioState.Unselected)
        GenreChip(
            genre = stringResource(R.string.action),
            selected = false,
        )
        GenreChip(
            genre = stringResource(R.string.action),
            selected = true,
        )
        Score(
            1,
        )
        Score(
            -1,
        )
        SectionTitle(
            title = stringResource(R.string.movies_birthday),
            icon = painterResource(R.drawable.ic_birthday_cake),
            showAllLabel = true,
            tintColor = AppTheme.color.yellowAccent,
        )
        TabsLayout(
            modifier = Modifier.fillMaxWidth(),
            tabs = listOf("tab1", "tab2", "tab3ddasdasdasas", "tab4"),
            selectedIndex = selectedIndex,
            onSelectTab = { selectedIndex = it },
        )
        TextField(
            "",
            hintText = stringResource(R.string.user_name_hint),
            leadingIcon = R.drawable.ic_user,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        TextField(
            "",
            hintText = stringResource(R.string.password_hint),
            leadingIcon = R.drawable.ic_user,
            trailingIcon = R.drawable.ic_password_hide,
            isError = true,
            errorMessage = stringResource(R.string.general_error_message),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        TextField(
            stringResource(R.string.action_adventure),
            hintText = stringResource(R.string.hint),
            trailingIcon = R.drawable.ic_filter_vertical,
            maxCharacters = 20,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        TextField(
            "",
            hintText = stringResource(R.string.country_name_hint),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun DesignSystemPreview() {
    AflamiTheme(isDarkTheme = true) {
        DesignSystem()
    }
}
