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
import com.amsterdam.localdatasource.roomDataBase.daos.GamePointsDao
import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.localdatasource.roomDataBase.daos.WatchHistoryDao
import com.amsterdam.repository.dto.local.GamePointsDto
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.MovieCategoryInterestLocalDto
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.SearchLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryInterestLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
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
    entities = [SearchLocalDto::class,
        CountryLocalDto::class,
        MovieLocalDto::class,
        TvShowLocalDto::class,
        MovieWatchHistoryDto::class,
        TvShowWatchHistoryDto::class,
        MovieCategoryInterestLocalDto::class,
        TvShowCategoryInterestLocalDto::class,
        MovieCategoryLocalDto::class,
        TvShowCategoryLocalDto::class,
        MovieCategoryCrossRefDto::class,
        TvShowCategoryCrossRefDto::class,
        PopularMovieDto::class,
        PopularTvShowDto::class,
        TopRatedMovieDto::class,
        TopRatedTvShowDto::class,
        UpcomingMovieDto::class,
        AccountDetailsLocalDto::class,
        GamePointsDto::class
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
    abstract fun gamePointsDao(): GamePointsDao

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