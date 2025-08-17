package com.amsterdam.viewmodel.movieDetails

import app.cash.turbine.test
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.list.AddMovieToListUseCase
import com.amsterdam.domain.useCase.list.CheckIsMovieInListUseCase
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.GetWishListsUseCase
import com.amsterdam.domain.useCase.myRating.movie.SetUserMovieRatingUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.Movie
import com.amsterdam.entity.Review
import com.amsterdam.entity.WishList
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class MovieDetailsViewModelTest {
    private val getsSessionType: GetsSessionType = mockk(relaxed = true)
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mockk(relaxed = true)
    private val addMovieToListUseCase: AddMovieToListUseCase = mockk(relaxed = true)
    private val getWishListsUseCase: GetWishListsUseCase = mockk(relaxed = true)
    private val createNewListUseCase: CreateNewListUseCase = mockk(relaxed = true)
    private val setUserMovieRatingUseCase: SetUserMovieRatingUseCase = mockk(relaxed = true)
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private val checkIsMovieInListUseCase: CheckIsMovieInListUseCase = mockk(relaxed = true)
    private var testArgs: MovieDetailsArgs = mockk(relaxed = true)

    private val viewModel by lazy {
        MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getWishListsUseCase = getWishListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            checkIsMovieInListUseCase = checkIsMovieInListUseCase,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @BeforeEach
    fun setUp() {
        every { testArgs.movieId } returns movieId
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(Language.ENGLISH)
    }

    //region Initialization Tests
    @Test
    fun `init should update state with movie details when success loaded the data`() = runTest {
        coEvery { testArgs.movieId } returns movieId
        coEvery { getMovieDetailsUseCase.invoke(movieId) } returns movieDetails

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.movieId).isEqualTo(movieId)
    }

    @Test
    fun `init should update error state when failed load the data`() = runTest {
        coEvery { testArgs.movieId } returns movieId
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NoInternetException()

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.networkError).isTrue()
    }

    @Test
    fun `init should stop loading when failed load the data`() = runTest {
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NoInternetException()

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoading).isFalse()
    }


    @Test
    fun `init should set isMovieInList to true when checkIsMovieInListUseCase returns true`() =
        runTest {
            coEvery { testArgs.movieId } returns movieId
            coEvery { getWishListsUseCase() } returns listOf(wishList)
            coEvery { checkIsMovieInListUseCase.invoke(movieId, listId) } returns true

            viewModel
            advanceUntilIdle()

            assertThat(viewModel.state.value.userLists.first().isMovieInList).isTrue()
        }

    @Test
    fun `init should set isMovieInList to false when checkIsMovieInListUseCase returns false`() =
        runTest {
            coEvery { testArgs.movieId } returns movieId
            coEvery { getWishListsUseCase() } returns listOf(wishList)
            coEvery { checkIsMovieInListUseCase.invoke(movieId, listId) } returns false

            viewModel
            advanceUntilIdle()

            assertThat(viewModel.state.value.userLists.first().isMovieInList).isFalse()
        }

    @Test
    fun `init should handle NetworkException`() = runTest {
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NetworkException()

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.networkError).isTrue()
    }

    //endregion


    @Test
    fun `onClickPlayVideo should send LaunchVideoEffect when called`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onClickPlayVideo()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.LaunchMovieVideoEffect(""))
        }
    }

    @Test
    fun `onClick Retry Request should call loadMovieDetails`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onClickRetryRequest()
        coVerify(exactly = 1) { getMovieDetailsUseCase(any()) }
    }


    @Test
    fun `onClickCancel should set dialog visibility to false`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onClickCancel()

        assertThat(viewModel.state.value.isAddToListDialogVisible).isFalse()
    }

    @Test
    fun `onClickMovieExtras should update the state to be true for selected movie extras`() =
        runTest {
            // Given
            val selectedExtras = MovieExtras.REVIEWS
            viewModel
            advanceUntilIdle()

            viewModel.onClickMovieExtras(selectedExtras)
            advanceUntilIdle()

            val selectedItem = viewModel.state.value.extraItem.find { it.item == selectedExtras }
            assertThat(selectedItem?.isSelected).isTrue()
        }

    @Test
    fun `onClickShowAllCast should send NavigateToCastsScreenEffect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickShowAllCast()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.NavigateToCastsScreenEffect)
        }
    }

    @Test
    fun `onClickBack should send NavigateBackEffect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickBack()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.NavigateBackEffect)
        }
    }

    @Test
    fun `onRateClicked should show login dialog when user is a guest`() = runTest {
        coEvery { getsSessionType.invoke() } returns SessionType.GUEST
        viewModel
        advanceUntilIdle()

        viewModel.onClickRate()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoginDialogVisible).isTrue()
    }

    @Test
    fun `onClickNavigateToLogin should send NavigateToLoginScreenEffect when its call`() = runTest {
        viewModel.effect.test {
            viewModel.onClickNavigateToLogin()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.NavigateToLoginScreenEffect)
        }
    }

    @Test
    fun `onClickSimilarMovie should send NavigateToMovieDetailsEffect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickSimilarMovie(similarMovieId)
            assertThat(awaitItem()).isEqualTo(
                MovieDetailsEffect.NavigateToMovieDetails(
                    similarMovieId
                )
            )
        }
    }

    @Test
    fun `onDescriptionExpansionToggled should toggle isDescriptionExpanded state`() = runTest {
        viewModel
        advanceUntilIdle()
        assertThat(viewModel.state.value.isDescriptionExpanded).isFalse()

        viewModel.onDescriptionExpansionToggled()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isDescriptionExpanded).isTrue()
    }


    @Test
    fun `onReviewExpansionToggled should toggle isReviewExpanded to true when reviewId is equal selected review`() =
        runTest {
            coEvery { getMovieDetailsUseCase(movieId) } returns movieDetails
            viewModel
            advanceUntilIdle()
            assertThat(viewModel.state.value.reviews.first().isExpanded).isFalse()

            viewModel.onReviewExpansionToggled("me")
            advanceUntilIdle()

            assertThat(viewModel.state.value.reviews.first().isExpanded).isTrue()
        }

    @Test
    fun `onReviewExpansionToggled should toggle isReviewExpanded to false when reviewId is not equal selected review`() =
        runTest {
            coEvery { getMovieDetailsUseCase(movieId) } returns movieDetails
            viewModel
            advanceUntilIdle()
            assertThat(viewModel.state.value.reviews.first().isExpanded).isFalse()

            viewModel.onReviewExpansionToggled("notMe")
            advanceUntilIdle()

            assertThat(viewModel.state.value.reviews.first().isExpanded).isFalse()
        }

    @Test
    fun `onClickAddToList should show add to list dialog when user is logged in`() = runTest {
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        coEvery { getWishListsUseCase.invoke() } returns emptyList()
        viewModel
        advanceUntilIdle()

        viewModel.onClickAddToList()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isAddToListDialogVisible).isTrue()
    }

    @Test
    fun `onClickAddToList should return early when screen is loading`() = runTest {
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        coEvery { getWishListsUseCase() } returns emptyList()
        viewModel

        viewModel.onClickAddToList()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isAddToListDialogVisible).isFalse()
    }

    @Test
    fun `onClickAddToList should show login dialog when user is a guest`() = runTest {
        coEvery { getsSessionType.invoke() } returns SessionType.GUEST
        viewModel
        advanceUntilIdle()

        viewModel.onClickAddToList()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoginDialogVisible).isTrue()
    }

    @Test
    fun `onSaveMovieToList should call addMovieToListUseCase and send success effect`() = runTest {
        coEvery { addMovieToListUseCase.invoke(any(), any()) } just Runs

        viewModel.effect.test {
            viewModel.onSaveMovieToList(listId, listIds = listOf(movieId))
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.MovieAddedToListSuccessfully)
        }
    }

    @Test
    fun `onSaveMovieToList should send error effect when it fails`() = runTest {
        coEvery { addMovieToListUseCase.invoke(any(), any()) } throws AflamiException()

        viewModel.effect.test {
            viewModel.onSaveMovieToList(listId, listIds = listOf(movieId))
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.MovieAddedToListError)
        }
    }

    @Test
    fun `onSaveMovieToList should hide add to list dialog if it success`() = runTest {
        coEvery { addMovieToListUseCase.invoke(any(), any()) } just Runs

        assertThat(viewModel.state.value.isAddToListDialogVisible).isFalse()
    }

    @Test
    fun `onSaveMovieToList should increase item count in list if success`() = runTest {
        coEvery { addMovieToListUseCase.invoke(any(), any()) } just Runs
        coEvery { getWishListsUseCase() } returns wishLists

        viewModel.onSaveMovieToList(movieId, listOf(1L, 2L))
        advanceUntilIdle()

        assertThat(viewModel.state.value.userLists.first().itemCount).isEqualTo(2)
    }

    @Test
    fun `onSaveMovieToList should not increase item count in list if not selected`() = runTest {
        coEvery { addMovieToListUseCase.invoke(any(), any()) } just Runs
        coEvery { getWishListsUseCase() } returns wishLists

        viewModel.onSaveMovieToList(movieId, listOf(2L))
        advanceUntilIdle()

        assertThat(viewModel.state.value.userLists.first().itemCount).isEqualTo(1)
    }

    @Test
    fun `onClickCreateList should show create new list dialog`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onClickCreateList()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isCreateNewListDialogVisible).isTrue()
        assertThat(viewModel.state.value.isAddToListDialogVisible).isFalse()
    }

    @Test
    fun `onChangeListName should update listName in state`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onChangeListName(newListName)
        advanceUntilIdle()

        assertThat(viewModel.state.value.listName).isEqualTo(newListName)
    }

    @Test
    fun `createListUseCase should be called with current list name when creating new list`() =
        runTest {
            coEvery { createNewListUseCase.invoke(newListName) } returns newListId
            viewModel.onChangeListName(newListName)

            viewModel.onClickCreateNewList()
            advanceUntilIdle()

            coVerify { createNewListUseCase.invoke(newListName) }
        }

    //region onClickCreateNewList Tests
    @Test
    fun `onClickCreateNewList movie should be added to newly created list`() = runTest {
        coEvery { createNewListUseCase.invoke(any()) } returns newListId
        coEvery { addMovieToListUseCase.invoke(newListId.toLong(), movieId) } returns Unit

        viewModel.onClickCreateNewList()
        advanceUntilIdle()

        coVerify { addMovieToListUseCase.invoke(newListId.toLong(), movieId) }
    }

    @Test
    fun `onClickCreateNewList should emit ListCreatedSuccessfully effect when list creation succeeds`() =
        runTest {
            coEvery { createNewListUseCase.invoke(any()) } returns newListId
            coEvery { addMovieToListUseCase.invoke(any(), any()) } returns Unit

            viewModel.effect.test {
                viewModel.onClickCreateNewList()
                assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ListCreatedSuccessfully)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onClickCreateNewList should emit MovieAddedToListSuccessfully effect when movie is added`() =
        runTest {
            coEvery { createNewListUseCase.invoke(any()) } returns newListId
            coEvery { addMovieToListUseCase.invoke(any(), any()) } returns Unit

            viewModel.effect.test {
                viewModel.onClickCreateNewList()
                skipItems(1) // Skip the ListCreatedSuccessfully effect
                assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.MovieAddedToListSuccessfully)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onClickCreateNewList should hide create list dialog after successful operation`() =
        runTest {
            coEvery { createNewListUseCase.invoke(any()) } returns newListId
            coEvery { addMovieToListUseCase.invoke(any(), any()) } returns Unit

            viewModel.onClickCreateNewList()
            advanceUntilIdle()

            assertThat(viewModel.state.value.isCreateNewListDialogVisible).isFalse()
        }

    @Test
    fun `onClickCreateNewList should send FailedToCreateList effect on error`() = runTest {
        coEvery { createNewListUseCase.invoke(any()) } throws AflamiException()

        viewModel.effect.test {
            viewModel.onClickCreateNewList()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.FailedToCreateList)
        }
    }

    //endregion

    @Test
    fun `onSelectedListChange should update selectedList in state`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onSelectedListChange(listOf(selectedList))
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedLists).isEqualTo(listOf(selectedList))
    }

    @Test
    fun `onClickSubmit should call setUserRatingUseCase and send success effect`() = runTest {
        coEvery { setUserMovieRatingUseCase.setUserMovieRate(any(), any()) } returns Unit

        viewModel.effect.test {
            viewModel.onChangeRating(newRate = 4)
            viewModel.onClickSubmit()
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ShowRatingSuccessSnackBar)
        }
    }

    @Test
    fun `onClickSubmit should call setUserRatingUseCase with zero when rate is null`() = runTest {
        coEvery { setUserMovieRatingUseCase.setUserMovieRate(any(), any()) } returns Unit
        viewModel

        viewModel.onClickSubmit()
        advanceUntilIdle()

        coVerify { setUserMovieRatingUseCase.setUserMovieRate(0, any()) }
    }


    @Test
    fun `onClickSubmit should call onSubmitRateError and send error effect when setUserMovieRate fails`() =
        runTest {
            coEvery {
                setUserMovieRatingUseCase.setUserMovieRate(
                    any(),
                    any()
                )
            } throws AflamiException()

            viewModel.effect.test {
                viewModel.onChangeRating(newRate = 4)
                viewModel.onClickSubmit()
                assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.ShowRatingErrorSnackBar)
            }
        }

    //region Rating Tests
    @Test
    fun `onClickCancelRateDialog should set dialog visible to false when called`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onClickCancelRateDialog()
        advanceUntilIdle()

        assertThat(viewModel.state.value.rateDialogUiState.isVisible).isFalse()
    }


    @Test
    fun `onClickRate should Set Rate Dialog Visible True When User Is LoggedIn`() = runTest {
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        viewModel
        advanceUntilIdle()

        viewModel.onClickRate()
        advanceUntilIdle()

        assertThat(viewModel.state.value.rateDialogUiState.isVisible).isTrue()
    }

    @Test
    fun `onClickRate should Set Is Login Dialog Visible True When User Is Guest`() = runTest {
        coEvery { getsSessionType.invoke() } returns SessionType.GUEST
        viewModel
        advanceUntilIdle()

        viewModel.onClickRate()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoginDialogVisible).isTrue()
    }

    @Test
    fun `onChangeRating should Update Selected Star Index In State`() = runTest {
        val newRating = 4
        viewModel
        advanceUntilIdle()

        viewModel.onChangeRating(newRating)
        advanceUntilIdle()

        assertThat(viewModel.state.value.rateDialogUiState.selectedStarIndex).isEqualTo(newRating)
    }

    @Test
    fun `onChangeRating should Set Is Submitting Enabled True When Rating Changes`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onChangeRating(4)
        advanceUntilIdle()

        assertThat(viewModel.state.value.rateDialogUiState.isSubmittingEnabled).isTrue()
    }

    @Test
    fun `onChangeRating should Set Is Changed to false When Rating Does not change`() = runTest {
        viewModel
        advanceUntilIdle()

        viewModel.onChangeRating(4)
        advanceUntilIdle()

        viewModel.onClickRate()
        advanceUntilIdle()

        viewModel.onChangeRating(4)
        advanceUntilIdle()

        assertThat(viewModel.state.value.rateDialogUiState.isSubmittingEnabled).isFalse()
    }

//endregion

    private val movieId: Long = 1
    private val similarMovieId: Long = 200
    private val newListName = "My New List"

    private val movieDetails = GetMovieDetailsUseCase.MovieDetails(
        movie = Movie(
            id = movieId,
            name = "Test Movie",
            description = "Description",
            posterUrl = "",
            releaseDate = LocalDate(2023, 1, 1),
            categories = listOf(MovieGenre.ACTION),
            rating = 8.0f,
            popularity = 100.0,
            originCountry = "US",
            runTimeInMinutes = 120
        ),
        reviews = listOf(
            Review(
                id = 1L,
                reviewerName = "me",
                reviewerUsername = "me",
                rating = 0F,
                content = "",
                date = LocalDate(2023, 1, 1),
                imageUrl = ""
            ),
            Review(
                id = 2L,
                reviewerName = "notMe",
                reviewerUsername = "notMe",
                rating = 0F,
                content = "",
                date = LocalDate(2023, 1, 1),
                imageUrl = ""
            )
        ),
        actors = emptyList(),
        similarMovies = emptyList(),
        movieGallery = emptyList(),
        moviePosters = emptyList(),
        productionCompanies = emptyList(),
        userRate = null
    )

    private val selectedList = WishListUiState(id = 1L, name = "Test List")
    private val wishList = WishList(
        id = 1,
        name = "Test List",
        description = "",
        itemCount = 1
    )
    private val wishLists = listOf(
        wishList,
        wishList.copy(id = 2)
    )
    private val newListId = 5
    private val listId = 1L


}