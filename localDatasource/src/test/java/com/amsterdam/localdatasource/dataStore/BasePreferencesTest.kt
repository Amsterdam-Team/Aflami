package com.amsterdam.localdatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import org.junit.jupiter.api.BeforeEach
import java.io.File

open class BasePreferencesTest {
    private val file: File by lazy {
        File.createTempFile("test", ".preferences_pb").apply { deleteOnExit() }
    }
    protected val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.create { file }
    }

    @BeforeEach
    fun setup() {
        file.writeText("")
    }


}