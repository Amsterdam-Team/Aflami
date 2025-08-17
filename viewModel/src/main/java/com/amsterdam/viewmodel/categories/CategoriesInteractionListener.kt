package com.amsterdam.viewmodel.categories

import com.amsterdam.viewmodel.shared.TabOption

interface CategoriesInteractionListener {
    fun onChangeTabOption(tabOption: TabOption)
    fun onClickMovieGenreCard(genreName: String)
    fun onClickTvShowGenreCard(genreName: String)
}