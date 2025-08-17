package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.repository.WishListRepository
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.viewmodel.utils.timer.TimerHandler
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.authentication.LoginAsGuestUseCase
import com.amsterdam.domain.useCase.authentication.LoginWithPasswordUseCase
import com.amsterdam.domain.useCase.authentication.LogoutUseCase
import com.amsterdam.domain.useCase.common.AddMovieWatchHistoryUseCase
import com.amsterdam.domain.useCase.common.AddTvShowWatchHistoryUseCase
import com.amsterdam.domain.useCase.details.GetEpisodeVideosUseCase
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.details.GetMoviesByGenreUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.details.GetTvShowsByGenreUseCase
import com.amsterdam.domain.useCase.game.GetAvailableGamesUseCase
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.domain.useCase.game.guessByPoster.DoGuessMovieByPosterHintUseCase
import com.amsterdam.domain.useCase.game.guessByPoster.GenerateMoviePosterQuestionsUseCase
import com.amsterdam.domain.useCase.game.guessByPoster.GuessMovieByPosterGameUseCase
import com.amsterdam.domain.useCase.game.guessByPoster.SubmitGuessMovieByPosterAnswerUseCase
import com.amsterdam.domain.useCase.game.character.DoGuessCharacterGameHintUseCase
import com.amsterdam.domain.useCase.game.character.GenerateCharacterQuestionsUseCase
import com.amsterdam.domain.useCase.game.character.GuessCharacterGameUseCase
import com.amsterdam.domain.useCase.game.character.SubmitCharacterAnswerUseCase
import com.amsterdam.domain.useCase.game.releaseYear.DoGuessReleaseGameHintUseCase
import com.amsterdam.domain.useCase.game.releaseYear.GenerateMovieReleaseYearQuestionsUseCase
import com.amsterdam.domain.useCase.game.releaseYear.GuessReleaseYearGameUseCase
import com.amsterdam.domain.useCase.game.releaseYear.SubmitGuessReleaseYearAnswerUseCase
import com.amsterdam.domain.useCase.game.whichGenre.DoGuessGenreGameHintUseCase
import com.amsterdam.domain.useCase.game.whichGenre.GenerateMovieGenreQuestionsUseCase
import com.amsterdam.domain.useCase.game.whichGenre.GuessMovieGenreUseCase
import com.amsterdam.domain.useCase.game.whichGenre.SubmitGuessMovieGenreAnswerUseCase
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
import com.amsterdam.domain.useCase.list.AddMovieToListUseCase
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.DeleteListUseCase
import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase
import com.amsterdam.domain.useCase.list.GetWishListsUseCase
import com.amsterdam.domain.useCase.list.RemoveMovieFromListUseCase
import com.amsterdam.domain.useCase.myRating.movie.DeleteUserRatedMovieUseCase
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase
import com.amsterdam.domain.useCase.myRating.movie.SetUserMovieRatingUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.DeleteUserRatedTvShowUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.SetUserTvShowRatingUseCase
import com.amsterdam.domain.useCase.preferences.GetOnboardingStatusUseCase
import com.amsterdam.domain.useCase.preferences.ManageAppThemeUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageRestrictionLevelUseCase
import com.amsterdam.domain.useCase.preferences.SetOnboardingCompletedUseCase
import com.amsterdam.domain.useCase.profile.GetAccountDetailsUseCase
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.AddPointsToGameUseCase
import com.amsterdam.domain.useCase.game.AddSecondToGameTimeUseCase
import com.amsterdam.domain.useCase.game.EvaluateWinConditionUseCase
import com.amsterdam.domain.useCase.game.CreateGameSessionIdUseCase
import com.amsterdam.domain.useCase.game.GetCollectedPointsUseCase
import com.amsterdam.domain.useCase.game.GetSpentSecondsUseCase
import com.amsterdam.domain.useCase.list.CheckIsMovieInListUseCase
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
    fun provideGetEpisodeVideosUseCase(tvShowRepository: TvShowRepository): GetEpisodeVideosUseCase =
        GetEpisodeVideosUseCase(tvShowRepository)

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
        addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase,
    ): GetTvShowDetailsUseCase =
        GetTvShowDetailsUseCase(tvShowRepository, addTvShowWatchHistoryUseCase)

    @Provides
    fun provideGetUpcomingMoviesUseCase(movieRepository: MovieRepository): GetUpcomingMoviesUseCase =
        GetUpcomingMoviesUseCase(movieRepository)

    @Provides
    fun provideGetTopRatedMoviesUseCase(movieRepository: MovieRepository): GetTopRatedMoviesUseCase =
        GetTopRatedMoviesUseCase(movieRepository)

    @Provides
    fun provideGetMoviesByGenreUseCase(movieRepository: MovieRepository): GetMoviesByGenreUseCase =
        GetMoviesByGenreUseCase(movieRepository)

    @Provides
    fun provideGetContinueWatchingMoviesUseCase(watchHistoryRepository: WatchHistoryRepository): GetContinueWatchingMoviesUseCase =
        GetContinueWatchingMoviesUseCase(watchHistoryRepository)

    @Provides
    fun provideGetTvShowByGenreUseCase(tvShowRepository: TvShowRepository): GetTvShowsByGenreUseCase =
        GetTvShowsByGenreUseCase(tvShowRepository)

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
        wishListRepository: WishListRepository,
    ): GetListMediaItemsFromListUseCase = GetListMediaItemsFromListUseCase(wishListRepository)

    @Provides
    fun provideDeleteListUseCase(
        wishListRepository: WishListRepository,
    ): DeleteListUseCase = DeleteListUseCase(wishListRepository)

    @Provides
    fun provideGetTopRatedTvShowsUseCase(tvShowRepository: TvShowRepository): GetTopRatedTvShowsUseCase =
        GetTopRatedTvShowsUseCase(tvShowRepository)

    @Provides
    fun provideGetPopularTvShowsUseCase(tvShowRepository: TvShowRepository): GetPopularTvShowsUseCase =
        GetPopularTvShowsUseCase(tvShowRepository)

    @Provides
    fun provideRemoveMovieFromListUseCase(
        wishListRepository: WishListRepository,
    ): RemoveMovieFromListUseCase = RemoveMovieFromListUseCase(wishListRepository)

    @Provides
    fun provideCheckIsMovieInListUseCase(
        wishListRepository: WishListRepository
    ): CheckIsMovieInListUseCase = CheckIsMovieInListUseCase(wishListRepository)

    @Provides
    fun provideGetHomeScreenDataUseCase(
        getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
        getPopularMoviesUseCase: GetPopularMoviesUseCase,
        getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
        getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
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
        addMovieWatchHistoryUseCase: AddMovieWatchHistoryUseCase,
    ): GetMovieDetailsUseCase =
        GetMovieDetailsUseCase(movieRepository, addMovieWatchHistoryUseCase)

    @Provides
    fun provideGetTopRatedScreenDataUseCase(
        getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
    ): GetTopRatedScreenDataUseCase =
        GetTopRatedScreenDataUseCase(getTopRatedMoviesUseCase, getTopRatedTvShowsUseCase)

    @Provides
    fun provideGetUserListsUseCase(
        wishListRepository: WishListRepository,
    ): GetWishListsUseCase =
        GetWishListsUseCase(wishListRepository)

    @Provides
    fun provideAddMovieToListUseCase(wishListRepository: WishListRepository): AddMovieToListUseCase =
        AddMovieToListUseCase(wishListRepository)

    @Provides
    fun provideCreateNewListUseCase(wishListRepository: WishListRepository): CreateNewListUseCase =
        CreateNewListUseCase(wishListRepository)


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

    @Provides
    fun provideGetTotalUserPointsUseCase(gameRepository: GameRepository) =
        GetTotalUserPointsUseCase(gameRepository)

    @Provides
    fun provideGetAvailableGamesUseCase() = GetAvailableGamesUseCase()

    @Provides
    fun provideGenerateMovieReleaseYearQuestionsUseCase(
        gameRepository: GameRepository,
        getGameDifficultyByDifficultyType: GetGameDifficultyByDifficultyTypeUseCase,
    ) = GenerateMovieReleaseYearQuestionsUseCase(gameRepository, getGameDifficultyByDifficultyType)

    @Provides
    fun provideGetGameDifficultyByDifficultyTypeUseCase() =
        GetGameDifficultyByDifficultyTypeUseCase()

    @Provides
    fun provideTimerHandler() = TimerHandler()

    @Provides
    fun provideUpdateUserGamePointsUseCase(gameRepository: GameRepository) =
        UpdateUserGamePointsUseCase(gameRepository)

    @Provides
    fun provideDoGuessReleaseGameHintUseCase(
        getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
        updatePoints: UpdateUserGamePointsUseCase,
    ) = DoGuessReleaseGameHintUseCase(getTotalUserPointsUseCase, updatePoints)

    @Provides
    fun provideSubmitGuessReleaseYearAnswerUseCase(
        getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
        updatePoints: UpdateUserGamePointsUseCase,
    ) = SubmitGuessReleaseYearAnswerUseCase(getDifficulty, updatePoints)

    @Provides
    fun provideGuessReleaseYearGameUseCase(
        getGameData: GenerateMovieReleaseYearQuestionsUseCase,
        doHint: DoGuessReleaseGameHintUseCase,
        submitAnswer: SubmitGuessReleaseYearAnswerUseCase,
    ) = GuessReleaseYearGameUseCase(getGameData, doHint, submitAnswer)


    @Provides
    fun provideGenerateMoviePosterQuestionsUseCase(
        gameRepository: GameRepository,
        getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    ): GenerateMoviePosterQuestionsUseCase =
        GenerateMoviePosterQuestionsUseCase(
            gameRepository,
            getGameDifficultyByDifficultyTypeUseCase
        )

    @Provides
    fun provideDoGuessCharacterGameHintUseCase(
        getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
        updatePoints: UpdateUserGamePointsUseCase,
    ) = DoGuessCharacterGameHintUseCase(
        getTotalUserPointsUseCase,
        updatePoints
    )

    @Provides
    fun provideGenerateCharacterQuestions(
        gameRepository: GameRepository,
        getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
    ) = GenerateCharacterQuestionsUseCase(gameRepository, getDifficulty)

    @Provides
    fun provideSubmitCharacterAnswerUseCase(
        getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
        updatePoints: UpdateUserGamePointsUseCase,
    ) = SubmitCharacterAnswerUseCase(getDifficulty, updatePoints)

    @Provides
    fun providesGuessCharacterGameUseCase(
        getGameData: GenerateCharacterQuestionsUseCase,
        doHint: DoGuessCharacterGameHintUseCase,
        submitAnswer: SubmitCharacterAnswerUseCase,
    ) = GuessCharacterGameUseCase(
        getGameData,
        doHint,
        submitAnswer
    )

    @Provides
    fun provideSubmitGuessMovieByPosterAnswerUseCase(
        getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
        updatePoints: UpdateUserGamePointsUseCase,
    ): SubmitGuessMovieByPosterAnswerUseCase =
        SubmitGuessMovieByPosterAnswerUseCase(getDifficulty, updatePoints)

    @Provides
    fun provideGuessMovieByPosterGameUseCase(
        getGameData: GenerateMoviePosterQuestionsUseCase,
        doHint: DoGuessMovieByPosterHintUseCase,
        submitAnswer: SubmitGuessMovieByPosterAnswerUseCase,
    ): GuessMovieByPosterGameUseCase =
        GuessMovieByPosterGameUseCase(getGameData, doHint, submitAnswer)

    @Provides
    fun provideGenerateMovieGenreQuestionsUseCase(
        getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase,
        gameRepository: GameRepository,
    ) = GenerateMovieGenreQuestionsUseCase(getGameDifficultyUseCase, gameRepository)

    @Provides
    fun provideDoGuessGenreGameHintUseCase(
        getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
        updateUserGamePointsUseCase: UpdateUserGamePointsUseCase,
    ) = DoGuessGenreGameHintUseCase(
        getTotalUserPointsUseCase,
        updateUserGamePointsUseCase
    )

    @Provides
    fun provideSubmitGuessMovieGenreAnswerUseCase(
        getDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase,
        updateUserGamePointsUseCase: UpdateUserGamePointsUseCase,
    ) = SubmitGuessMovieGenreAnswerUseCase(getDifficultyUseCase, updateUserGamePointsUseCase)

    @Provides
    fun provideGuessMovieGenreUseCase(
        generateMovieGenreQuestionsUseCase: GenerateMovieGenreQuestionsUseCase,
        submitGuessMovieGenreAnswerUseCase: SubmitGuessMovieGenreAnswerUseCase,
        doGuessGenreGameHintUseCase: DoGuessGenreGameHintUseCase,
    ) = GuessMovieGenreUseCase(
        generateMovieGenreQuestionsUseCase,
        submitGuessMovieGenreAnswerUseCase,
        doGuessGenreGameHintUseCase
    )

    @Provides
    fun provideDoGuessMovieByPosterHintUseCase(
        getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
        updatePoints: UpdateUserGamePointsUseCase,
    ): DoGuessMovieByPosterHintUseCase =
        DoGuessMovieByPosterHintUseCase(getTotalUserPointsUseCase, updatePoints)

    @Provides
    fun provideCreateGameSessionIdUseCase() = CreateGameSessionIdUseCase()

    @Provides
    fun provideAddSecondToGameTimeUseCase(
        gameRepository: GameRepository
    ): AddSecondToGameTimeUseCase = AddSecondToGameTimeUseCase(gameRepository)

    @Provides
    fun provideGetSpentSecondsUseCase(
        gameRepository: GameRepository
    ): GetSpentSecondsUseCase = GetSpentSecondsUseCase(gameRepository)

    @Provides
    fun provideAddPointsToGameUseCase(
        gameRepository: GameRepository
    ): AddPointsToGameUseCase = AddPointsToGameUseCase(gameRepository)

    @Provides
    fun provideGetCollectedPointsUseCase(
        gameRepository: GameRepository
    ): GetCollectedPointsUseCase = GetCollectedPointsUseCase(gameRepository)

    @Provides
    fun provideEvaluateWinConditionUseCase(
        gameRepository: GameRepository
    ): EvaluateWinConditionUseCase = EvaluateWinConditionUseCase(gameRepository)
}



