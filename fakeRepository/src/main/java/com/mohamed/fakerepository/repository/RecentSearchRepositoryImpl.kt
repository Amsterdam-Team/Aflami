package com.mohamed.fakerepository.repository

import com.example.domain.repository.RecentSearchRepository

class RecentSearchRepositoryImpl() : RecentSearchRepository {
    override suspend fun upsertRecentSearch(searchKeyword: String) {
        println("recent search $searchKeyword")
    }

    override suspend fun upsertRecentSearchForCountry(searchKeyword: String) {
        println("recent search country $searchKeyword")
    }

    override suspend fun upsertRecentSearchForActor(searchKeyword: String) {
        println("recent search actor $searchKeyword")
    }

    override suspend fun getAllRecentSearches(): List<String> {
        return listOf("spiderman", "sinners")
    }

    override suspend fun deleteAllRecentSearches() {
        println("recent search delete all")
    }

    override suspend fun deleteRecentSearch(searchKeyword: String) {
        println("recent search delete $searchKeyword")
    }
}