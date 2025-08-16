package com.amsterdam.repository.mapper

import com.amsterdam.entity.Character
import com.amsterdam.repository.dto.remote.RemoteCharacterItemDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CharacterMapperTest {
    @Test
    fun `toEntity should map RemoteCharacterItemDto to Character entity`() {
        val result = remotePersonDto.toEntity()

        assertThat(result).isEqualTo(expectedPersonEntity)
    }

    @Test
    fun `toEntity should map imageUrl to empty string when profilePath is null`() {
        val result = remotePersonDtoWithNullImage.toEntity()

        assertThat(result).isEqualTo(expectedPersonWithNullImage)
    }

    @Test
    fun `toEntityList should map a list of Dtos to a list of Character entities`() {
        val dtoList = listOf(remotePersonDto, remotePersonDtoWithNullImage)
        val result = dtoList.toEntityList()

        assertThat(result).isEqualTo(expectedCharacterList)
    }

    @Test
    fun `toEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<RemoteCharacterItemDto>()
        val result = emptyDtoList.toEntityList()

        assertThat(result).isEmpty()
    }

    private val remotePersonDto = RemoteCharacterItemDto(
        id = 1,
        name = "Tom Holland",
        profilePath = "/tom_holland.jpg",
        adult = false,
        gender = 2,
        popularity = 150.0,
        mediaType = "person",
        originalName = "Tom Holland",
        knownForDepartment = "Acting"
    )

    private val remotePersonDtoWithNullImage = remotePersonDto.copy(
        id = 2,
        name = "Zendaya",
        profilePath = null
    )

    private val expectedPersonEntity = Character(
        id = 1L,
        name = "Tom Holland",
        imageUrl = "https://image.tmdb.org/t/p/w500/tom_holland.jpg"
    )

    private val expectedPersonWithNullImage = Character(
        id = 2L,
        name = "Zendaya",
        imageUrl = ""
    )

    private val expectedCharacterList = listOf(
        expectedPersonEntity,
        expectedPersonWithNullImage
    )
}