package com.amsterdam.viewmodel.lists

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.GetWishListsUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.WishList
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WishListsViewModelTest {

    private lateinit var viewModel: WishListsViewModel
    private lateinit var getWishListsUseCase: GetWishListsUseCase
    private lateinit var createListUseCase: CreateNewListUseCase
    private lateinit var getsSessionType: GetsSessionType
    private lateinit var manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase
    private lateinit var testDispatcherProvider: TestDispatcherProvider
    private var testScope: TestScope = TestScope()

    @BeforeEach
    fun setUp() {
        getWishListsUseCase = mockk(relaxed = true)
        createListUseCase = mockk(relaxed = true)
        getsSessionType = mockk(relaxed = true)
        manageLocaleLanguageUseCase = mockk(relaxed = true)
        testDispatcherProvider = TestDispatcherProvider()
        testScope = TestScope(testDispatcherProvider.testDispatcher)

        Dispatchers.setMain(testDispatcherProvider.testDispatcher)

        // Mock language flow
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(
            ManageLocaleLanguageUseCase.Language.ENGLISH
        )

        viewModel = WishListsViewModel(
            getWishListsUseCase = getWishListsUseCase,
            createListUseCase = createListUseCase,
            getsSessionType = getsSessionType,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should initialize as logged in user when session type is not guest`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.LOGGED_IN
        coEvery { getWishListsUseCase() } returns emptyList()

       
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.isUserLoggedIn).isTrue()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `should initialize as guest user when session type is guest`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.GUEST

        viewModel = WishListsViewModel(
           getWishListsUseCase=  getWishListsUseCase,
        createListUseCase=createListUseCase,
        getsSessionType= getsSessionType,
        manageLocaleLanguageUseCase=manageLocaleLanguageUseCase,
        dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isUserLoggedIn).isFalse()

    }

    @Test
    fun `should load custom lists successfully`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.LOGGED_IN
        val mockLists = listOf(
            WishList(id = 1, name = "Favorites", description = "", itemCount = 5),
            WishList(id = 2, name = "Watchlist", description = "", itemCount = 10)
        )
        coEvery { getWishListsUseCase() } returns mockLists

       
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.userLists).hasSize(2)
        assertThat(state.userLists[0].name).isEqualTo("Favorites")
        assertThat(state.userLists[1].name).isEqualTo("Watchlist")
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorUiState).isNull()
    }

    @Test
    fun `should handle empty custom lists`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.LOGGED_IN
        coEvery { getWishListsUseCase() } returns emptyList()

       
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.userLists).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorUiState).isNull()
    }

    @Test
    fun `should handle network error when loading custom lists`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.LOGGED_IN
        coEvery { getWishListsUseCase() } throws NetworkException()

       
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorUiState).isNotNull()
        assertThat(state.errorUiState).isInstanceOf(ListsUiState.ListsErrorState.NoNetworkConnection::class.java)
    }

    @Test
    fun `should handle unknown error when loading custom lists`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.LOGGED_IN
        coEvery { getWishListsUseCase() } throws AflamiException()

       
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorUiState).isNotNull()
        assertThat(state.errorUiState).isInstanceOf(ListsUiState.ListsErrorState.UnknownError::class.java)
    }

    @Test
    fun `should show create list dialog for logged in user`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.LOGGED_IN

       
        viewModel.onClickAddList()
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.isCreateNewListDialogVisible).isTrue()
        assertThat(state.isUserLoggedIn).isTrue()
    }

    @Test
    fun `should not show create list dialog for guest user`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.GUEST

       
        viewModel.onClickAddList()
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.isUserLoggedIn).isFalse()
        assertThat(state.isCreateNewListDialogVisible).isFalse()
    }

    @Test
    fun `should update list name when onListNameChange is called`() = testScope.runTest {
       
        val listName = "My New List"

       
        viewModel.onListNameChange(listName)
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.listName).isEqualTo(listName)
    }

    @Test
    fun `should create new list successfully`() = testScope.runTest {
       
        val listName = "My New List"
        coEvery { createListUseCase(listName) } returns 1

       
        viewModel.onListNameChange(listName)
        viewModel.onCreateNewListClick()
        advanceUntilIdle()

       
        coVerify { createListUseCase(listName) }
        val state = viewModel.state.value
        assertThat(state.isCreateNewListDialogVisible).isFalse()
        assertThat(state.listName).isEmpty()
        assertThat(state.isCreateListLoading).isFalse()
    }

    @Test
    fun `should handle list creation failure`() = testScope.runTest {
       
        val listName = "My New List"
        coEvery { createListUseCase(listName) } throws AflamiException()

       
        viewModel.onListNameChange(listName)
        viewModel.onCreateNewListClick()
        advanceUntilIdle()

       
        coVerify { createListUseCase(listName) }
        val state = viewModel.state.value
        assertThat(state.isCreateNewListDialogVisible).isFalse()
        assertThat(state.listName).isEmpty()
        assertThat(state.isCreateListLoading).isFalse()
    }

    @Test
    fun `should navigate to list details when onListClick is called`() = testScope.runTest {
       
        val listId = 1L
        val listName = "Favorites"
        var capturedEffect: ListsEffect? = null
        val job = launch {
            viewModel.effect.collect { effect ->
                capturedEffect = effect
            }
        }

       
        viewModel.onListClick(listId, listName)
        advanceUntilIdle()

       
        assertThat(capturedEffect).isInstanceOf(ListsEffect.NavigateToListDetails::class.java)
        val navigateEffect = capturedEffect as ListsEffect.NavigateToListDetails
        assertThat(navigateEffect.listId).isEqualTo(listId)
        assertThat(navigateEffect.listName).isEqualTo(listName)
        job.cancel()
    }

    @Test
    fun `should retry loading lists when onClickRetryFetchList is called`() = testScope.runTest {
       
        coEvery { getsSessionType() } returns SessionType.LOGGED_IN
        coEvery { getWishListsUseCase() } returns emptyList()

       
        advanceUntilIdle() // Wait for initial calls to complete
        viewModel.onClickRetryFetchList()
        advanceUntilIdle()

       
        coVerify(exactly = 3) { getWishListsUseCase() } // Once in init, once from language flow, once in retry
    }

    @Test
    fun `should dismiss create list dialog when onDismiss is called`() = testScope.runTest {
       
        viewModel.onClickAddList() // First show the dialog
        advanceUntilIdle()

       
        viewModel.onDismiss()
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.isCreateNewListDialogVisible).isFalse()
    }

    @Test
    fun `should navigate to login when onNavigateToLoginClicked is called`() = testScope.runTest {
       
        var capturedEffect: ListsEffect? = null
        val job = launch {
            viewModel.effect.collect { effect ->
                capturedEffect = effect
            }
        }

       
        viewModel.onNavigateToLoginClicked()
        advanceUntilIdle()

       
        assertThat(capturedEffect).isEqualTo(ListsEffect.NavigateToLogin)
        job.cancel()
    }

    @Test
    fun `should handle multiple list creation attempts`() = testScope.runTest {
       
        val listName = "Test List"
        coEvery { createListUseCase(listName) } returns 1

       
        viewModel.onListNameChange(listName)
        viewModel.onCreateNewListClick()
        viewModel.onCreateNewListClick() // Second attempt
        advanceUntilIdle()

       
        coVerify(exactly = 2) { createListUseCase(listName) }
    }

    @Test
    fun `should handle very long list name`() = testScope.runTest {
       
        val longListName = "A".repeat(1000)
        coEvery { createListUseCase(longListName) } returns 1

       
        viewModel.onListNameChange(longListName)
        viewModel.onCreateNewListClick()
        advanceUntilIdle()

       
        coVerify { createListUseCase(longListName) }
        val state = viewModel.state.value
        assertThat(state.isCreateNewListDialogVisible).isFalse()
        assertThat(state.listName).isEmpty()
    }

    @Test
    fun `should handle empty list name creation`() = testScope.runTest {
       
        val emptyListName = ""
        coEvery { createListUseCase(emptyListName) } returns 1

       
        viewModel.onListNameChange(emptyListName)
        viewModel.onCreateNewListClick()
        advanceUntilIdle()

       
        coVerify { createListUseCase(emptyListName) }
        val state = viewModel.state.value
        assertThat(state.isCreateNewListDialogVisible).isFalse()
        assertThat(state.listName).isEmpty()
    }

    @Test
    fun `should handle special characters in list name`() = testScope.runTest {
       
        val specialListName = "List with @#$%^&*() characters"
        coEvery { createListUseCase(specialListName) } returns 1

       
        viewModel.onListNameChange(specialListName)
        viewModel.onCreateNewListClick()
        advanceUntilIdle()

       
        coVerify { createListUseCase(specialListName) }
        val state = viewModel.state.value
        assertThat(state.isCreateNewListDialogVisible).isFalse()
        assertThat(state.listName).isEmpty()
    }

    @Test
    fun `should handle rapid list name changes`() = testScope.runTest {
       
        val names = listOf("List1", "List2", "List3", "List4", "List5")

       
        names.forEach { name ->
            viewModel.onListNameChange(name)
        }
        advanceUntilIdle()

       
        val state = viewModel.state.value
        assertThat(state.listName).isEqualTo("List5")
    }
}