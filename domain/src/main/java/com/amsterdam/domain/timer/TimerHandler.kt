package com.amsterdam.domain.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class TimerHandler {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private var timerJob: Job? = null
    private var endTimeMillis: Long = 0L
    private var isTimerStopped = false

    fun startTimer(
        totalSeconds: Int,
        onTimerFinish: () -> Unit
    ): StateFlow<Int> {
        timerJob?.cancel()
        isTimerStopped = false
        endTimeMillis = System.currentTimeMillis() + totalSeconds * 1000L

        val remainingTimeFlow = MutableStateFlow(totalSeconds)

        timerJob = scope.launch {
            while (true) {
                val remainingMillis = endTimeMillis - System.currentTimeMillis()
                val remainingSeconds = max(0, (remainingMillis / 1000).toInt())

                remainingTimeFlow.value = remainingSeconds

                if (remainingSeconds <= 0 || isTimerStopped) {
                    onTimerFinish()
                    break
                }
                delay(1000L)
            }
        }

        return remainingTimeFlow
    }

    fun stopTimer() {
        isTimerStopped = true
        timerJob?.cancel()
    }

}
