package com.example.viewmodel.cast

import com.example.viewmodel.shared.BaseViewModel


sealed interface CastUiEffect : BaseViewModel.BaseUiEffect {
    object NavigateBack : CastUiEffect
}