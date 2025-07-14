package com.example.viewmodel.common

data class Selectable<T>(
    val isSelected: Boolean,
    val item: T
)
