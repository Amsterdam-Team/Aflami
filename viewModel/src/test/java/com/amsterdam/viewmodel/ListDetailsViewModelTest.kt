package com.amsterdam.viewmodel


import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import app.cash.turbine.test
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.list.DeleteListUseCase
import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase
import com.amsterdam.domain.useCase.list.RemoveMovieFromListUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.viewmodel.listDetails.ListDetailsArgs
import com.amsterdam.viewmodel.listDetails.ListDetailsEffect
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState
import com.amsterdam.viewmodel.listDetails.ListDetailsViewModel
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListDetailsViewModelTest {

    private lateinit var viewModel: ListDetailsViewModel
    private val getListMediaItemsFromListUseCase: GetListMediaItemsFromListUseCase = mockk(relaxed = true)
    private val removeMovieFromListUseCase: RemoveMovieFromListUseCase = mockk(relaxed = true)
    private val deleteListUseCase: DeleteListUseCase = mockk(relaxed = true)
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private val args: ListDetailsArgs = mockk(relaxed = true)
    private val testDispatcherProvider = TestDispatcherProvider()
    private val testScope = TestScope(testDispatcherProvider.testDispatcher)

    @BeforeEach
    fun setup() {
        viewModel = ListDetailsViewModel(
            getListMediaItemsFromListUseCase = getListMediaItemsFromListUseCase,
            removeMovieFromListUseCase = removeMovieFromListUseCase,
            deleteListUseCase = deleteListUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider,
            args = args
        )
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should update state with args list id and name`() = testScope.runTest {
        val listId = 1L
        val listName = "List"
        every { args.listId } returns listId
        every { args.listName } returns listName

        viewModel = ListDetailsViewModel(
            getListMediaItemsFromListUseCase = getListMediaItemsFromListUseCase,
            removeMovieFromListUseCase = removeMovieFromListUseCase,
            deleteListUseCase = deleteListUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider,
            args = args
        )
        advanceUntilIdle()

        assertThat(viewModel.state.value.listId).isEqualTo(listId)
        assertThat(viewModel.state.value.listName).isEqualTo(listName)
    }

    @Test
    fun `onMovieClicked should send NavigateToDetails effect when its call`() = testScope.runTest {
        val movieId = 1L
        var effect: ListDetailsEffect? = null

        val job = launch {
            viewModel.effect.collect { listEffects -> effect = listEffects }
        }
        viewModel.onClickMovie(movieId)
        advanceUntilIdle()
        job.cancel()

        assertThat(effect).isEqualTo(ListDetailsEffect.NavigateToMovieDetailsScreen(1))
    }

    @Test
    fun `onNavigateBack should send NavigateBack effect when its called`() = testScope.runTest {
        var effect: ListDetailsEffect? = null
        val job = launch {
            viewModel.effect.collect { listEffects -> effect = listEffects }
        }

        viewModel.onClickBack()
        advanceUntilIdle()
        job.cancel()
        assertThat(effect).isEqualTo(ListDetailsEffect.NavigateBack)
    }

    @Test
    fun ` should send NavigateBack effect when its called`() = testScope.runTest {
        val effects = mutableListOf<ListDetailsEffect>()
        val job = launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.onClickBack()
        advanceUntilIdle()
        job.cancel()
        assertThat(effects).contains(ListDetailsEffect.NavigateBack)
    }

    @Test
    fun `onClickDeleteList should set showDeleteListDialog true`() = testScope.runTest {
        viewModel.onClickDeleteList()
        advanceUntilIdle()
        assertThat(viewModel.state.value.showDeleteListDialog).isTrue()
    }

    @Test
    fun `onDeleteListConfirmed should send show success snackbar`() =
        testScope.runTest {
            coEvery { deleteListUseCase(any()) } returns Unit

            val effects = mutableListOf<ListDetailsEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onDeleteListConfirmed()
            advanceUntilIdle()
            job.cancel()

            assertThat(viewModel.state.value.isDeleteLoading).isFalse()
            assertThat(viewModel.state.value.showDeleteListDialog).isFalse()
            assertThat(effects).contains(ListDetailsEffect.ShowDeletionSuccessSnackBar)
        }

    @Test
    fun `onClickRemoveMovie should show success snackbar`() = testScope.runTest {
        coEvery { removeMovieFromListUseCase(any(), any()) } returns Unit

        val effects = mutableListOf<ListDetailsEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        viewModel.onClickRemoveMovie(1)
        advanceUntilIdle()
        job.cancel()

        assertThat(viewModel.state.value.error).isNull()
        assertThat(effects).contains(ListDetailsEffect.ShowRemoveMovieSuccessSnackBar)
    }

    @Test
    fun `onDeleteDialogDismiss should set showDeleteListDialog false`() = testScope.runTest {
        viewModel.onDeleteListDialogDismiss()
        advanceUntilIdle()
        assertThat(viewModel.state.value.showDeleteListDialog).isFalse()
    }


    @Test
    fun `onDeleteListError should send show error snackbar`() = testScope.runTest {
        coEvery { deleteListUseCase(any()) } throws AflamiException()
        val effects = mutableListOf<ListDetailsEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        viewModel.onDeleteListConfirmed()
        advanceUntilIdle()
        job.cancel()

        assertThat(effects.any { it is ListDetailsEffect.ShowErrorSnackbar })
    }

    @Test
    fun `onRemoveMovieError should send show error snackbar`() = testScope.runTest {
        coEvery { removeMovieFromListUseCase(any(), any()) } throws AflamiException()
        val effects = mutableListOf<ListDetailsEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        viewModel.onClickRemoveMovie(1)
        advanceUntilIdle()
        job.cancel()

        assertThat(effects.any { it is ListDetailsEffect.ShowErrorSnackbar })
    }

    @Test
    fun `should set loading to true when pagination load changed to loading`() =
        testScope.runTest {
            val loadState = LoadState.Loading

            viewModel.onPagingLoadStateChanged(
                CombinedLoadStates(
                    refresh = loadState,
                    prepend = loadState,
                    append = loadState,
                    source =
                        LoadStates(loadState, loadState, loadState),
                    mediator = LoadStates(loadState, loadState, loadState),
                )
            )

            assertThat(viewModel.state.value.isLoading).isTrue()
        }

    @Test
    fun `should set loading to false when pagination load changed to not loading`() =
        testScope.runTest {
            val loadState = LoadState.NotLoading(true)

            viewModel.onPagingLoadStateChanged(
                CombinedLoadStates(
                    refresh = loadState,
                    prepend = loadState,
                    append = loadState,
                    source = LoadStates(loadState, loadState, loadState),
                    mediator = LoadStates(loadState, loadState, loadState),
                )
            )
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isFalse()
        }

    @Test
    fun `should set error ui state when pagination load changed to error`() = testScope.runTest {
        val loadState = LoadState.Error(error = NetworkException())
        loadState.error

        viewModel.onPagingLoadStateChanged(
            CombinedLoadStates(
                refresh = loadState,
                prepend = loadState,
                append = loadState,
                source = LoadStates(loadState, loadState, loadState),
                mediator = LoadStates(loadState, loadState, loadState),
            )
        )
        advanceUntilIdle()

        viewModel.state.test {
            assertThat(awaitItem().error).isEqualTo(ListDetailsUiState.ListDetailsError.NoNetwork)
        }
    }
}