package com.amsterdam.repository.mapper

import com.amsterdam.entity.WishList
import com.amsterdam.repository.dto.remote.WishListRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class WishListMapperTest {
    @Test
    fun `toEntity should map WishListRemoteDto to UserList entity`() {
        val result = wishListRemoteDto.toEntity()

        assertThat(result).isEqualTo(expectedEntity)
    }

    private val wishListRemoteDto = WishListRemoteDto(
        id = 1,
        name = "My Favorite Movies",
        description = "A collection of the best movies ever.",
        itemCount = 25
    )

    private val expectedEntity = WishList(
        id = 1,
        name = "My Favorite Movies",
        description = "A collection of the best movies ever.",
        itemCount = 25
    )
}