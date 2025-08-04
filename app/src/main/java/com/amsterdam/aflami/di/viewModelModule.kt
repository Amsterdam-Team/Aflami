package com.amsterdam.aflami.di

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.viewmodel.cast.CastScreenArgs
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
    fun provideMovieDetailsArgs(savedStateHandle: SavedStateHandle): MovieDetailsArgs = MovieDetailsArgs(savedStateHandle)

    @Provides
    fun provideSeriesDetailsArgs(savedStateHandle: SavedStateHandle): SeriesDetailsArgs = SeriesDetailsArgs(savedStateHandle)

    @Provides
    fun provideCastScreenArgs(savedStateHandle: SavedStateHandle): CastScreenArgs = CastScreenArgs(savedStateHandle)

    @Provides
    fun provideListDetailsArgs(
        savedStateHandle: SavedStateHandle
    ): ListDetailsArgs = ListDetailsArgs(savedStateHandle)
}

@Module
@InstallIn(SingletonComponent::class)
object ViewModelSingletonScope {
    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()
}