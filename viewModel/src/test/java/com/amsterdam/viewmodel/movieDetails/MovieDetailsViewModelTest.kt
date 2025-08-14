package com.amsterdam.viewmodel.movieDetails

import app.cash.turbine.test
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.list.AddMovieToListUseCase
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.GetUserListsUseCase
import com.amsterdam.domain.useCase.myRating.movie.SetUserMovieRatingUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.Movie
import com.amsterdam.entity.Review
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
import kotlinx.datetime.toKotlinLocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class MovieDetailsViewModelTest {
    private val getsSessionType: GetsSessionType = mockk(relaxed = true)
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mockk(relaxed = true)
    private val addMovieToListUseCase: AddMovieToListUseCase = mockk(relaxed = true)
    private val getUserListsUseCase: GetUserListsUseCase = mockk(relaxed = true)
    private val createNewListUseCase: CreateNewListUseCase = mockk(relaxed = true)
    private val setUserMovieRatingUseCase: SetUserMovieRatingUseCase = mockk(relaxed = true)
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private var testArgs: MovieDetailsArgs = mockk(relaxed = true)

    private val viewModel by lazy {
        MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
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
    fun `init should handle NetworkException`() = runTest {
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NetworkException()

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.networkError).isTrue()
    }

    //endregion

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
    fun `onClickAddToList should show add to list dialog when user is logged in`() = runTest {
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        coEvery { getUserListsUseCase.invoke() } returns emptyList()
        viewModel
        advanceUntilIdle()

        viewModel.onClickAddToList()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isAddToListDialogVisible).isTrue()
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
            viewModel.onSaveMovieToList(listId, movieId)
            assertThat(awaitItem()).isEqualTo(MovieDetailsEffect.MovieAddedToListSuccessfully)
        }
    }

    @Test
    fun `onSaveMovieToList should hide add to list dialog if it success`() = runTest {
        coEvery { addMovieToListUseCase.invoke(any(), any()) } just Runs

        assertThat(viewModel.state.value.isAddToListDialogVisible).isFalse()
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

        viewModel.onSelectedListChange(selectedList)
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedList).isEqualTo(selectedList)
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

    //region Rating Tests
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
//endregion

    private val movieId: Long = 1
    private val reviewId = "1"
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
        reviews = emptyList(),
        actors = emptyList(),
        similarMovies = emptyList(),
        movieGallery = emptyList(),
        moviePosters = emptyList(),
        productionCompanies = emptyList(),
        userRate = null
    )

    val reviewUsername = "user1"

    val reviews = listOf(
        Review(
            id = 1L,
            reviewerName = "Author 1",
            reviewerUsername = reviewUsername,
            rating = 8.5f,
            content = "This is a great movie!",
            date = java.time.LocalDate.of(2023, 1, 1).toKotlinLocalDate(),
            imageUrl = "url1"
        ),
        Review(
            id = 2L,
            reviewerName = "Author 2",
            reviewerUsername = "user2",
            rating = 7.0f,
            content = "It was okay.",
            date = java.time.LocalDate.of(2023, 2, 1).toKotlinLocalDate(),
            imageUrl = "url2"
        )
    )

    private val selectedList = UserListUiState(id = 1L, name = "Test List")
    private val newListId = 5
    private val listId = 1L


}