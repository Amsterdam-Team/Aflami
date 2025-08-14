package com.amsterdam.domain.timer

import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class TimerHandlerTest {

    @Test
    fun `startTimer should return correct start time when called`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val startTime = 5

        val flow = handler.startTimer(totalSeconds = startTime) {}

        assertThat(flow.value).isEqualTo(startTime)
    }

    @Test
    fun `startTime should decrease remaining time go down by one when one real second passes`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        val flow = handler.startTimer(totalSeconds = startTime) {}

        advanceTimeBy(1000)
        runCurrent()

        assertThat(flow.value).isEqualTo(4)
    }

    @Test
    fun `startTimer counts down to zero when all time has passed`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        val flow = handler.startTimer(totalSeconds = startTime) {}

        advanceTimeBy(5000)
        runCurrent()

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `startTimer should not call onTimerFinish when time reaches 0`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var finishCalls = 0
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        handler.startTimer(totalSeconds = startTime) { finishCalls++ }

        advanceTimeBy(5000)
        runCurrent()

        assertThat(finishCalls).isEqualTo(1)
    }

    @Test
    fun `startTimer should not call onTimerFinish when time has not ended`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var finishCalls = 0
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        handler.startTimer(totalSeconds = startTime) { finishCalls++ }

        advanceTimeBy(3000)
        runCurrent()

        assertThat(finishCalls).isEqualTo(0)
    }

    @Test
    fun `startTimer should not keep advancing old timer when new timer is created`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val flow = handler.startTimer(totalSeconds = 10) {}

        advanceTimeBy(3000)
        runCurrent()

        handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(3000)
        runCurrent()

        assertThat(flow.value).isEqualTo(7)
    }

    @Test
    fun `startTimer should keep advance new timer when new timer is created`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        handler.startTimer(totalSeconds = 10) {}

        advanceTimeBy(3000)
        runCurrent()

        val flow = handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(3000)
        runCurrent()

        assertThat(flow.value).isEqualTo(2)
    }

    @Test
    fun `startTimer should not call finish when another timer gets started`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var finishCalls = 0
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        handler.startTimer(totalSeconds = 10) { finishCalls++ }

        advanceTimeBy(3000)
        runCurrent()

        handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(3000)
        runCurrent()

        assertThat(finishCalls).isEqualTo(0)
    }

    @Test
    fun `startTime should set start time as 0 when given zero`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val flow = handler.startTimer(totalSeconds = 0) {}

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `startTime should set start time as 0 when given negative start`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val flow = handler.startTimer(totalSeconds = -5) {}

        advanceTimeBy(1000)
        runCurrent()

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `stopTimer should cancel timer early when called`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val flow = handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(2000)
        runCurrent()

        handler.stopTimer()
        advanceTimeBy(3000)
        runCurrent()

        assertThat(flow.value).isEqualTo(3)
    }

    @Test
    fun `startTimer should works when called with default nowMillis and dispatcher`() = runTest {
        val handler = TimerHandler()
        val flow = handler.startTimer(totalSeconds = 1) {}
        advanceUntilIdle()
        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `startTimer should count down to 0 when system time jumps to large number`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val flow = handler.startTimer(totalSeconds = 10) {}

        advanceTimeBy(15_000)
        runCurrent()

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `stopTimer should do nothing when no timer was started`() = runTest{
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val mockJob = mockk<Job>()

        handler.stopTimer()

        coVerify(exactly = 0) { mockJob.cancel(any()) }
    }
}
