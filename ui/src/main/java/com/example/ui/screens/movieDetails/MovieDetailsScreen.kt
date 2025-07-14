package com.example.ui.screens.movieDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.viewmodel.movieDetails.MovieDetailsUiState
import com.example.viewmodel.movieDetails.MovieDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.designsystem.R
import com.example.designsystem.components.Chip
import com.example.designsystem.components.ExpandableText
import com.example.designsystem.components.Icon
import com.example.designsystem.components.RatingChip
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.designsystem.utils.dropShadow
import com.example.imageviewer.ui.SafeImageView
import com.example.viewmodel.movieDetails.ActorUiState
import com.example.viewmodel.movieDetails.Extras
import com.example.viewmodel.movieDetails.Selectable

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()
    MovieContent(state.value)
}


@Composable
fun MovieContent(state: MovieDetailsUiState) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.328f)
        ) {
            SafeImageView(
                model = state.imageUrl,
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
            DefaultAppBar(
                modifier = Modifier.statusBarsPadding(),
                firstOption = painterResource(R.drawable.ic_outlined_star),
                lastOption = painterResource(R.drawable.ic_outlined_add_to_favourite)
            )
            RatingChip(
                state.rating,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 34.dp, start = 4.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppTheme.color.surface)
        ) {
            PlayButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = (-32).dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .offset(y = (-20).dp)
            ) {
                Text(
                    text = state.movieTitle,
                    style = AppTheme.textStyle.title.large,
                    color = AppTheme.color.title
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.categoriesNames) {
                        CategoryChip(categoryName = it)
                    }
                }
                MovieInfoSection(
                    modifier = Modifier.padding(top = 8.dp),
                    releaseDate = state.releaseDate,
                    movieLength = state.movieLength,
                    originCountry = state.originCountry
                )
                DescriptionSection(
                    modifier = Modifier.padding(top = 24.dp),
                    description = state.description
                )
                CastSection(
                    modifier = Modifier.padding(top = 24.dp),
                    actors = state.actors
                )
                Spacer(modifier = Modifier.padding(top = 24.dp).requiredWidth(screenWidthDp).height(1.dp).background(AppTheme.color.stroke))
                MovieExtrasSection(modifier = Modifier.padding(top = 12.dp),extras = state.extraItem){}
            }

        }
    }

}


@Composable
fun PlayButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = CircleShape
            )
            .padding(2.dp)
            .border(2.dp, AppTheme.color.stroke, shape = CircleShape)
            .dropShadow(CircleShape, Color(0x1F8951FF), spread = 12.dp)
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = CircleShape
            )

    ) {
        Icon(
            painterResource(R.drawable.ic_play),
            contentDescription = null,
            tint = AppTheme.color.primary,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
        )
    }
}

@Composable
fun CategoryChip(modifier: Modifier = Modifier, categoryName: String) {
    Row(
        modifier = modifier
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            categoryName,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.primary
        )
    }
}

@Composable
fun MovieInfoSection(
    releaseDate: String,
    movieLength: String,
    originCountry: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(releaseDate, movieLength, originCountry)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEachIndexed { index, item ->
            Text(
                text = item,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.hint
            )

            if (index < items.lastIndex) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(AppTheme.color.stroke, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
fun DescriptionSection(modifier: Modifier = Modifier, description: String) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.description),
            style = AppTheme.textStyle.headline.small,
            color = AppTheme.color.title,
        )
        ExpandableText(text = description)
    }
}


@Composable
fun CastSection(modifier: Modifier = Modifier, actors: List<ActorUiState>) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.cast),
                style = AppTheme.textStyle.headline.small,
                color = AppTheme.color.title,
            )
            Text(
                text = stringResource(R.string.all),
                style = AppTheme.textStyle.label.medium,
                color = AppTheme.color.primary,
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(actors) {
                ActorCard(actor = it)
            }
        }
    }
}

@Composable
fun ActorCard(modifier: Modifier = Modifier, actor: ActorUiState) {
    Column(modifier = modifier) {
        SafeImageView(
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(16.dp)),
            model = actor.photo,
            contentDescription = actor.name
        )
        Text(
            text = actor.name,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.body
        )
    }
}

@Composable
fun MovieExtrasSection(modifier: Modifier = Modifier, extras: List<Selectable<Extras>>,onClickExtras :(Extras)->Unit) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        extras.forEach {
           val extrasSectionItemInfo =  it.extras.getExtrasSectionItemInfo()
            Chip(modifier = Modifier.size(70.dp,96.dp),icon = painterResource(extrasSectionItemInfo.iconResId),
                label = stringResource(extrasSectionItemInfo.textResId),
                isSelected = it.isSelected)
        }
    }
}



@Composable
@ThemeAndLocalePreviews
private fun SearchByActorContentPreview() {
    AflamiTheme {
        MovieContent(MovieDetailsUiState(imageUrl = "https://image.tmdb.org/t/p/w500/1GJvBE7UWU1WOVi0XREl4JQc7f8.jpg"))
    }
}

