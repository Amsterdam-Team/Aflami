package com.example.localdatasource.roomDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.localdatasource.roomDataBase.converter.InstantConverter
import com.example.localdatasource.roomDataBase.converter.SearchTypeConverter
import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto
import com.example.repository.dto.local.MovieCategoryCrossRefDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.TvShowCategoryCrossRefDto

@Database(
    entities = [LocalSearchDto::class,
        LocalCountryDto::class,
        LocalMovieCategoryDto::class,
        LocalTvShowCategoryDto::class,
        LocalMovieDto::class,
        LocalTvShowDto::class,
        LocalTvShowWithSearchDto::class,
        MovieCategoryCrossRefDto::class,
        TvShowCategoryCrossRefDto::class,
        SearchMovieCrossRefDto::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(InstantConverter::class, SearchTypeConverter::class)
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
                .fallbackToDestructiveMigration(false)
                .build()
        }
    }
}