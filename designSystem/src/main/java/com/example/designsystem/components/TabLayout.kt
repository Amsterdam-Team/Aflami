package com.example.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import com.example.designsystem.components.divider.HorizontalDivider
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun TabsLayout(
    tabs: List<String>,
    selectedIndex: Int,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier,
    tabTopPadding: Dp = 8.dp,
    tabBottomPadding: Dp = 13.dp,
    tabsEndSpace: Dp = 16.dp,
    selectedTextColor: Color = AppTheme.color.title,
    unselectedTextColor: Color = AppTheme.color.hint,
    containerColor: Color = AppTheme.color.surface,
    indicatorColor: Color = AppTheme.color.secondary,
    dividerColor: Color = AppTheme.color.stroke
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            modifier = modifier.fillMaxWidth(),
            containerColor = containerColor,
            selectedTabIndex = selectedIndex,
            divider = {},
            indicator = { list ->
                TabRowDefaults.SecondaryIndicator(
                    height = 5.dp,
                    color = indicatorColor,
                    modifier = Modifier
                        .tabIndicatorOffset(list[selectedIndex])
                        .padding(start = 24.dp, end = 40.dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                )
            }
        ) {
            tabs.fastForEachIndexed { index, text ->
                Tab(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = if (index == tabs.lastIndex) 0.dp else tabsEndSpace),
                    selected = index == selectedIndex,
                    onClick = { onSelectTab(index) },
                    selectedContentColor = selectedTextColor,
                    unselectedContentColor = unselectedTextColor,
                ) {
                    Text(
                        modifier = Modifier.padding(
                            top = tabTopPadding,
                            bottom = tabBottomPadding
                        ),
                        text = text,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = dividerColor)
    }
}


@ThemeAndLocalePreviews
@Composable
private fun TabLayoutPreview() {
    AflamiTheme {
        var selectedIndex by remember { mutableIntStateOf(0) }
        TabsLayout(
            modifier = Modifier.fillMaxWidth(),
            tabs = listOf("tab1", "tab2", "tab3ddasdasdasas", "tab4"),
            selectedIndex = selectedIndex,
            onSelectTab = { selectedIndex = it },
        )
    }
}