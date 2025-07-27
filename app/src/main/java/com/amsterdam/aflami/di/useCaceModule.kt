package com.amsterdam.aflami.di


import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.domain.useCase.common.AddWatchHistoryUseCase
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetsSessionType)
    singleOf(::LoginAsGuestUseCase)
    singleOf(::LoginWithPasswordUseCase)
    singleOf(::GetAndFilterMoviesByKeywordUseCase)
    singleOf(::GetMoviesByCountryUseCase)
    singleOf(::GetMovieCastUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetMoviesByActorUseCase)
    singleOf(::GetSuggestedCountriesUseCase)
    singleOf(::RecentSearchesUseCase)
    singleOf(::GetAndFilterTvShowsByKeywordUseCase)
    singleOf(::GetPopularMoviesUseCase)
    singleOf(::GetEpisodesBySeasonNumberUseCase)
    singleOf(::GetTvShowDetailsUseCase)
    singleOf(::GetUpcomingMoviesUseCase)
    singleOf(::GetTopRatedMoviesUseCase)
    singleOf(::GetContinueWatchingMoviesUseCase)
    singleOf(::AddWatchHistoryUseCase)
    singleOf(::GetMoviesByMoodUseCase)
}