package com.amsterdam.domain.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

class TimerHandler(
    private val scope: CoroutineScope
) {
    private var timerJob: Job? = null
    private var endTimeMillis: Long = 0L
    private var isTimerStopped = false

    fun startTimer(
        questionTimeLimitSeconds: Int,
        onTimerUpdate: (remainingSeconds: Int) -> Unit,
        onTimerFinish: () -> Unit
    ) {
        timerJob?.cancel()
        isTimerStopped = false
        endTimeMillis = System.currentTimeMillis() + questionTimeLimitSeconds * 1000L

        timerJob = scope.launch {
            while (true) {
                val remainingMillis = endTimeMillis - System.currentTimeMillis()
                val remainingSeconds = max(0, (remainingMillis / 1000).toInt())

                onTimerUpdate(remainingSeconds)

                if (remainingSeconds <= 0 || isTimerStopped) {
                    onTimerFinish()
                    break
                }
                delay(1000L)
            }
        }
    }

    fun stopTimer() {
        isTimerStopped = true
        timerJob?.cancel()
    }

}