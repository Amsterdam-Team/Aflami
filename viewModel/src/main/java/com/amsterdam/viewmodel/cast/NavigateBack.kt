package com.amsterdam.viewmodel.cast

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface CastUiEffect : BaseViewModel.BaseUiEffect {
    data object NavigateBack : CastUiEffect
}