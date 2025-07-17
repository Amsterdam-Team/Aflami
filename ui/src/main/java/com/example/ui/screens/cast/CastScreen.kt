package com.example.ui.screens.cast

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.components.NoDataContainer
import com.example.designsystem.components.NoNetworkContainer
import com.example.designsystem.components.Text
import com.example.designsystem.components.appBar.DefaultAppBar
import com.example.designsystem.theme.AppTheme
import com.example.imageviewer.ui.SafeImageView
import com.example.ui.application.LocalNavController
import com.example.viewmodel.cast.CastErrorUiState
import com.example.viewmodel.cast.CastInteractionListener
import com.example.viewmodel.cast.CastUiEffect
import com.example.viewmodel.cast.CastUiState
import com.example.viewmodel.cast.CastViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun CastScreen(
    viewModel: CastViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            if (effect is CastUiEffect.NavigateBack) navController.popBackStack()
        }
    }

    CastContent(state = state, interaction = viewModel,)
}

@Composable
private fun CastContent(
    state: CastUiState,
    interaction: CastInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = AppTheme.color.surface)
            .padding(horizontal = 16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultAppBar(
            title = stringResource(R.string.cast),
            onNavigateBackClicked = interaction::onNavigateBackClicked
        )

        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = Triple(state.isLoading, state.errorUiState, state.cast),
            transitionSpec = {
                fadeIn(tween(1700)) togetherWith fadeOut(tween(1700))
            },
        ) { (isLoading, errorState, cast) ->

            when {
                isLoading -> LoadingContainer()

                errorState == CastErrorUiState.NoCastFound -> {
                    NoDataContainer(
                        modifier = Modifier.fillMaxSize(),
                        title = stringResource(R.string.cast_not_available),
                        description = stringResource(R.string.we_could_not_find_cast_information),
                        imageRes = painterResource(id = R.drawable.placeholder_no_result_found)
                    )
                }

                errorState == CastErrorUiState.NoNetworkConnection -> {
                    NoNetworkContainer(
                        modifier = Modifier.fillMaxSize(),
                        onClickRetry = interaction::onRetrySearchClick
                    )
                }

                cast.isNotEmpty() -> {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Adaptive(104.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cast) { actor ->
                            ActorCard(actorImage = actor.actorImage, actorName = actor.actorName)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ActorCard(actorImage: String, actorName: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SafeImageView(
            modifier = Modifier
                .fillMaxWidth()
                .height(122.dp)
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp)),
            contentDescription = actorName,
            model = actorImage,
            contentScale = ContentScale.Crop,
        )

        Text(
            text = actorName,
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.body,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}