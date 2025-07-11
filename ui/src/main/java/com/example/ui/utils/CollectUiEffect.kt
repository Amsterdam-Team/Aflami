package com.example.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CollectUiEffect(
    effect: SharedFlow<BaseViewModel.BaseUiEffect>,
    effectHandler: (effect: BaseViewModel.BaseUiEffect) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        effect.collectLatest { effect ->
            effectHandler(effect)
        }
    }
}