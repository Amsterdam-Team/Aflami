package com.amsterdam.aflami.di

import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.domain.useCase.common.AddMovieWatchHistoryUseCase
import com.amsterdam.domain.useCase.common.AddTvShowWatchHistoryUseCase
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingTvShowsUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.home.GetPopularTvShowsUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedTvShowsUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetsSessionType)
    singleOf(::LoginAsGuestUseCase)
    singleOf(::LoginWithPasswordUseCase)
    singleOf(::GetAndFilterMoviesByKeywordUseCase)
    singleOf(::GetMoviesByCountryUseCase)
    singleOf(::GetMovieCastUseCase)
    singleOf(::GetTvShowCastUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetMoviesByActorUseCase)
    singleOf(::GetSuggestedCountriesUseCase)
    singleOf(::RecentSearchesUseCase)
    singleOf(::GetAndFilterTvShowsByKeywordUseCase)
    singleOf(::GetPopularMoviesUseCase)
    singleOf(::GetPopularTvShowsUseCase)
    singleOf(::GetEpisodesBySeasonNumberUseCase)
    singleOf(::GetTvShowDetailsUseCase)
    singleOf(::GetUpcomingMoviesUseCase)
    singleOf(::GetTopRatedMoviesUseCase)
    singleOf(::GetContinueWatchingMoviesUseCase)
    singleOf(::GetContinueWatchingTvShowsUseCase)
    singleOf(::GetTopRatedTvShowsUseCase)
    singleOf(::GetHomeScreenDataUseCase)
    singleOf(::AddMovieWatchHistoryUseCase)
    singleOf(::AddTvShowWatchHistoryUseCase)
    singleOf(::GetMoviesByMoodUseCase)
    singleOf(::GetTopRatedScreenDataUseCase)
    singleOf(::GetContinueWatchingScreenDataUseCase)
}