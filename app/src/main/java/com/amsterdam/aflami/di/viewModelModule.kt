package com.amsterdam.aflami.di

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.ui.screens.cast.CastScreenArgsImpl
import com.amsterdam.ui.screens.categoriesDetails.movies.CategoriesMovieDetailsArgsImpl
import com.amsterdam.ui.screens.categoriesDetails.tvShow.CategoriesTvShowsDetailsArgsImpl
import com.amsterdam.ui.screens.games.character.GuessCharacterGameArgsImpl
import com.amsterdam.ui.screens.games.guessByPoster.GuessMovieByPosterGameArgsImpl
import com.amsterdam.ui.screens.games.releaseYear.GuessReleaseYearGameArgsImpl
import com.amsterdam.ui.screens.games.guessGenre.GameGenreArgsImpl
import com.amsterdam.ui.screens.letsPlay.GameResultArgsImpl
import com.amsterdam.ui.screens.listDetails.ListDetailsArgsImpl
import com.amsterdam.ui.screens.movieDetails.MovieDetailsArgsImpl
import com.amsterdam.ui.screens.seriesDetails.SeriesDetailsArgsImpl
import com.amsterdam.viewmodel.cast.CastScreenArgs
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMovieDetailsArgs
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsArgs
import com.amsterdam.viewmodel.gameResult.GameResultArgs
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterGameArgs
import com.amsterdam.viewmodel.guessMovieByPosterGame.GuessMovieByPosterGameArgs
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearGameArgs
import com.amsterdam.viewmodel.guessWhichGenre.GameGenreArgs
import com.amsterdam.viewmodel.listDetails.ListDetailsArgs
import com.amsterdam.viewmodel.movieDetails.MovieDetailsArgs
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsArgs
import com.amsterdam.viewmodel.utils.dispatcher.DefaultDispatcherProvider
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelViewModelScope {

    @Provides
    fun provideMovieDetailsArgs(savedStateHandle: SavedStateHandle): MovieDetailsArgs =
        MovieDetailsArgsImpl(savedStateHandle)

    @Provides
    fun provideCategoriesMoviesDetailsArgs(savedStateHandle: SavedStateHandle): CategoriesMovieDetailsArgs =
        CategoriesMovieDetailsArgsImpl(savedStateHandle)

    @Provides
    fun provideSeriesDetailsArgs(savedStateHandle: SavedStateHandle): SeriesDetailsArgs =
        SeriesDetailsArgsImpl(savedStateHandle)

    @Provides
    fun provideCategoriesTvShowsDetailsArgs(savedStateHandle: SavedStateHandle): CategoriesTvShowsDetailsArgs =
        CategoriesTvShowsDetailsArgsImpl(savedStateHandle)

    @Provides
    fun provideCastScreenArgs(savedStateHandle: SavedStateHandle): CastScreenArgs =
        CastScreenArgsImpl(savedStateHandle)

    @Provides
    fun provideGuessReleaseYearGameArgs(
        savedStateHandle: SavedStateHandle
    ): GuessReleaseYearGameArgs = GuessReleaseYearGameArgsImpl(savedStateHandle)

    @Provides
    fun provideGuessCharacterGameArgs(
        savedStateHandle: SavedStateHandle
    ): GuessCharacterGameArgs = GuessCharacterGameArgsImpl(savedStateHandle)

    @Provides
    fun provideListDetailsArgs(
        savedStateHandle: SavedStateHandle
    ): ListDetailsArgs = ListDetailsArgsImpl(savedStateHandle)

    @Provides
    fun provideGameResultArgs(savedStateHandle: SavedStateHandle
    ): GameResultArgs = GameResultArgsImpl(savedStateHandle)

    @Provides
    fun provideGuessMovieByPosterGameArgs(
        savedStateHandle: SavedStateHandle
    ): GuessMovieByPosterGameArgs = GuessMovieByPosterGameArgsImpl(savedStateHandle)

    @Provides
    fun provideGameArgs(
        savedStateHandle: SavedStateHandle
    ): GameGenreArgs = GameGenreArgsImpl(savedStateHandle)

}

@Module
@InstallIn(SingletonComponent::class)
object ViewModelSingletonScope {
    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}