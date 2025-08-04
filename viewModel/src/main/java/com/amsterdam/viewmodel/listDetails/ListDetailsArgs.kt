package com.amsterdam.viewmodel.listDetails

import androidx.lifecycle.SavedStateHandle

class ListDetailsArgs(savedStateHandle: SavedStateHandle) {
    val listId = savedStateHandle.get<Long>(LIST_ID_ARG) ?: 0
    val listName = savedStateHandle.get<String>(LIST_NAME_ARG) ?: ""

    companion object {
        const val LIST_ID_ARG = "listId"
        const val LIST_NAME_ARG = "listName"
    }
}