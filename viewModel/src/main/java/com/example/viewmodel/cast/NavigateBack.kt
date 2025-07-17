package com.example.viewmodel.cast

import com.example.viewmodel.BaseViewModel

sealed interface CastUiEffect : BaseViewModel.BaseUiEffect {
    object NavigateBack : CastUiEffect
}