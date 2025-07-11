package com.example.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.CategoryCard
import com.example.designsystem.components.Chip
import com.example.designsystem.components.CustomMoodPickerCard
import com.example.designsystem.components.CustomRadioButton
import com.example.designsystem.components.CustomSlider
import com.example.designsystem.components.EpisodeCard
import com.example.designsystem.components.GenreChip
import com.example.designsystem.components.ListItem
import com.example.designsystem.components.MovieCard
import com.example.designsystem.components.Score
import com.example.designsystem.components.SectionTitle
import com.example.designsystem.components.TabsLayout
import com.example.designsystem.components.TextField
import com.example.designsystem.components.UpcomingCard
import com.example.designsystem.components.appBar.HomeAppBar
import com.example.designsystem.components.bottomNavBar.BottomNavBar
import com.example.designsystem.components.bottomNavBar.Destination
import com.example.designsystem.components.buttons.FloatingActionButton
import com.example.designsystem.components.buttons.PrimaryButton
import com.example.designsystem.components.buttons.SecondaryButton
import com.example.designsystem.components.buttons.TextButton
import com.example.designsystem.components.customSnackBar.SnackBar
import com.example.designsystem.components.customSnackBar.SnackBarStatus
import com.example.designsystem.components.gameCard.GameCard
import com.example.designsystem.components.gameCard.GameCardImageContentType
import com.example.designsystem.components.globalSearchHub.GlobalSearchHub
import com.example.designsystem.components.globalSearchHub.GlobalSearchHubUI
import com.example.designsystem.components.guessGame.GuessPicture
import com.example.designsystem.components.guessGame.GuessTitle
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme

@Composable
fun DesignSystem(){
    val scrollState = rememberScrollState()
    var selectedIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HomeAppBar()
        BottomNavBar(
            items = mapOf(),
            selectedDestination = Destination.HOME
        )
        CustomSlider(
            aflamiImageList = listOf(
                R.drawable.bg_man_with_popcorn,
                R.drawable.bg_directly_shot_film,
                R.drawable.bg_cinema_movie_theater,
                R.drawable.bg_directly_shot_film,
                R.drawable.bg_children_wearing_3d,
                R.drawable.bg_children_wearing_3d,
            )
        )
        FloatingActionButton(
            icon = R.drawable.ic_add,
            onClick = {},
            isNegative = false,
            isLoading = false
        )
        FloatingActionButton(
            icon = R.drawable.ic_add,
            onClick = {},
            isNegative = true,
            isLoading = false
        )
        FloatingActionButton(
            icon = R.drawable.ic_add,
            onClick = {},
            isNegative = false,
            isLoading = true
        )
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = true
        )
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = true,
            isLoading = false,
            isEnabled = true
        )
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = false
        )
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = true,
            isEnabled = true
        )
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = true
        )
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = true,
            isLoading = false,
            isEnabled = true
        )
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = false
        )
        SecondaryButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = true,
            isEnabled = true
        )
        TextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = true
        )
        TextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = true,
            isLoading = false,
            isEnabled = true
        )
        TextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = false,
            isEnabled = false
        )
        TextButton(
            modifier = Modifier.fillMaxWidth(0.5f),
            title = stringResource(R.string.add),
            onClick = {},
            isNegative = false,
            isLoading = true,
            isEnabled = true
        )
        Box {
            SnackBar(
                message = stringResource(R.string.list_added_success_message),
                status = SnackBarStatus.Success
            )
        }
        Box {
            SnackBar(
                message = stringResource(R.string.general_error_message),
                status = SnackBarStatus.Failure
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlobalSearchHub(
                GlobalSearchHubUI.ACTOR
            )
            GlobalSearchHub(
                GlobalSearchHubUI.WORLD
            )
        }
        MovieCard(
            movieImage = painterResource(R.drawable.bg_man_with_popcorn),
            movieTitle = "Eternal Sunshine of the Spotless mind",
            movieType = "Sci-fi",
            movieYear = "2002",
            movieContentDescription = "best movie ever you should totally watch it",
            movieRating = "9.8"
        )
        UpcomingCard(
            movieImage = painterResource(R.drawable.bg_man_with_popcorn),
            movieTitle = "Eternal Sunshine of the Spotless mind",
            movieType = "Sci-fi",
            movieYear = "2002",
            movieContentDescription = "best movie ever you should totally watch it",
            movieRating = "9.8"
        )
        ListItem(
            title = "Kitten Movies",
            count = 10,
            modifier = Modifier.size(160.dp, 147.dp)
        )
        CategoryCard(
            categoryName = stringResource(R.string.action),
            painterResource(R.drawable.img_action)
        )
        Chip(
            icon = painterResource(R.drawable.ic_nav_categories),
            label = stringResource(R.string.categories),
            isSelected = true
        )
        Chip(
            icon = painterResource(R.drawable.ic_nav_categories),
            label = stringResource(R.string.categories),
            isSelected = false
        )
        CustomMoodPickerCard(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        CustomRadioButton(isEnable = true)
        CustomRadioButton(isEnable = false)
        GenreChip(
            genre = stringResource(R.string.action),
            selected = false
        )
        GenreChip(
            genre = stringResource(R.string.action),
            selected = true
        )
        Score(
            1
        )
        Score(
            -1
        )
        SectionTitle(
            title = stringResource(R.string.movies_birthday),
            icon = painterResource(R.drawable.ic_birthday_cake),
            showAllLabel = true,
            tintColor = AppTheme.color.yellowAccent
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
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextField(
            "",
            hintText = stringResource(R.string.password_hint),
            leadingIcon = R.drawable.ic_user,
            trailingIcon = R.drawable.ic_password_hide,
            isError = true,
            errorMessage = stringResource(R.string.general_error_message),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextField(
            stringResource(R.string.action_adventure),
            hintText = stringResource(R.string.hint),
            trailingIcon = R.drawable.ic_filter_vertical,
            maxCharacters = 20,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TextField(
            "",
            hintText = stringResource(R.string.country_name_hint),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.release_game_title),
            description = stringResource(R.string.release_game_description),
            containerColor = AppTheme.color.navyCard,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x800A203A)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.CALENDER,
            isPlayable = false,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.release_game_title),
            description = stringResource(R.string.release_game_description),
            containerColor = AppTheme.color.navyCard,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x800A203A)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.CALENDER,
            isPlayable = true,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.guess_movie_game_title),
            description = stringResource(R.string.guess_movie_game_description),
            containerColor = AppTheme.color.blueCard,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x802BA3D9)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.MANY_POSTERS,
            isPlayable = false,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.guess_movie_game_title),
            description = stringResource(R.string.guess_movie_game_description),
            containerColor = AppTheme.color.blueCard,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x802BA3D9)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.MANY_POSTERS,
            isPlayable = true,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.genre_game_title),
            description = stringResource(R.string.genre_game_description),
            containerColor = AppTheme.color.yellowCard,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x80E5A02E)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.LAWN_CHAIR,
            isPlayable = false,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.genre_game_title),
            description = stringResource(R.string.genre_game_description),
            containerColor = AppTheme.color.yellowCard,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x80E5A02E)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.LAWN_CHAIR,
            isPlayable = true,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.guess_character_game_title),
            description = stringResource(R.string.guess_character_game_description),
            containerColor = AppTheme.color.primaryVariant,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x80D85895)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.FUN_CLOWN,
            isPlayable = false,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GameCard(
            title = stringResource(R.string.guess_character_game_title),
            description = stringResource(R.string.guess_character_game_description),
            containerColor = AppTheme.color.primaryVariant,
            borderColors = listOf(Color(0x05FFFFFF), Color(0x80D85895)),
            onCardClick = {},
            gameCardImageContentType = GameCardImageContentType.FUN_CLOWN,
            isPlayable = true,
            unlockPrice = "400",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GuessPicture(
            blurRadius = 8.dp,
            points = 10,
            painter = painterResource(R.drawable.bg_children_wearing_3d),
            showHint = true,
            onClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GuessPicture(
            blurRadius = 8.dp,
            points = 10,
            painter = painterResource(R.drawable.bg_children_wearing_3d),
            showHint = false,
            onClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GuessTitle(
            title = "The Green Mile",
            points = 10,
            showHint = true,
            onClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        GuessTitle(
            title = "The Green Mile",
            points = 10,
            showHint = false,
            onClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        EpisodeCard(
            episodeBanner = painterResource(id = R.drawable.bg_man_with_popcorn),
            episodeRate = 4.5,
            episodeNumber = 1,
            episodeTitle = "Recovering a body",
            episodeTime = 58,
            publishedAt = "3 Sep 2020",
            episodeDescription = "In 1935, corrections officer Paul Edgecomb oversees ",
            onPlayEpisodeClick = { },
        )
    }
}

@Preview(heightDp = 2000)
@Composable
fun DesignSystemPreview(){
    AflamiTheme(isDarkTheme = true) {
        DesignSystem()
    }
}