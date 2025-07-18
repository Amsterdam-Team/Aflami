package com.example.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import androidx.compose.material3.Scaffold as MaterialScaffold

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    containerColor: Color = AppTheme.color.surface,
    contentColor: Color = Color.Unspecified,
    content: @Composable () -> Unit,
) {
    MaterialScaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        containerColor = containerColor,
        contentColor = contentColor,
        content = { content() },
    )
}

@Preview
@Composable
private fun ScaffoldPreview() {
    AflamiTheme {
        Scaffold(content = {
            Text("Hi")
        })
    }
}
