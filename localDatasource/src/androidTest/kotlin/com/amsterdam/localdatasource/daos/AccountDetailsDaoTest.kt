package com.amsterdam.localdatasource.daos

import androidx.room.Dao
import com.amsterdam.localdatasource.roomDataBase.daos.AccountDetailsDao
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Dao
class AccountDetailsDaoTest : BaseDaoTest() {
    private lateinit var accountDetailsDao: AccountDetailsDao

    @BeforeEach
    fun setup() {
        accountDetailsDao = aflamiDatabase.accountDetailsDao()
    }

    @Test
    fun upsertAccountDetails_shouldInsertAccountDetails_whenAccountDetailsNotExist() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(accountDetails)
    }

    @Test
    fun upsertAccountDetails_shouldUpdateAccountDetails_whenAccountDetailsExist() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)

        accountDetailsDao.upsertAccountDetails(updatedAccountDetails)
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(updatedAccountDetails)
    }

    @Test
    fun getAccountDetails_shouldReturnAccountDetails_whenAccountDetailsExist() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)

        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(accountDetails)
    }

    @Test
    fun getAccountDetails_shouldReturnNull_whenAccountDetailsNotExist() = runTest {
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isNull()
    }

    @Test
    fun deleteAccountDetails_shouldDeleteAccountDetails() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)

        accountDetailsDao.deleteAccountDetails()
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isNull()
    }

}

private val accountDetails = AccountDetailsLocalDto(accountId = 1)
private val updatedAccountDetails = accountDetails.copy(username = "new name")