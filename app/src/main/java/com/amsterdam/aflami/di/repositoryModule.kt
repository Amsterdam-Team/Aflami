package com.amsterdam.aflami.di

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.CountryRepository
import com.example.domain.repository.MovieRepository
import com.example.domain.repository.RecentSearchRepository
import com.example.domain.repository.TvShowRepository
import com.example.repository.mapper.DateParser
import com.example.repository.mapper.DateParserImpl
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.local.MovieCategoryLocalMapper
import com.example.repository.mapper.local.MovieGenreLocalMapper
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.MovieWithCategoriesLocalMapper
import com.example.repository.mapper.local.RecentSearchLocalMapper
import com.example.repository.mapper.local.TvShowCategoryLocalMapper
import com.example.repository.mapper.local.TvShowGenreLocalMapper
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.local.TvShowWithCategoryLocalMapper
import com.example.repository.mapper.remote.CastRemoteMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.example.repository.mapper.remote.CountryRemoteMapper
import com.example.repository.mapper.remote.GalleryRemoteMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.mapper.remote.ProductionCompanyRemoteMapper
import com.example.repository.mapper.remote.ReviewRemoteMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.mapper.remoteToLocal.CountryRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.MovieCategoryRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.TvShowCategoryRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.TvShowRemoteLocalMapper
import com.example.repository.repository.CategoryRepositoryImpl
import com.example.repository.repository.CountryRepositoryImpl
import com.example.repository.repository.MovieRepositoryImpl
import com.example.repository.repository.RecentSearchRepositoryImpl
import com.example.repository.repository.TvShowRepositoryImpl
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.RecentSearchHandlerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    // Mappers
    singleOf(::CountryLocalMapper)
    singleOf(::CountryRemoteMapper)
    singleOf(::MovieCategoryLocalMapper)
    singleOf(::CategoryRemoteMapper)
    singleOf(::MovieLocalMapper)
    singleOf(::TvShowLocalMapper)
    singleOf(::MovieRemoteMapper)
    singleOf(::TvShowRemoteMapper)
    singleOf(::RecentSearchLocalMapper)
    singleOf(::CastRemoteMapper)
    singleOf(::ReviewRemoteMapper)
    singleOf(::GalleryRemoteMapper)
    singleOf(::ProductionCompanyRemoteMapper)
    singleOf(::CountryRemoteLocalMapper)
    singleOf(::MovieCategoryRemoteLocalMapper)
    singleOf(::MovieRemoteLocalMapper)
    singleOf(::TvShowCategoryRemoteLocalMapper)
    singleOf(::TvShowRemoteLocalMapper)
    singleOf(::MovieCategoryLocalMapper)
    singleOf(::MovieGenreLocalMapper)
    singleOf(::MovieWithCategoriesLocalMapper)
    singleOf(::TvShowCategoryLocalMapper)
    singleOf(::TvShowGenreLocalMapper)
    singleOf(::TvShowWithCategoryLocalMapper)

    singleOf<DateParser>(::DateParserImpl)

    // Handler
    singleOf(::RecentSearchHandlerImpl) bind RecentSearchHandler::class

    // Repositories
    singleOf(::CountryRepositoryImpl) bind CountryRepository::class
    singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    singleOf(::MovieRepositoryImpl) bind MovieRepository::class
    singleOf(::RecentSearchRepositoryImpl) bind RecentSearchRepository::class
    singleOf(::TvShowRepositoryImpl) bind TvShowRepository::class
}