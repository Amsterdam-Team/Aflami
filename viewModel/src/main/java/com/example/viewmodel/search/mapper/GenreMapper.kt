package com.example.viewmodel.search.mapper

import com.example.viewmodel.search.searchByKeyword.genre.MovieGenre
import com.example.viewmodel.search.searchByKeyword.genre.MovieGenreItemUiState
import com.example.viewmodel.search.searchByKeyword.genre.Selectable
import com.example.viewmodel.search.searchByKeyword.genre.TvGenreItemUiState
import com.example.viewmodel.search.searchByKeyword.genre.TvShowGenre

fun TvShowGenre.mapToGenreId(): Int {
    return when (this) {
        TvShowGenre.ACTION_ADVENTURE -> 10759
        TvShowGenre.ANIMATION -> 16
        TvShowGenre.COMEDY -> 35
        TvShowGenre.CRIME -> 80
        TvShowGenre.DOCUMENTARY -> 99
        TvShowGenre.DRAMA -> 18
        TvShowGenre.FAMILY -> 10751
        TvShowGenre.KIDS -> 10762
        TvShowGenre.MYSTERY -> 9648
        TvShowGenre.NEWS -> 10763
        TvShowGenre.REALITY -> 10764
        TvShowGenre.SCIENCE_FICTION_FANTASY -> 10765
        TvShowGenre.SOAP -> 10766
        TvShowGenre.TALK -> 10767
        TvShowGenre.WAR_POLITICS -> 10768
        TvShowGenre.WESTERN -> 37
        TvShowGenre.ROMANCE -> 10749
        TvShowGenre.ALL -> 0
    }
}

fun MovieGenre.mapToGenreId(): Int {
    return when (this) {
        MovieGenre.ACTION -> 28
        MovieGenre.ADVENTURE -> 12
        MovieGenre.ANIMATION -> 16
        MovieGenre.COMEDY -> 35
        MovieGenre.CRIME -> 80
        MovieGenre.DOCUMENTARY -> 99
        MovieGenre.DRAMA -> 18
        MovieGenre.FAMILY -> 10751
        MovieGenre.FANTASY -> 14
        MovieGenre.HISTORY -> 36
        MovieGenre.HORROR -> 27
        MovieGenre.MUSIC -> 10402
        MovieGenre.MYSTERY -> 9648
        MovieGenre.ROMANCE -> 10749
        MovieGenre.SCIENCE_FICTION -> 878
        MovieGenre.TV_MOVIE -> 10770
        MovieGenre.THRILLER -> 53
        MovieGenre.WAR -> 10752
        MovieGenre.WESTERN -> 37
        MovieGenre.ALL -> 0
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