package com.example.designsystem.components.globalSearchHub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun GlobalSearchHub(
    globalSearchHubUI: GlobalSearchHubUI, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(globalSearchHubUI.gradient), alpha = 0.8f
            )
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(globalSearchHubUI.icon),
                contentDescription = null,
                modifier = Modifier.height(40.dp),
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = stringResource(globalSearchHubUI.labelRes),
                style = AppTheme.textStyle.title.small,
                color = AppTheme.color.onPrimary
            )
            Text(
                text = stringResource(globalSearchHubUI.descriptionRes),
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.onPrimaryBody
            )
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.38f)
                .fillMaxWidth(0.20f)
                .blur(56.dp, BlurredEdgeTreatment.Unbounded)
                .background(color = AppTheme.color.onPrimary, CircleShape)
                .alpha(0.12f)
                .align(Alignment.TopEnd)
        )
    }
}


@ThemeAndLocalePreviews
@Composable
private fun GlobalSearchHubWorldPreview() {
    AflamiTheme {
        GlobalSearchHub(
            globalSearchHubUI = GlobalSearchHubUI.WORLD, modifier = Modifier.size(160.dp, 100.dp)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GlobalSearchHubActorPreview() {
    AflamiTheme {
        GlobalSearchHub(
            globalSearchHubUI = GlobalSearchHubUI.ACTOR, modifier = Modifier.size(160.dp, 100.dp)
        )
    }
}