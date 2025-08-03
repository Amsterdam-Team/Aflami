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
import com.amsterdam.localdatasource.roomDataBase.daos.PopularMovieDao
import com.amsterdam.localdatasource.roomDataBase.daos.PopularTvShowsDao
import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.localdatasource.roomDataBase.daos.TopRatedMovieDao
import com.amsterdam.localdatasource.roomDataBase.daos.TopRatedTvShowDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.localdatasource.roomDataBase.daos.UpcomingMovieDao
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
import com.amsterdam.repository.dto.local.SearchMovieCrossRefDto
import com.amsterdam.repository.dto.local.SearchTvShowCrossRefDto
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import com.amsterdam.repository.dto.local.TopRatedTvShowDto
import com.amsterdam.repository.dto.local.TvShowCategoryCrossRefDto
import com.amsterdam.repository.dto.local.TvShowWatchHistoryDto
import com.amsterdam.repository.dto.local.UpcomingMovieDto

@Database(
    entities = [LocalSearchDto::class,
        LocalCountryDto::class,
        LocalMovieCategoryDto::class,
        LocalTvShowCategoryDto::class,
        LocalMovieDto::class,
        LocalTvShowDto::class,
        MovieWatchHistoryDto::class,
        TvShowWatchHistoryDto::class,
        SearchTvShowCrossRefDto::class,
        MovieCategoryCrossRefDto::class,
        TvShowCategoryCrossRefDto::class,
        SearchMovieCrossRefDto::class,
        LocalMovieCategoryInterestDto::class,
        LocalTvShowCategoryInterestDto::class,
        PopularMovieDto::class,
        PopularTvShowDto::class,
        TopRatedMovieDto::class,
        TopRatedTvShowDto::class,
        UpcomingMovieDto::class
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
    abstract fun popularMovieDao(): PopularMovieDao
    abstract fun upcomingMovieDao(): UpcomingMovieDao
    abstract fun popularTvShowsDao(): PopularTvShowsDao
    abstract fun topRatedMovieDao(): TopRatedMovieDao
    abstract fun topRatedTvShowDao(): TopRatedTvShowDao

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