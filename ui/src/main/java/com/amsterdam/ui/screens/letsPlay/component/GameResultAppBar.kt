package com.amsterdam.ui.screens.letsPlay.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.designsystem.R as DesignSystemR


@Composable
fun GameResultAppBar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        containerColor = Color.Transparent,
        title = {
            Text(
                text = stringResource(R.string.guess_character_game_title),
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title,
                fontSize = 20.sp
            )
        },
        leadingIcon = {
            IconButton(
                painter = painterResource(id = DesignSystemR.drawable.ic_cancel),
                contentDescription = null,
                onClick = onCloseClicked,
                tint = AppTheme.color.title
            )
        }
    )
}


@ThemeAndLocalePreviews
@Composable
private fun GameResultAppBarPreview() {
    AflamiTheme {
        GameResultAppBar(onCloseClicked = {})
    }
}