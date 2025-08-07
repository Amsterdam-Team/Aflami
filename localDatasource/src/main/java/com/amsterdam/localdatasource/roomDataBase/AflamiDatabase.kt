package com.amsterdam.localdatasource.roomDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amsterdam.localdatasource.roomDataBase.converter.InstantConverter
import com.amsterdam.localdatasource.roomDataBase.converter.LocalDateConverter
import com.amsterdam.localdatasource.roomDataBase.converter.SearchTypeConverter
import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.roomDataBase.daos.AccountDetailsDao
import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalMovieCategoryInterestDto
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryInterestDto
import com.amsterdam.repository.dto.local.LocalTvShowDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.MovieWatchHistoryDto
import com.amsterdam.repository.dto.local.PopularMovieDto
import com.amsterdam.repository.dto.local.PopularTvShowDto
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.dto.local.UpcomingMovieDto
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto

@Database(
    entities = [LocalSearchDto::class,
        LocalCountryDto::class,
        LocalMovieDto::class,
        LocalTvShowDto::class,
        MovieWatchHistoryDto::class,
        TvShowWatchHistoryDto::class,
        LocalMovieCategoryInterestDto::class,
        LocalTvShowCategoryInterestDto::class,
        LocalMovieCategoryDto::class,
        LocalTvShowCategoryDto::class,
        MovieCategoryCrossRefDto::class,
        TvShowCategoryCrossRefDto::class,
        PopularMovieDto::class,
        PopularTvShowDto::class,
        TopRatedMovieDto::class,
        TopRatedTvShowDto::class,
        UpcomingMovieDto::class,
        AccountDetailsLocalDto::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(InstantConverter::class, SearchTypeConverter::class, LocalDateConverter::class)
abstract class AflamiDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun countryDao(): CountryDao
    abstract fun categoryDao(): CategoryDao
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun watchHistoryDao(): WatchHistoryDao
    abstract fun movieCategoryInterestDao(): MovieCategoryInterestDao
    abstract fun tvShowCategoryInterestDao(): TvShowCategoryInterestDao
    abstract fun accountDetailsDao(): AccountDetailsDao

    companion object {
        private const val DATABASE_NAME = "AflamiDatabase.db"

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