package com.amsterdam.repository.mapper

import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class AccountDetailsMapperTest {
    @Test
    fun `toEntity should map AccountDetailsLocalDto to AccountDetails entity`() {
        val resultEntity = localDto.toEntity()

        assertThat(resultEntity).isEqualTo(expectedEntityFromLocal)
    }

    @Test
    fun `toEntity should map AccountDetailsRemoteDto to AccountDetails entity`() {
        val resultEntity = remoteDto.toEntity()

        assertThat(resultEntity).isEqualTo(expectedEntityFromRemote)
    }

    @Test
    fun `toEntity should map avatarUrl to empty string if remote avatar path is null`() {
        val resultEntity = remoteDtoWithNullAvatar.toEntity()

        assertThat(resultEntity).isEqualTo(expectedEntityWithEmptyAvatar)
    }

    @Test
    fun `toLocalDto should map AccountDetailsRemoteDto to AccountDetailsLocalDto`() {
        val resultLocalDto = remoteDto.toLocalDto()

        assertThat(resultLocalDto).isEqualTo(expectedLocalDtoFromRemote)
    }

    @Test
    fun `toLocalDto should map avatarUrl to empty string if remote avatar path is null`() {
        val resultLocalDto = remoteDtoWithNullAvatar.toLocalDto()

        assertThat(resultLocalDto).isEqualTo(expectedLocalDtoWithEmptyAvatar)
    }

    private val localDto = AccountDetailsLocalDto(
        accountId = 1,
        username = "localUser",
        avatarUrl = "/local_avatar.jpg"
    )

    private val remoteDto = AccountDetailsRemoteDto(
        id = 123,
        username = "remoteUser",
        name = "Remote User Name",
        includeAdult = false,
        countryIsoCode = "US",
        languageCode = "en",
        accountAvatar = AccountDetailsRemoteDto.AccountAvatar(
            gravatar = AccountDetailsRemoteDto.Gravatar(hash = "some_hash_value"),
            movieDBData = AccountDetailsRemoteDto.MovieDBData(avatarPath = "/remote_avatar.png")
        )
    )

    private val remoteDtoWithNullAvatar = AccountDetailsRemoteDto(
        id = 456,
        username = "noAvatarUser",
        name = "No Avatar User Name",
        includeAdult = true,
        countryIsoCode = "EG",
        languageCode = "ar",
        accountAvatar = AccountDetailsRemoteDto.AccountAvatar(
            gravatar = AccountDetailsRemoteDto.Gravatar(hash = "another_hash"),
            movieDBData = AccountDetailsRemoteDto.MovieDBData(avatarPath = null)
        )
    )

    private val expectedEntityFromLocal = AccountDetails(
        accountId = 1,
        username = "localUser",
        avatarUrl = "/local_avatar.jpg"
    )

    private val expectedEntityFromRemote = AccountDetails(
        accountId = 123,
        username = "remoteUser",
        avatarUrl = "https://image.tmdb.org/t/p/w500/remote_avatar.png"
    )

    private val expectedEntityWithEmptyAvatar = AccountDetails(
        accountId = 456,
        username = "noAvatarUser",
        avatarUrl = ""
    )

    private val expectedLocalDtoFromRemote = AccountDetailsLocalDto(
        accountId = 123,
        username = "remoteUser",
        avatarUrl = "https://image.tmdb.org/t/p/w500/remote_avatar.png"
    )

    private val expectedLocalDtoWithEmptyAvatar = AccountDetailsLocalDto(
        accountId = 456,
        username = "noAvatarUser",
        avatarUrl = ""
    )
}