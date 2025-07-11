package com.example.localdatasource.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.localdatasource.roomDatabase.daos.CategoryDao
import com.example.localdatasource.roomDatabase.daos.CountryDao
import com.example.localdatasource.roomDatabase.daos.MovieDao
import com.example.localdatasource.roomDatabase.daos.RecentSearchDao
import com.example.localdatasource.roomDatabase.daos.TvShowDao

@Database(entities = [], version = 1, exportSchema = true)
abstract class AflamiDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun countryDao(): CountryDao
    abstract fun categoryDao(): CategoryDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao

    companion object {
        private const val DATABASE_NAME = "AflamiDatabase"

        @Volatile
        private var instance: AflamiDatabase? = null

        fun getInstance(context: Context): AflamiDatabase {
            return instance ?: synchronized(this) {
                buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): AflamiDatabase {
            return Room.databaseBuilder(context, AflamiDatabase::class.java, DATABASE_NAME)
                .createFromAsset("database/$DATABASE_NAME.db")
                .build()
        }
    }
}
