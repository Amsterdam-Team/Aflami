package com.amsterdam.repository.mapper

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.repository.dto.remote.CastRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CastMapperTest {
    @Test
    fun `toEntity should map Dto to Actor entity for Male actor`() {
        val result = maleActorDto.toEntity()

        assertThat(result).isEqualTo(expectedMaleActor)
    }

    @Test
    fun `toEntity should map Dto to Actor entity for Female actor`() {
        val result = femaleActorDto.toEntity()

        assertThat(result).isEqualTo(expectedFemaleActor)
    }

    @Test
    fun `toEntity should map imageUrl to a string containing null when profilePath is null`() {
        val result = actorDtoWithNullImage.toEntity()

        assertThat(result).isEqualTo(expectedActorWithNullImage)
    }

    @Test
    fun `toEntityList should map list of Dtos to list of Actor entities`() {
        val dtoList = listOf(maleActorDto, actorDtoWithNullImage)
        val result = dtoList.toEntityList()

        assertThat(result).isEqualTo(expectedActorList)
    }

    @Test
    fun `toEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<CastRemoteDto>()
        val result = emptyDtoList.toEntityList()

        assertThat(result).isEmpty()
    }

    private val maleActorDto = CastRemoteDto(
        id = 1L,
        name = "Keanu Reeves",
        gender = 2,
        popularity = 90.0,
        profilePath = "/keanu_reeves.jpg",
        adult = false,
        knownForDepartment = "Acting",
        originalName = "Keanu Reeves",
        character = "Neo",
        creditId = "credit1",
        order = 0
    )

    private val femaleActorDto = CastRemoteDto(
        id = 2L,
        name = "Carrie-Anne Moss",
        gender = 1,
        popularity = 85.5,
        profilePath = "/carrie_anne_moss.jpg",
        adult = false,
        knownForDepartment = "Acting",
        originalName = "Carrie-Anne Moss",
        character = "Trinity",
        creditId = "credit2",
        order = 1
    )

    private val actorDtoWithNullImage = CastRemoteDto(
        id = 3L,
        name = "Laurence Fishburne",
        gender = 2,
        popularity = 82.1,
        profilePath = null,
        adult = false,
        knownForDepartment = "Acting",
        originalName = "Laurence Fishburne",
        character = "Morpheus",
        creditId = "credit3",
        order = 2
    )

    private val expectedMaleActor = Actor(
        id = 1L,
        name = "Keanu Reeves",
        imageUrl = "https://image.tmdb.org/t/p/w500/keanu_reeves.jpg",
        popularity = 90.0,
        gender = Gender.Male
    )

    private val expectedFemaleActor = Actor(
        id = 2L,
        name = "Carrie-Anne Moss",
        imageUrl = "https://image.tmdb.org/t/p/w500/carrie_anne_moss.jpg",
        popularity = 85.5,
        gender = Gender.Female
    )

    private val expectedActorWithNullImage = Actor(
        id = 3L,
        name = "Laurence Fishburne",
        imageUrl = "https://image.tmdb.org/t/p/w500null",
        popularity = 82.1,
        gender = Gender.Male
    )

    private val expectedActorList = listOf(
        expectedMaleActor,
        expectedActorWithNullImage
    )
}