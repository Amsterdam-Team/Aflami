package com.amsterdam.viewmodel.utils

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class DebounceSearchTest {
    @Test
    fun `emits only the last item after a rapid sequence of emissions`() = runTest {
        // Given
        val collectedItems = mutableListOf<String>()
        val onCollect: suspend (String) -> Unit = { collectedItems.add(it) }
        val sourceFlow = flow {
            emit("a")
            advanceTimeBy(50)
            emit("ab")
            advanceTimeBy(50)
            emit("abc")
            advanceTimeBy(301)
            emit("d")
        }

        // When
        sourceFlow.debounceSearch(onCollect)

        // Then
        assertThat(collectedItems).isEqualTo(listOf("abc", "d"))
    }
}