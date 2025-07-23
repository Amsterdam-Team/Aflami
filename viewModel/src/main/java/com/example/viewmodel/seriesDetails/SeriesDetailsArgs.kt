package com.example.viewmodel.seriesDetails

import androidx.lifecycle.SavedStateHandle

class SeriesDetailsArgs(savedStateHandle: SavedStateHandle){
    val tvShowId = savedStateHandle.get<Long>(TV_SHOW_ID_ARGS)

    companion object{
        const val TV_SHOW_ID_ARGS = "tvShowId"
    }
}