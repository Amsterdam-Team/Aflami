package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.domain.useCase.common.AddMovieWatchHistoryUseCase
import com.amsterdam.domain.useCase.common.AddTvShowWatchHistoryUseCase
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
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
import com.amsterdam.domain.useCase.preferences.GetOnboardingStatusUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.SetOnboardingCompletedUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetAndFilterTvShowsByKeywordUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetsSessionType(repo: AuthenticationRepository): GetsSessionType =
        GetsSessionType(repo)

    @Provides
    fun provideGetOnboardingStatusUseCase(repo: AppPreferencesRepository): GetOnboardingStatusUseCase =
        GetOnboardingStatusUseCase(repo)

    @Provides
    fun provideSetOnboardingCompletedUseCase(repo: AppPreferencesRepository): SetOnboardingCompletedUseCase =
        SetOnboardingCompletedUseCase(repo)


    @Provides
    fun provideSetCurrentLanguage(repo: AppPreferencesRepository): ManageLocaleLanguageUseCase =
        ManageLocaleLanguageUseCase(repo)

    @Provides
    fun provideLoginAsGuestUseCase(repo: AuthenticationRepository): LoginAsGuestUseCase =
        LoginAsGuestUseCase(repo)

    @Provides
    fun provideLoginWithPasswordUseCase(repo: AuthenticationRepository): LoginWithPasswordUseCase =
        LoginWithPasswordUseCase(repo)

    @Provides
    fun provideAddMovieWatchHistoryUseCase(watchHistoryRepository: WatchHistoryRepository): AddMovieWatchHistoryUseCase =
        AddMovieWatchHistoryUseCase(watchHistoryRepository)

    @Provides
    fun provideAddTvShowWatchHistoryUseCase(watchHistoryRepository: WatchHistoryRepository): AddTvShowWatchHistoryUseCase =
        AddTvShowWatchHistoryUseCase(watchHistoryRepository)

    @Provides
    fun provideGetAndFilterMoviesByKeywordUseCase(repo: MovieRepository): GetAndFilterMoviesByKeywordUseCase =
        GetAndFilterMoviesByKeywordUseCase(repo)

    @Provides
    fun provideGetMoviesByCountryUseCase(repo: MovieRepository): GetMoviesByCountryUseCase =
        GetMoviesByCountryUseCase(repo)

    @Provides
    fun provideGetMovieCastUseCase(repo: MovieRepository): GetMovieCastUseCase =
        GetMovieCastUseCase(repo)

    @Provides
    fun provideGetMoviesByActorUseCase(repo: MovieRepository): GetMoviesByActorUseCase =
        GetMoviesByActorUseCase(repo)

    @Provides
    fun provideGetSuggestedCountriesUseCase(repo: CountryRepository): GetSuggestedCountriesUseCase =
        GetSuggestedCountriesUseCase(repo)

    @Provides
    fun provideRecentSearchesUseCase(repo: RecentSearchRepository): RecentSearchesUseCase =
        RecentSearchesUseCase(repo)

    @Provides
    fun provideGetAndFilterTvShowsByKeywordUseCase(repo: TvShowRepository): GetAndFilterTvShowsByKeywordUseCase =
        GetAndFilterTvShowsByKeywordUseCase(repo)

    @Provides
    fun provideGetPopularMoviesUseCase(repo: MovieRepository): GetPopularMoviesUseCase =
        GetPopularMoviesUseCase(repo)

    @Provides
    fun provideGetEpisodesBySeasonNumberUseCase(repo: TvShowRepository): GetEpisodesBySeasonNumberUseCase =
        GetEpisodesBySeasonNumberUseCase(repo)

    @Provides
    fun provideGetTvShowDetailsUseCase(
        repo: TvShowRepository,
        addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase
    ): GetTvShowDetailsUseCase =
        GetTvShowDetailsUseCase(repo, addTvShowWatchHistoryUseCase)

    @Provides
    fun provideGetUpcomingMoviesUseCase(repo: MovieRepository): GetUpcomingMoviesUseCase =
        GetUpcomingMoviesUseCase(repo)

    @Provides
    fun provideGetTopRatedMoviesUseCase(repo: MovieRepository): GetTopRatedMoviesUseCase =
        GetTopRatedMoviesUseCase(repo)

    @Provides
    fun provideGetContinueWatchingMoviesUseCase(repo: WatchHistoryRepository): GetContinueWatchingMoviesUseCase =
        GetContinueWatchingMoviesUseCase(repo)

    @Provides
    fun provideGetTvShowCastUseCase(tvShowRepository: TvShowRepository): GetTvShowCastUseCase =
        GetTvShowCastUseCase(tvShowRepository)

    @Provides
    fun provideGetMoviesByMoodUseCase(repo: MovieRepository): GetMoviesByMoodUseCase =
        GetMoviesByMoodUseCase(repo)

    @Provides
    fun provideGetContinueWatchingTvShowsUseCase(watchHistoryRepository: WatchHistoryRepository): GetContinueWatchingTvShowsUseCase =
        GetContinueWatchingTvShowsUseCase(watchHistoryRepository)

    @Provides
    fun provideGetTopRatedTvShowsUseCase(
        tvShowRepository: TvShowRepository
    ): GetTopRatedTvShowsUseCase = GetTopRatedTvShowsUseCase(tvShowRepository)

    @Provides
    fun provideGetPopularTvShowsUseCase(
        tvShowRepository: TvShowRepository
    ): GetPopularTvShowsUseCase = GetPopularTvShowsUseCase(tvShowRepository)

    @Provides
    fun provideGetHomeScreenDataUseCase(
        getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
        getPopularMoviesUseCase: GetPopularMoviesUseCase,
        getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
        getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    ): GetHomeScreenDataUseCase =
        GetHomeScreenDataUseCase(
            getTopRatedMoviesUseCase,
            getTopRatedTvShowsUseCase,
            getPopularMoviesUseCase,
            getPopularTvShowsUseCase,
            getUpcomingMoviesUseCase
        )

    @Provides
    fun provideGetContinueWatchingScreenDataUseCase(
        getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase,
        getContinueWatchingTvShowsUseCase: GetContinueWatchingTvShowsUseCase,
    ) = GetContinueWatchingScreenDataUseCase(
        getContinueWatchingMoviesUseCase,
        getContinueWatchingTvShowsUseCase
    )

    @Provides
    fun provideGetMovieDetailsUseCase(
        movieRepository: MovieRepository,
        addWatchHistoryUseCase: AddMovieWatchHistoryUseCase
    ) = GetMovieDetailsUseCase(
        movieRepository,
        addWatchHistoryUseCase
    )

    @Provides
    fun provideGetTopRatedScreenDataUseCase(
        getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase
    ) = GetTopRatedScreenDataUseCase(
        getTopRatedMoviesUseCase,
        getTopRatedTvShowsUseCase
    )

}