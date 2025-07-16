package com.amsterdam.aflami.di

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.CountryRepository
import com.example.domain.repository.MovieRepository
import com.example.domain.repository.RecentSearchRepository
import com.example.domain.repository.TvShowRepository
import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.RecentSearchMapper
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.example.repository.mapper.remote.CountryRemoteMapper
import com.example.repository.mapper.remote.MovieRemoteMapper
import com.example.repository.mapper.remote.TvShowRemoteMapper
import com.example.repository.mapper.remote.RemoteCastMapper
import com.example.repository.mapper.remote.RemoteGalleryMapper
import com.example.repository.mapper.remote.RemoteProductionCompanyMapper
import com.example.repository.mapper.remote.RemoteReviewMapper
import com.example.repository.repository.CategoryRepositoryImpl
import com.example.repository.repository.CountryRepositoryImpl
import com.example.repository.repository.MovieRepositoryImpl
import com.example.repository.repository.RecentSearchRepositoryImpl
import com.example.repository.repository.TvShowRepositoryImpl
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.RecentSearchHandlerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    single { CountryLocalMapper() }
    single { CountryRemoteMapper() }
    single { CategoryLocalMapper() }
    single { CategoryRemoteMapper() }
    single { MovieLocalMapper(get()) }
    single { TvShowLocalMapper(get()) }
    single { MovieRemoteMapper() }
    single { TvShowRemoteMapper() }
    single { RecentSearchMapper() }
    single<RecentSearchHandler> { RecentSearchHandlerImpl(get()) }
    single { RemoteCastMapper() }
    single {  RemoteReviewMapper() }
    single { RemoteGalleryMapper() }
    single { RemoteProductionCompanyMapper() }
    single<CountryRepository> { CountryRepositoryImpl(get(), get(), get(), get()) }
    singleOf(::MovieRemoteMapper)
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get(), get(), get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get(), get(), get(), get(), get(),get(),get(),get()) }
    single<RecentSearchRepository> { RecentSearchRepositoryImpl(get(), get()) }
    single<TvShowRepository> { TvShowRepositoryImpl(get(), get(), get(), get(), get()) }
}