package com.amsterdam.aflami.di

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.viewmodel.cast.CastScreenArgs
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMovieDetailsArgs
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsArgs
import com.amsterdam.viewmodel.game.whichGenre.GameGenreArgs
import com.amsterdam.viewmodel.gameEnd.GameResultArgs
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterGameArgs
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearGameArgs
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
        MovieDetailsArgs(savedStateHandle)

    @Provides
    fun provideCategoriesMoviesDetailsArgs(savedStateHandle: SavedStateHandle): CategoriesMovieDetailsArgs =
        CategoriesMovieDetailsArgs(savedStateHandle)

    @Provides
    fun provideSeriesDetailsArgs(savedStateHandle: SavedStateHandle): SeriesDetailsArgs =
        SeriesDetailsArgs(savedStateHandle)

    @Provides
    fun provideCategoriesTvShowsDetailsArgs(savedStateHandle: SavedStateHandle): CategoriesTvShowsDetailsArgs =
        CategoriesTvShowsDetailsArgs(savedStateHandle)

    @Provides
    fun provideCastScreenArgs(savedStateHandle: SavedStateHandle): CastScreenArgs =
        CastScreenArgs(savedStateHandle)

    @Provides
    fun provideGuessReleaseYearGameArgs(
        savedStateHandle: SavedStateHandle
    ): GuessReleaseYearGameArgs = GuessReleaseYearGameArgs(savedStateHandle)

    @Provides
    fun provideGuessCharacterGameArgs(
        savedStateHandle: SavedStateHandle
    ): GuessCharacterGameArgs = GuessCharacterGameArgs(savedStateHandle)

    @Provides
    fun provideListDetailsArgs(
        savedStateHandle: SavedStateHandle
    ): ListDetailsArgs = ListDetailsArgs(savedStateHandle)

    @Provides
    fun provideGameResultArgs(savedStateHandle: SavedStateHandle) = GameResultArgs(savedStateHandle)

    @Provides
    fun provideGameArgs(
        savedStateHandle: SavedStateHandle
    ): GameGenreArgs = GameGenreArgs(savedStateHandle)

}

@Module
@InstallIn(SingletonComponent::class)
object ViewModelSingletonScope {
    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}