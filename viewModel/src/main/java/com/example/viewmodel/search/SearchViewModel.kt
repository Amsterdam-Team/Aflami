package com.example.viewmodel.search

import com.example.domain.GetMoviesByKeywordUseCase
import com.example.domain.GetTvShowByKeywordUseCase
import com.example.viewmodel.BaseViewModel

class SearchViewModel(
    private val getMoviesByKeywordUseCase: GetMoviesByKeywordUseCase,
    private val getTvShowByKeywordUseCase: GetTvShowByKeywordUseCase
) : BaseViewModel<SearchUiState>(SearchUiState()) {


}