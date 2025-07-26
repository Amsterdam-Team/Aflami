package com.amsterdam.aflami.di

import com.amsterdam.repository.mapper.local.CountryLocalMapper
import com.amsterdam.repository.mapper.local.MovieCategoryLocalMapper
import com.amsterdam.repository.mapper.local.MovieGenreLocalMapper
import com.amsterdam.repository.mapper.local.MovieLocalMapper
import com.amsterdam.repository.mapper.local.MovieWatchHistoryMapper
import com.amsterdam.repository.mapper.local.MovieWithCategoriesLocalMapper
import com.amsterdam.repository.mapper.local.RecentSearchLocalMapper
import com.amsterdam.repository.mapper.local.SessionTypeMapper
import com.amsterdam.repository.mapper.local.TvShowCategoryLocalMapper
import com.amsterdam.repository.mapper.local.TvShowGenreLocalMapper
import com.amsterdam.repository.mapper.local.TvShowLocalMapper
import com.amsterdam.repository.mapper.local.TvShowWatchHistoryMapper
import com.amsterdam.repository.mapper.local.TvShowWithCategoryLocalMapper
import com.amsterdam.repository.mapper.remote.CastRemoteMapper
import com.amsterdam.repository.mapper.remote.CategoryRemoteMapper
import com.amsterdam.repository.mapper.remote.CountryRemoteMapper
import com.amsterdam.repository.mapper.remote.EpisodeRemoteMapper
import com.amsterdam.repository.mapper.remote.GalleryRemoteMapper
import com.amsterdam.repository.mapper.remote.MovieRemoteMapper
import com.amsterdam.repository.mapper.remote.PostersRemoteMapper
import com.amsterdam.repository.mapper.remote.ProductionCompanyRemoteMapper
import com.amsterdam.repository.mapper.remote.ReviewRemoteMapper
import com.amsterdam.repository.mapper.remote.SeasonRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowDetailsRemoteMapper
import com.amsterdam.repository.mapper.remote.TvShowRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.CountryRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieCategoryRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieGenreIdsRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowCategoryRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowGenreIdsRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteDetailsLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowRemoteLocalMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val mapperModule = module {
    singleOf(::SessionTypeMapper)
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
    singleOf(::MovieGenreIdsRemoteLocalMapper)
    singleOf(::TvShowGenreIdsRemoteLocalMapper)
    singleOf(::MovieCategoryRemoteLocalMapper)
    singleOf(::MovieRemoteLocalMapper)
    singleOf(::TvShowCategoryRemoteLocalMapper)
    singleOf(::TvShowRemoteLocalMapper)
    singleOf(::TvShowRemoteDetailsLocalMapper)
    singleOf(::MovieCategoryLocalMapper)
    singleOf(::MovieGenreLocalMapper)
    singleOf(::MovieWithCategoriesLocalMapper)
    singleOf(::TvShowCategoryLocalMapper)
    singleOf(::TvShowGenreLocalMapper)
    singleOf(::TvShowWithCategoryLocalMapper)
    singleOf(::PostersRemoteMapper)
    singleOf(::TvShowDetailsRemoteMapper)
    singleOf(::EpisodeRemoteMapper)
    singleOf(::SeasonRemoteMapper)
    singleOf(::MovieWatchHistoryMapper)
    singleOf(::TvShowWatchHistoryMapper)
}