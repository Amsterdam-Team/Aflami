package com.amsterdam.aflami.di

import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.RecentSearchMapper
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.example.repository.mapper.remote.CountryRemoteMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.repository.CategoryRepositoryImpl
import com.example.repository.repository.CountryRepositoryImpl
import com.example.repository.repository.MovieRepositoryImpl
import com.example.repository.repository.RecentSearchRepositoryImpl
import com.example.repository.repository.TvShowRepositoryImpl
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.RecentSearchHandlerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::CountryLocalMapper)
    singleOf(::CountryRemoteMapper)
    singleOf(::CategoryLocalMapper)
    singleOf(::CategoryRemoteMapper)
    singleOf(::MovieLocalMapper)
    singleOf(::TvShowLocalMapper)
    singleOf(::MovieRemoteMapper)
    singleOf(::TvShowRemoteMapper)
    singleOf(::RecentSearchMapper)

    singleOf(::RecentSearchHandlerImpl) { bind<RecentSearchHandler>() }

    singleOf(::CountryRepositoryImpl)
    singleOf(::MovieRepositoryImpl)
    singleOf(::CategoryRepositoryImpl)
    singleOf(::RecentSearchRepositoryImpl)
    singleOf(::TvShowRepositoryImpl)
}
