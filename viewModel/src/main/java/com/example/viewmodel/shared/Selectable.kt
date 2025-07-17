package com.example.viewmodel.shared

data class Selectable<T>(
    val isSelected: Boolean = true,
    val type: T
)