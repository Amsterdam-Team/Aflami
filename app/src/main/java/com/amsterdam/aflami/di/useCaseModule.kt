package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.domain.useCase.authentication.LogoutUseCase
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
import com.amsterdam.domain.useCase.myRating.movie.DeleteUserRatedMovieUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase
import com.amsterdam.domain.useCase.myRating.movie.SetUserMovieRatingUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.DeleteUserRatedTvShowUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.SetUserTvShowRatingUseCase
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.AddMovieToListUseCase
import com.amsterdam.domain.useCase.list.DeleteListUseCase
import com.amsterdam.domain.useCase.list.GetMoviesFromListUseCase
import com.amsterdam.domain.useCase.list.GetUserListsUseCase
import com.amsterdam.domain.useCase.list.RemoveMovieFromListUseCase
import com.amsterdam.domain.useCase.preferences.GetOnboardingStatusUseCase
import com.amsterdam.domain.useCase.preferences.ManageAppThemeUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageRestrictionLevelUseCase
import com.amsterdam.domain.useCase.preferences.SetOnboardingCompletedUseCase
import com.amsterdam.domain.useCase.profile.GetAccountDetailsUseCase
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
    fun provideManageRestrictionLevelUseCase(appPreferencesRepository: AppPreferencesRepository): ManageRestrictionLevelUseCase =
        ManageRestrictionLevelUseCase(appPreferencesRepository)


    @Provides
    fun provideGetsSessionType(authenticationRepository: AuthenticationRepository): GetsSessionType =
        GetsSessionType(authenticationRepository)

    @Provides
    fun provideGetOnboardingStatusUseCase(appPreferencesRepository: AppPreferencesRepository): GetOnboardingStatusUseCase =
        GetOnboardingStatusUseCase(appPreferencesRepository)

    @Provides
    fun provideSetOnboardingCompletedUseCase(appPreferencesRepository: AppPreferencesRepository): SetOnboardingCompletedUseCase =
        SetOnboardingCompletedUseCase(appPreferencesRepository)


    @Provides
    fun provideSetCurrentLanguage(appPreferencesRepository: AppPreferencesRepository): ManageLocaleLanguageUseCase =
        ManageLocaleLanguageUseCase(appPreferencesRepository)

    @Provides
    fun provideLoginAsGuestUseCase(authenticationRepository: AuthenticationRepository): LoginAsGuestUseCase =
        LoginAsGuestUseCase(authenticationRepository)

    @Provides
    fun provideManageAppThemeUseCase(repo: AppPreferencesRepository): ManageAppThemeUseCase =
        ManageAppThemeUseCase(repo)

    @Provides
    fun provideLoginWithPasswordUseCase(authenticationRepository: AuthenticationRepository): LoginWithPasswordUseCase =
        LoginWithPasswordUseCase(authenticationRepository)

    @Provides
    fun provideLogoutUseCase(authenticationRepository: AuthenticationRepository): LogoutUseCase =
        LogoutUseCase(authenticationRepository)

    @Provides
    fun provideAddMovieWatchHistoryUseCase(watchHistoryRepository: WatchHistoryRepository): AddMovieWatchHistoryUseCase =
        AddMovieWatchHistoryUseCase(watchHistoryRepository)

    @Provides
    fun provideAddTvShowWatchHistoryUseCase(watchHistoryRepository: WatchHistoryRepository): AddTvShowWatchHistoryUseCase =
        AddTvShowWatchHistoryUseCase(watchHistoryRepository)

    @Provides
    fun provideGetAndFilterMoviesByKeywordUseCase(movieRepository: MovieRepository): GetAndFilterMoviesByKeywordUseCase =
        GetAndFilterMoviesByKeywordUseCase(movieRepository)

    @Provides
    fun provideGetMoviesByCountryUseCase(movieRepository: MovieRepository): GetMoviesByCountryUseCase =
        GetMoviesByCountryUseCase(movieRepository)

    @Provides
    fun provideGetMovieCastUseCase(movieRepository: MovieRepository): GetMovieCastUseCase =
        GetMovieCastUseCase(movieRepository)

    @Provides
    fun provideGetMoviesByActorUseCase(movieRepository: MovieRepository): GetMoviesByActorUseCase =
        GetMoviesByActorUseCase(movieRepository)

    @Provides
    fun provideGetSuggestedCountriesUseCase(countryRepository: CountryRepository): GetSuggestedCountriesUseCase =
        GetSuggestedCountriesUseCase(countryRepository)

    @Provides
    fun provideRecentSearchesUseCase(recentSearchRepository: RecentSearchRepository): RecentSearchesUseCase =
        RecentSearchesUseCase(recentSearchRepository)

    @Provides
    fun provideGetAndFilterTvShowsByKeywordUseCase(tvShowRepository: TvShowRepository): GetAndFilterTvShowsByKeywordUseCase =
        GetAndFilterTvShowsByKeywordUseCase(tvShowRepository)

    @Provides
    fun provideGetPopularMoviesUseCase(movieRepository: MovieRepository): GetPopularMoviesUseCase =
        GetPopularMoviesUseCase(movieRepository)

    @Provides
    fun provideGetEpisodesBySeasonNumberUseCase(tvShowRepository: TvShowRepository): GetEpisodesBySeasonNumberUseCase =
        GetEpisodesBySeasonNumberUseCase(tvShowRepository)

    @Provides
    fun provideGetTvShowDetailsUseCase(
        tvShowRepository: TvShowRepository,
        addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase
    ): GetTvShowDetailsUseCase =
        GetTvShowDetailsUseCase(tvShowRepository, addTvShowWatchHistoryUseCase)

    @Provides
    fun provideGetUpcomingMoviesUseCase(movieRepository: MovieRepository): GetUpcomingMoviesUseCase =
        GetUpcomingMoviesUseCase(movieRepository)

    @Provides
    fun provideGetTopRatedMoviesUseCase(movieRepository: MovieRepository): GetTopRatedMoviesUseCase =
        GetTopRatedMoviesUseCase(movieRepository)

    @Provides
    fun provideGetContinueWatchingMoviesUseCase(watchHistoryRepository: WatchHistoryRepository): GetContinueWatchingMoviesUseCase =
        GetContinueWatchingMoviesUseCase(watchHistoryRepository)

    @Provides
    fun provideGetTvShowCastUseCase(tvShowRepository: TvShowRepository): GetTvShowCastUseCase =
        GetTvShowCastUseCase(tvShowRepository)

    @Provides
    fun provideGetMoviesByMoodUseCase(movieRepository: MovieRepository): GetMoviesByMoodUseCase =
        GetMoviesByMoodUseCase(movieRepository)

    @Provides
    fun provideGetContinueWatchingTvShowsUseCase(watchHistoryRepository: WatchHistoryRepository): GetContinueWatchingTvShowsUseCase =
        GetContinueWatchingTvShowsUseCase(watchHistoryRepository)

    @Provides
    fun provideGetMoviesFromListUseCase(
        userListRepository: UserListRepository
    ): GetMoviesFromListUseCase = GetMoviesFromListUseCase(userListRepository)

    @Provides
    fun provideDeleteListUseCase(
        userListRepository: UserListRepository
    ): DeleteListUseCase = DeleteListUseCase(userListRepository)

    @Provides
    fun provideGetTopRatedTvShowsUseCase(tvShowRepository: TvShowRepository): GetTopRatedTvShowsUseCase =
        GetTopRatedTvShowsUseCase(tvShowRepository)

    @Provides
    fun provideGetPopularTvShowsUseCase(tvShowRepository: TvShowRepository): GetPopularTvShowsUseCase =
        GetPopularTvShowsUseCase(tvShowRepository)

    @Provides
    fun provideRemoveMovieFromListUseCase(
        userListRepository: UserListRepository
    ): RemoveMovieFromListUseCase = RemoveMovieFromListUseCase(userListRepository)

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
    ): GetContinueWatchingScreenDataUseCase =
        GetContinueWatchingScreenDataUseCase(
            getContinueWatchingMoviesUseCase,
            getContinueWatchingTvShowsUseCase
        )

    @Provides
    fun provideGetMovieDetailsUseCase(
        movieRepository: MovieRepository,
        addMovieWatchHistoryUseCase: AddMovieWatchHistoryUseCase
    ): GetMovieDetailsUseCase =
        GetMovieDetailsUseCase(movieRepository, addMovieWatchHistoryUseCase)

    @Provides
    fun provideGetTopRatedScreenDataUseCase(
        getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase
    ): GetTopRatedScreenDataUseCase =
        GetTopRatedScreenDataUseCase(getTopRatedMoviesUseCase, getTopRatedTvShowsUseCase)

    @Provides
    fun provideGetUserListsUseCase(
        userListRepository: UserListRepository,
    ): GetUserListsUseCase =
        GetUserListsUseCase(userListRepository)
    @Provides
    fun provideAddMovieToListUseCase(userListRepository: UserListRepository): AddMovieToListUseCase =
        AddMovieToListUseCase(userListRepository)

    @Provides
    fun provideCreateNewListUseCase(userListRepository: UserListRepository): CreateNewListUseCase =
        CreateNewListUseCase(userListRepository)


    @Provides
    fun provideGetUserMovieRatingUseCase(
        movieRepository: MovieRepository,
    ) = GetUserRatedMoviesUseCase(movieRepository)

    @Provides
    fun provideSetUserMovieRatingUseCase(
        movieRepository: MovieRepository,
    ) = SetUserMovieRatingUseCase(movieRepository)

    @Provides
    fun provideDeleteUserMovieRateUseCase(
        movieRepository: MovieRepository,
    ) = DeleteUserRatedMovieUseCase(movieRepository)

    @Provides
    fun provideGetUserTvShowRatingUseCase(
        tvShowRepository: TvShowRepository,
    ) = GetUserRatedTvShowsUseCase(tvShowRepository)

    @Provides
    fun provideSetUserTvShowRatingUseCase(
        tvShowRepository: TvShowRepository,
    ) = SetUserTvShowRatingUseCase(tvShowRepository)

    @Provides
    fun provideDeleteUserTvShowRateUseCase(
        tvShowRepository: TvShowRepository,
    ) = DeleteUserRatedTvShowUseCase(tvShowRepository)

    @Provides
    fun provideGetAccountDetailsUseCase(repo: ProfileRepository): GetAccountDetailsUseCase =
        GetAccountDetailsUseCase(repo)

}
