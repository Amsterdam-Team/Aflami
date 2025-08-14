package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.AccountDetailsDao
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ProfileLocalDataSourceImplTest {
    private val accountDetailsDao by lazy { mockk<AccountDetailsDao>(relaxed = true) }
    private val profileLocalDataSource by lazy { ProfileLocalDataSourceImpl(accountDetailsDao) }

    @Test
    fun `getAccountDetails should return account Details when data returned from dao`() = runTest {
        coEvery { accountDetailsDao.getAccountDetails() } returns accountDetails

        val result = profileLocalDataSource.getAccountDetails()

        assertThat(result).isEqualTo(accountDetails)
    }

    @Test
    fun `getAccountDetails should return null when no data returned from dao`() = runTest {
        coEvery { accountDetailsDao.getAccountDetails() } returns null

        val result = profileLocalDataSource.getAccountDetails()

        assertThat(result).isNull()
    }

    @Test
    fun `getAccountDetails should call getAccountDetails in dao`() = runTest {
        profileLocalDataSource.getAccountDetails()

        coVerify(exactly = 1) { accountDetailsDao.getAccountDetails() }
    }

    @Test
    fun `upsertAccountDetails should call upsertAccountDetails in dao`() = runTest {
        profileLocalDataSource.upsertAccountDetails(accountDetails)

        coVerify(exactly = 1) { accountDetailsDao.upsertAccountDetails(accountDetails) }
    }

    @Test
    fun `deleteAccountDetails should call deleteAccountDetails in dao`() = runTest {
        profileLocalDataSource.deleteAccountDetails()

        coVerify(exactly = 1) { accountDetailsDao.deleteAccountDetails() }
    }

}

private val accountDetails = AccountDetailsLocalDto(
    username = "username",
    avatarUrl = "avatarUrl",
    accountId = 1
)