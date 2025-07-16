package com.example.viewmodel.search.mapper

import com.example.entity.category.MovieCategoryType
import com.example.entity.category.TvShowCategoryType
import com.example.viewmodel.search.globalSearch.genre.MovieGenre
import com.example.viewmodel.search.globalSearch.genre.MovieGenreItemUiState
import com.example.viewmodel.search.globalSearch.genre.Selectable
import com.example.viewmodel.search.globalSearch.genre.TvGenreItemUiState
import com.example.viewmodel.search.globalSearch.genre.TvShowGenre

fun TvShowGenre.toTvShowCategoryType(): TvShowCategoryType {
    return when (this) {
        TvShowGenre.ACTION_ADVENTURE -> TvShowCategoryType.ACTION_ADVENTURE
        TvShowGenre.ANIMATION -> TvShowCategoryType.ANIMATION
        TvShowGenre.COMEDY -> TvShowCategoryType.COMEDY
        TvShowGenre.CRIME -> TvShowCategoryType.CRIME
        TvShowGenre.DOCUMENTARY -> TvShowCategoryType.DOCUMENTARY
        TvShowGenre.DRAMA -> TvShowCategoryType.DRAMA
        TvShowGenre.FAMILY -> TvShowCategoryType.FAMILY
        TvShowGenre.SCIENCE_FICTION_FANTASY -> TvShowCategoryType.SCIENCE_FICTION_FANTASY
        TvShowGenre.WAR_POLITICS -> TvShowCategoryType.WAR_POLITICS
        TvShowGenre.MYSTERY -> TvShowCategoryType.MYSTERY
        TvShowGenre.ROMANCE -> TvShowCategoryType.ROMANCE
        TvShowGenre.WESTERN -> TvShowCategoryType.WESTERN
        TvShowGenre.KIDS -> TvShowCategoryType.KIDS
        TvShowGenre.NEWS -> TvShowCategoryType.NEWS
        TvShowGenre.REALITY -> TvShowCategoryType.REALITY
        TvShowGenre.SOAP -> TvShowCategoryType.SOAP
        TvShowGenre.TALK -> TvShowCategoryType.TALK
        TvShowGenre.ALL -> TvShowCategoryType.ALL
    }
}

fun MovieGenre.toMovieCategoryType(): MovieCategoryType {
    return when (this) {
        MovieGenre.ACTION -> MovieCategoryType.ACTION
        MovieGenre.ADVENTURE -> MovieCategoryType.ADVENTURE
        MovieGenre.ANIMATION -> MovieCategoryType.ANIMATION
        MovieGenre.COMEDY -> MovieCategoryType.COMEDY
        MovieGenre.CRIME -> MovieCategoryType.CRIME
        MovieGenre.DOCUMENTARY -> MovieCategoryType.DOCUMENTARY
        MovieGenre.DRAMA -> MovieCategoryType.DRAMA
        MovieGenre.FAMILY -> MovieCategoryType.FAMILY
        MovieGenre.FANTASY -> MovieCategoryType.FANTASY
        MovieGenre.HISTORY -> MovieCategoryType.HISTORY
        MovieGenre.HORROR -> MovieCategoryType.HORROR
        MovieGenre.MUSIC -> MovieCategoryType.MUSIC
        MovieGenre.MYSTERY -> MovieCategoryType.MYSTERY
        MovieGenre.ROMANCE -> MovieCategoryType.ROMANCE
        MovieGenre.SCIENCE_FICTION -> MovieCategoryType.SCIENCE_FICTION
        MovieGenre.TV_MOVIE -> MovieCategoryType.TV_MOVIE
        MovieGenre.THRILLER -> MovieCategoryType.THRILLER
        MovieGenre.WAR -> MovieCategoryType.WAR
        MovieGenre.WESTERN -> MovieCategoryType.WESTERN
        MovieGenre.ALL -> MovieCategoryType.ALL
    }
}


fun List<MovieGenreItemUiState>.selectByMovieGenre(movieGenre: MovieGenre): List<MovieGenreItemUiState> {
    return this.map { movies ->
        movies.copy(
            selectableMovieGenre = Selectable(
                type = movies.selectableMovieGenre.type,
                isSelected = movies.selectableMovieGenre.type == movieGenre
            )
        )
    }
}

fun List<TvGenreItemUiState>.selectByTvGenre(tvGenre: TvShowGenre): List<TvGenreItemUiState> {
    return this.map { tvShows ->
        tvShows.copy(
            selectableTvShowGenre = Selectable(
                type = tvShows.selectableTvShowGenre.type,
                isSelected = tvShows.selectableTvShowGenre.type == tvGenre
            )
        )
    }
}


fun List<MovieGenreItemUiState>.getSelectedGenreType(): MovieGenre {
    return this.find { it.selectableMovieGenre.isSelected }?.selectableMovieGenre?.type
        ?: MovieGenre.ALL
}

fun List<TvGenreItemUiState>.getSelectedGenreType(): TvShowGenre {
    return this.find { it.selectableTvShowGenre.isSelected }?.selectableTvShowGenre?.type
        ?: TvShowGenre.ALL

}