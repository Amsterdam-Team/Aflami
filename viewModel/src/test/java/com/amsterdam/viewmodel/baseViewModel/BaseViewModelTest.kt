package com.amsterdam.viewmodel.baseViewModel

import app.cash.turbine.test
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private val testDispatcherProvider = TestDispatcherProvider()
    private var testScope = TestScope(
        testDispatcherProvider.testDispatcher
    )
    private val testViewModel = TestViewModel(testDispatcherProvider)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        testScope = TestScope(testDispatcherProvider.testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be zero`() = runTest {
        assertThat(testViewModel.state.value, equalTo(0))
    }

    @Test
    fun `increment should update the state once`() = runTest {
        var emissionCount = 0
        val job = launch {
            testViewModel.state.collect {
                emissionCount++
            }
        }
        testViewModel.increment(1)
        advanceUntilIdle()
        job.cancel()
        assertThat(emissionCount, equalTo(1))
    }

    @Test
    fun `sendNewEffect should emit correct effect`() = runTest {
        val expectedEffect = "TestEffect"
        val job = launch {
            testViewModel.effect.collect {
                assertThat(it, equalTo(expectedEffect))
            }
        }
        testViewModel.sendTestEffect(expectedEffect)
        advanceUntilIdle()
        job.cancel()
    }

    @Test
    fun `tryToExecute should emit error effect when exception thrown`() = runTest {
        val job = launch {
            testViewModel.effect.test {
                testViewModel.executeWithError()
                assertThat(awaitItem(), equalTo("Error"))
                cancelAndIgnoreRemainingEvents()
            }
        }
        advanceUntilIdle()
        job.cancel()
    }

    @Test
    fun `tryToExecute should emit effect when exception not thrown`() = runTest {
        val job = launch {
            testViewModel.effect.test {
                testViewModel.executeWithSuccess()
                assertThat(awaitItem(), equalTo("Success"))
                cancelAndIgnoreRemainingEvents()
            }
        }
        advanceUntilIdle()
        job.cancel()
    }
}
