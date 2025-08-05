package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.localdatasource.utils.createLocalSearchDto
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecentSearchDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var dao: RecentSearchDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        dao = database.recentSearchDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }


    @Test
    fun upsertRecentSearch_shouldInsertNewEntry() = runTest {
        // Given
        val search = createLocalSearchDto("barbie")

        // When
        dao.upsertRecentSearch(search)

        // Then
        val result = dao.getRecentSearches()
        assertThat(result.map { it.toComparable() }).contains(search.toComparable())
    }

    @Test
    fun upsertRecentSearch_shouldUpdateExistingEntry() = runTest {
        // Given
        val original = createLocalSearchDto("oppenheimer")
        val updated = createLocalSearchDto("oppenheimer")

        // When
        dao.upsertRecentSearch(original)
        dao.upsertRecentSearch(updated)

        // Then
        val result = dao.getRecentSearches()
        assertThat(result.map { it.toComparable() }).containsExactly(updated.toComparable())
    }




    @Test
    fun deleteAllSearches_shouldRemoveAll() = runTest {
        // Given
        dao.upsertRecentSearch(createLocalSearchDto("foo"))
        dao.upsertRecentSearch(createLocalSearchDto("bar"))
        //When
        dao.deleteAllSearches()
        //Then
        val result = dao.getRecentSearches()
        assertThat(result).isEmpty()
    }

    @Test
    fun deleteSearchByKeyword_shouldRemoveCorrectSearch() = runTest {
        // Given
        val deletedSearch = createLocalSearchDto("this search will be delete")
        val otherSearch = createLocalSearchDto("other search")

        dao.upsertRecentSearch(deletedSearch)
        dao.upsertRecentSearch(otherSearch)
        //When
        dao.deleteSearchByKeyword(deletedSearch.searchKeyword)
        //Then
        val result = dao.getRecentSearches()

        assertThat(result.map { it.toComparable() }).containsExactly(otherSearch.toComparable())
    }

        data class ComparableSearchDto(
        val searchKeyword: String,
        val dateAdded: Long
    )

    fun LocalSearchDto.toComparable(): ComparableSearchDto {
        return ComparableSearchDto(
            searchKeyword = this.searchKeyword,
            dateAdded = this.dateAdded.toEpochMilliseconds()
        )
    }
}
