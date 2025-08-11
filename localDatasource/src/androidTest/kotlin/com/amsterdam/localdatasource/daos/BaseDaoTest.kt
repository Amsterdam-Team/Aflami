package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import org.junit.jupiter.api.AfterEach

open class BaseDaoTest {
    private val appContext by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    protected val aflamiDatabase by lazy {
        Room.inMemoryDatabaseBuilder(appContext, AflamiDatabase::class.java).build()
    }

    @AfterEach
    fun tearDown() {
        aflamiDatabase.close()
    }
}