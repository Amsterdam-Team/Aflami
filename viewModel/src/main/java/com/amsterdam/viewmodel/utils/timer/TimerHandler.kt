package com.amsterdam.viewmodel.utils.timer

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.math.max

class TimerHandler(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val nowMillis: () -> Long = { Clock.System.now().toEpochMilliseconds()}
) {

    private val job = Job()
    private val scope = CoroutineScope(dispatcher + job)

    private var timerJob: Job? = null
    private var endTimeMillis: Long = 0L

    fun startTimer(
        totalSeconds: Int,
        onTimerFinish: () -> Unit
    ): StateFlow<Int> {
        timerJob?.cancel()
        endTimeMillis = nowMillis() + totalSeconds * 1000L

        val remainingTimeFlow = MutableStateFlow(totalSeconds.coerceAtLeast(0))

        timerJob = scope.launch {
            while (true) {
                val remainingMillis = endTimeMillis - nowMillis()
                val remainingSeconds = max(0, (remainingMillis / 1000).toInt())

                remainingTimeFlow.value = remainingSeconds

                if (remainingSeconds <= 0) {
                    onTimerFinish()
                    break
                }
                delay(1000L)
            }
        }
        return remainingTimeFlow
    }

    fun stopTimer() {
        timerJob?.cancel()
    }
}
